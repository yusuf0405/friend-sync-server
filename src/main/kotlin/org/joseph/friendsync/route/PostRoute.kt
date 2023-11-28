package org.joseph.friendsync.route

import org.joseph.friendsync.models.post.AddPostParams
import org.joseph.friendsync.repository.post.PostRepository
import org.joseph.friendsync.util.*
import org.joseph.friendsync.util.extensions.invalidCredentialsError
import org.joseph.friendsync.util.extensions.saveImage
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.io.File

private const val POST_REQUEST_PATH = "/post"
private const val ADD_POST_REQUEST_PATH = "/add"
private const val RECOMMENDED_POST_REQUEST_PATH = "/recommended"

fun Routing.postRoute() {

    val repository by inject<PostRepository>()

    staticFiles("/images", File("static/post_pictures"))

    route(path = POST_REQUEST_PATH) {
        userPosts(repository)
        postById(repository)
        recommendedPosts(repository)
        addPost(repository)
        searchPosts(repository)
    }
}

fun Route.postById(repository: PostRepository) {
    get("/{$POST_ID_PARAM}") {
        val postId = call.parameters[POST_ID_PARAM]?.toIntOrNull()
        if (postId == null) {
            call.invalidCredentialsError(POST_ID_PARAM)
            return@get
        }
        val result = repository.fetchPostById(postId)
        call.respond(
            status = result.code,
            message = result.data
        )
    }
}

private fun Route.userPosts(repository: PostRepository) {
    get("list/{$USER_ID_PARAM}") {
        val userId = call.parameters[USER_ID_PARAM]?.toIntOrNull()
        if (userId == null) {
            call.invalidCredentialsError(USER_ID_PARAM)
            return@get
        }
        val result = repository.fetchUserPosts(userId)
        call.respond(
            status = result.code,
            message = result.data
        )
    }
}

private fun Route.recommendedPosts(repository: PostRepository) {
    get(RECOMMENDED_POST_REQUEST_PATH) {
        val userId = call.parameters[USER_ID_PARAM]?.toIntOrNull()
        val page = call.parameters[PAGE_PARAM]?.toIntOrNull()
        val pageSize = call.parameters[PAGE_SIZE_PARAM]?.toIntOrNull()
        if (userId == null) {
            call.invalidCredentialsError(USER_ID_PARAM)
            return@get
        }
        if (page == null) {
            call.invalidCredentialsError(PAGE_PARAM)
            return@get
        }

        if (pageSize == null) {
            call.invalidCredentialsError(PAGE_SIZE_PARAM)
            return@get
        }

        val result = repository.fetchUserRecommendedPosts(
            page = page,
            userId = userId,
            pageSize = pageSize
        )
        call.respond(
            status = result.code,
            message = result.data
        )
    }
}

private fun Route.searchPosts(repository: PostRepository) {
    get(SEARCH_REQUEST_PATCH) {
        val pageSize = call.parameters[PAGE_SIZE_PARAM]?.toIntOrNull()
        val page = call.parameters[PAGE_PARAM]?.toIntOrNull()
        val query = call.parameters[QUERY_PARAM]
        if (pageSize == null) {
            call.invalidCredentialsError(PAGE_SIZE_PARAM)
            return@get
        }
        if (page == null) {
            call.invalidCredentialsError(PAGE_PARAM)
            return@get
        }

        if (query == null) {
            call.invalidCredentialsError(QUERY_PARAM)
            return@get
        }
        val result = repository.searchPostsWithParams(
            page = page,
            pageSize = pageSize,
            query = query
        )
        call.respond(
            status = result.code,
            message = result.data
        )
    }

}

private fun Route.addPost(repository: PostRepository) {
    post(ADD_POST_REQUEST_PATH) {
        try {
            val multipart = call.receiveMultipart()

            var fileName: String? = null
            var message: String? = null
            var userId: Int? = null
            val imageUrls = mutableListOf<String>()

            var index = 0
            try {
                multipart.forEachPart { partData ->
                    when (partData) {
                        is PartData.FileItem -> {
                            if (partData.name == "${IMAGE_PARAM_NAME}$index") {
                                index++
                                fileName = partData.saveImage(POST_IMAGE_PATH)
                                val imageUrl = "${EXTERNAL_POST_IMAGE_PATH}/$fileName"
                                imageUrls.add(imageUrl)
                            }
                        }

                        is PartData.FormItem -> {
                            if (partData.name == MESSAGE_PARAM_NAME) message = partData.value
                            if (partData.name == USER_ID_PARAM) userId = partData.value.toInt()
                        }

                        else -> Unit
                    }
                }

            } catch (ex: Exception) {
                File("${POST_IMAGE_PATH}/$fileName").delete()
                ex.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError, ex.message.toString())
            }

            if (userId == null) {
                call.invalidCredentialsError(USER_ID_PARAM)
                return@post
            }
            val response = repository.addPost(
                params = AddPostParams(
                    userId = userId!!,
                    imageUrls = imageUrls,
                    message = message
                )
            )

            call.respond(
                status = response.code,
                message = response.data
            )
        } catch (ex: Exception) {
            call.respond(HttpStatusCode.BadRequest, ex.message.toString())
        }
    }
}