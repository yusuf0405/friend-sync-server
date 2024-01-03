package org.joseph.friendsync.db.dao.likes

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.joseph.friendsync.db.dao.DatabaseFactory.dbQuery
import org.joseph.friendsync.db.tables.LikesPostRow
import org.joseph.friendsync.models.likes.LikedPost

class LikesPostDaoImpl : LikesPostDao {

    override suspend fun likePost(userId: Int, postId: Int): LikedPost? {
        if (isPostLikedByUser(userId, postId)) return null

        val likedPostId = dbQuery {
            LikesPostRow.insert {
                it[this.userId] = userId
                it[this.postId] = postId
            }.resultedValues?.singleOrNull()
        }

        return LikedPost(likedPostId?.get(LikesPostRow.id) ?: return null, userId, postId)
    }

    override suspend fun unlikePost(userId: Int, postId: Int): LikedPost? {
        val deletedLikedPost = dbQuery {
            val result = LikesPostRow.select {
                (LikesPostRow.userId eq userId) and (LikesPostRow.postId eq postId)
            }.singleOrNull()

            println("result = ${result?.get(LikesPostRow.id)}")

            LikesPostRow.deleteWhere {
                (LikesPostRow.userId eq userId) and (LikesPostRow.postId eq postId)
            }
            result
        }
        println("deletedLikedPost = ${deletedLikedPost?.get(LikesPostRow.id)}")
        return deletedLikedPost?.let { LikedPost(it[LikesPostRow.id], userId, postId) }
    }

    private suspend fun isPostLikedByUser(userId: Int, postId: Int): Boolean {
        return dbQuery {
            LikesPostRow.select {
                (LikesPostRow.userId eq userId) and (LikesPostRow.postId eq postId)
            }.count() > 0
        }
    }

    override suspend fun fetchPostLikedCount(postId: Int): Int {
        return dbQuery {
            LikesPostRow.select { LikesPostRow.postId eq postId }.count()
        }.toInt()
    }

    override suspend fun fetchLikedPostsCount(userId: Int): List<LikedPost> {
        return dbQuery {
            LikesPostRow.select { LikesPostRow.userId eq userId }
                .map {
                    LikedPost(
                        id = it[LikesPostRow.id],
                        postId = it[LikesPostRow.postId],
                        userId = it[LikesPostRow.userId]
                    )
                }
        }
    }

    override suspend fun fetchUserLikedPostIds(userId: Int): List<Int> {
        return dbQuery {
            LikesPostRow.select { LikesPostRow.userId eq userId }
                .map { it[LikesPostRow.postId] }
        }
    }

    override suspend fun fetchPostLikeUserIds(postId: Int): List<Int> {
        return dbQuery {
            LikesPostRow.select { LikesPostRow.postId eq postId }
                .map { it[LikesPostRow.userId] }
        }
    }
}
