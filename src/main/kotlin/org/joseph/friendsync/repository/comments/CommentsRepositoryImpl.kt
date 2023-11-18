package org.joseph.friendsync.repository.comments

import io.ktor.http.*
import org.joseph.friendsync.db.dao.comments.CommentsDao
import org.joseph.friendsync.models.comments.CommentListResponse
import org.joseph.friendsync.models.comments.CommentResponse
import org.joseph.friendsync.repository.subscription.something_went_wrong
import org.joseph.friendsync.util.Response
import org.joseph.friendsync.util.extensions.callSafe

class CommentsRepositoryImpl(
    private val dao: CommentsDao,
) : CommentsRepository {

    override suspend fun addCommentToPost(
        userId: Int,
        postId: Int,
        commentText: String
    ): Response<CommentResponse> = callSafe(
        Response.Error(
            code = HttpStatusCode.InternalServerError,
            data = CommentResponse(
                errorMessage = something_went_wrong
            )
        )
    ) {
        val comment = dao.addCommentToPost(userId, postId, commentText)
        Response.Success(
            data = CommentResponse(
                data = comment
            )
        )
    }

    override suspend fun deleteCommentById(
        commentId: Int
    ): Response<Int> = callSafe(
        Response.Error(
            code = HttpStatusCode.InternalServerError,
            data = -1
        )
    ) {
        Response.Success(
            data = dao.deleteCommentById(commentId)
        )
    }

    override suspend fun editCommentById(
        commentId: Int,
        editedText: String
    ): Response<Int> = callSafe(
        Response.Error(
            code = HttpStatusCode.InternalServerError,
            data = -1
        )
    ) {
        Response.Success(
            data = dao.editCommentById(commentId, editedText)
        )
    }

    override suspend fun fetchAllPostComments(
        postId: Int
    ): Response<CommentListResponse> = callSafe(
        Response.Error(
            code = HttpStatusCode.InternalServerError,
            data = CommentListResponse(
                errorMessage = something_went_wrong
            )
        )
    ) {
        val comments = dao.fetchAllPostComments(postId)

        Response.Success(
            data = CommentListResponse(
                data = comments.filterNotNull()
            )
        )
    }
}