package org.joseph.friendsync.route

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.joseph.friendsync.models.comments.AddCommentParams
import org.joseph.friendsync.models.comments.EditCommentParams
import org.joseph.friendsync.repository.comments.CommentsRepository
import org.joseph.friendsync.util.*
import org.joseph.friendsync.util.ADD_REQUEST_PATCH
import org.joseph.friendsync.util.extensions.invalidCredentialsError
import org.koin.ktor.ext.inject

private const val COMMENTS_REQUEST_PATH = "/comments"

fun Routing.commentsRoute() {

    val repository by inject<CommentsRepository>()

    route(path = COMMENTS_REQUEST_PATH) {
        addComment(repository)
        commentsByPostId(repository)
        editComment(repository)
        deleteCommentById(repository)
    }
}

private fun Route.addComment(repository: CommentsRepository) {
    post(ADD_REQUEST_PATCH) {
        val params = call.receiveNullable<AddCommentParams>()
        if (params == null) {
            call.invalidCredentialsError()
            return@post
        }
        val result = repository.addCommentToPost(
            userId = params.userId,
            postId = params.postId,
            commentText = params.commentText
        )

        call.respond(
            status = result.code,
            message = result.data
        )
    }
}

private fun Route.commentsByPostId(repository: CommentsRepository) {
    get("/{$POST_ID_PARAM}") {
        val postId = call.parameters[POST_ID_PARAM]?.toIntOrNull()
        if (postId == null) {
            call.invalidCredentialsError(POST_ID_PARAM)
            return@get
        }

        val result = repository.fetchAllPostComments(postId)
        call.respond(
            status = result.code,
            message = result.data
        )
    }
}

private fun Route.editComment(repository: CommentsRepository) {
    post(EDIT_REQUEST_PATCH) {
        val params = call.receiveNullable<EditCommentParams>()
        if (params == null) {
            call.invalidCredentialsError()
            return@post
        }

        val result = repository.editCommentById(
            commentId = params.commentId,
            editedText = params.editedText
        )
        call.respond(
            status = result.code,
            message = result.data
        )
    }
}

private fun Route.deleteCommentById(repository: CommentsRepository) {
    post("$DELETE_REQUEST_PATCH/{$COMMENTS_ID_PARAM}") {
        val commentId = call.parameters[COMMENTS_ID_PARAM]?.toIntOrNull()
        if (commentId == null) {
            call.invalidCredentialsError(COMMENTS_ID_PARAM)
            return@post
        }

        val result = repository.deleteCommentById(commentId)
        call.respond(
            status = result.code,
            message = result.data
        )
    }
}