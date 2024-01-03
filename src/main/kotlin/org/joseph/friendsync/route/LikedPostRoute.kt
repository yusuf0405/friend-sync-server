package org.joseph.friendsync.route

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.joseph.friendsync.models.likes.LikeOrUnlikeParams
import org.joseph.friendsync.repository.likes.LikesPostRepository
import org.joseph.friendsync.util.*
import org.joseph.friendsync.util.extensions.invalidCredentialsError
import org.koin.ktor.ext.inject

private const val LIKES_REQUEST_PATH = "/likes"

fun Routing.likedPostRoute() {

    val repository by inject<LikesPostRepository>()

    route(path = LIKES_REQUEST_PATH) {
        likePost(repository)
        unlikePost(repository)
        likedPosts(repository)
    }
}

fun Route.likedPosts(repository: LikesPostRepository) {
    get("/{$USER_ID_PARAM}") {
        val userId = call.parameters[USER_ID_PARAM]?.toIntOrNull()
        if (userId == null) {
            call.invalidCredentialsError(USER_ID_PARAM)
            return@get
        }
        val result = repository.fetchLikedPosts(userId)
        call.respond(
            status = result.code,
            message = result.data
        )
    }
}

private fun Route.likePost(repository: LikesPostRepository) {
    post(LIKE_PARAM) {
        val params = call.receiveNullable<LikeOrUnlikeParams>()
        if (params == null) {
            call.invalidCredentialsError()
            return@post
        }
        val result = repository.likePost(
            userId = params.userId,
            postId = params.postId,
        )

        call.respond(
            status = result.code,
            message = result.data
        )
    }
}

private fun Route.unlikePost(repository: LikesPostRepository) {
    post(UNLIKE_PARAM) {
        val params = call.receiveNullable<LikeOrUnlikeParams>()
        if (params == null) {
            call.invalidCredentialsError()
            return@post
        }
        val result = repository.unlikePost(
            userId = params.userId,
            postId = params.postId,
        )
        call.respond(
            status = result.code,
            message = result.data
        )
    }
}