package org.joseph.friendsync.db.dao.comments

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.joseph.friendsync.db.dao.DatabaseFactory.dbQuery
import org.joseph.friendsync.db.tables.CommentsRow
import org.joseph.friendsync.mappers.ResultRowToCommentMapper
import org.joseph.friendsync.models.comments.Comment

class CommentsDaoImpl(
    private val resultRowToCommentMapper: ResultRowToCommentMapper
) : CommentsDao {

    override suspend fun addCommentToPost(
        userId: Int,
        postId: Int,
        commentText: String
    ): Comment? = dbQuery {
        val result = CommentsRow.insert {
            it[CommentsRow.userId] = userId
            it[CommentsRow.postId] = postId
            it[CommentsRow.comment] = commentText
            it[CommentsRow.releaseDate] = System.currentTimeMillis()
            it[CommentsRow.likesCount] = 0
        }.resultedValues?.singleOrNull()

        if (result != null) resultRowToCommentMapper.map(result) else null
    }

    override suspend fun deleteCommentById(commentId: Int): Int {
        dbQuery {
            CommentsRow.deleteWhere { CommentsRow.id eq commentId }
        }
        return commentId
    }

    override suspend fun editCommentById(commentId: Int, editedText: String): Int {
        dbQuery {
            CommentsRow.update({ CommentsRow.id eq commentId }) {
                it[comment] = editedText
            }
        }
        return commentId
    }

    override suspend fun fetchAllPostComments(postId: Int): List<Comment?> {
        return dbQuery {
            CommentsRow
                .select { CommentsRow.postId eq postId }
                .orderBy(CommentsRow.releaseDate, SortOrder.DESC)
                .map { resultRowToCommentMapper.map(it) }
        }
    }

    override suspend fun fetchPostCommentsSize(postId: Int): Int {
        return dbQuery {
            CommentsRow
                .select { CommentsRow.postId eq postId }
                .count()
                .toInt()
        }
    }
}