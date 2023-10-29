package com.joseph.db.dao.post

import com.joseph.db.dao.DatabaseFactory.dbQuery
import com.joseph.db.tables.PostImageUrlRow
import com.joseph.db.tables.PostRow
import com.joseph.db.tables.UserRow
import com.joseph.mappers.ResultRowToPostMapper
import com.joseph.models.post.AddPostParams
import com.joseph.models.post.Post
import org.jetbrains.exposed.sql.*
import java.util.*

class PostDaoImpl(
    private val resultRowToPostMapper: ResultRowToPostMapper,
) : PostDao {
    override suspend fun addPost(params: AddPostParams): Post? {
        return dbQuery {
            val insertStatement = PostRow.insert {
                it[message] = params.message ?: ""
                it[userId] = params.userId
                it[likesCount] = 0
                it[savedCount] = 0
                it[commentsCount] = 0
                it[releaseDate] = System.currentTimeMillis()
            }
            val postRow = insertStatement.resultedValues?.singleOrNull()
            params.imageUrls.forEach { url ->
                PostImageUrlRow.insert {
                    it[postId] = postRow?.get(PostRow.id) ?: -1
                    it[imageUrl] = url
                }.resultedValues?.singleOrNull()
            }

            if (postRow != null) rowToPost(postRow) else null
        }
    }

    override suspend fun fetchUserPosts(userId: Int): List<Post> {
        return dbQuery {
            PostRow.select { PostRow.userId eq userId }
                .orderBy(PostRow.releaseDate, SortOrder.DESC)
                .map(::rowToPost)
        }
    }

    override suspend fun fetchUserRecommendedPosts(
        page: Int,
        pageSize: Int,
        followedUserIds: List<Int>
    ): List<Post> {
        val offset = (page - 1) * pageSize
        return dbQuery {
            PostRow.select { PostRow.userId inList followedUserIds }
                .orderBy(PostRow.releaseDate, SortOrder.DESC)
                .limit(pageSize, offset.toLong())
                .map(::rowToPost)
        }
    }

    override suspend fun searchPostsWithParams(
        page: Int,
        pageSize: Int,
        query: String
    ): List<Post> {
        val offset = (page - 1) * pageSize
        // Добавлена подстановочный знак "%" в запрос для частичного совпадения
        val searchQuery = "%${query.lowercase(Locale.getDefault())}%"
        return dbQuery {
            PostRow.select { PostRow.message.lowerCase() like searchQuery }
                .orderBy(PostRow.releaseDate, SortOrder.DESC)
                .limit(pageSize, offset.toLong())
                .map(::rowToPost)
        }
    }

    private fun rowToPost(row: ResultRow): Post {
        val userRow = UserRow.select { UserRow.id eq row[PostRow.userId] }.singleOrNull()
        val imageUrls = PostImageUrlRow
            .select { PostImageUrlRow.postId eq row[PostRow.id] }
            .map { it[PostImageUrlRow.imageUrl] }
        return resultRowToPostMapper.map(row, userRow, imageUrls)
    }
}