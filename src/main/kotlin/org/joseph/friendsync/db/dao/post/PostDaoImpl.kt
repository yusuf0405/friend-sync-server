package org.joseph.friendsync.db.dao.post

import org.joseph.friendsync.db.dao.DatabaseFactory.dbQuery
import org.joseph.friendsync.db.tables.CategoriesRow
import org.joseph.friendsync.db.tables.PostImageUrlRow
import org.joseph.friendsync.db.tables.PostRow
import org.joseph.friendsync.db.tables.UserRow
import org.joseph.friendsync.mappers.ResultRowToPostMapper
import org.joseph.friendsync.models.post.AddPostParams
import org.joseph.friendsync.models.post.Post
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
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

    override suspend fun fetchPostById(postId: Int): Post? {
        return dbQuery {
            val postResult = PostRow.select { PostRow.id eq postId }
                .singleOrNull()
            if (postResult == null) null else rowToPost(postResult)
        }
    }

    override suspend fun fetchUserPosts(userId: Int): List<Post> {
        return dbQuery {
            PostRow.select { PostRow.userId eq userId }
                .orderBy(PostRow.releaseDate, SortOrder.DESC)
                .map { rowToPost(it) }
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
                .map { rowToPost(it) }
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
                .map { rowToPost(it) }
        }
    }

    private suspend fun rowToPost(row: ResultRow): Post {
        val userRow = UserRow.select { UserRow.id eq row[PostRow.userId] }.singleOrNull()
        val imageUrls = PostImageUrlRow
            .select { PostImageUrlRow.postId eq row[PostRow.id] }
            .map { it[PostImageUrlRow.imageUrl] }
        return resultRowToPostMapper.map(row, userRow, imageUrls)
    }
}