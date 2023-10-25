package com.joseph.route

import com.joseph.models.auth.AuthResponse
import com.joseph.models.post.AddPostParams
import com.joseph.models.subscription.FetchSubscriptionInfo
import com.joseph.repository.post.PostRepository
import com.joseph.util.EXTERNAL_POST_IMAGE_PATH
import com.joseph.util.POST_IMAGE_PATH
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.io.File
import java.util.*


private const val IMAGE_PARAM_NAME = "image"
private const val MESSAGE_PARAM_NAME = "message"
private const val USER_ID_PARAM_NAME = "user_id"
fun Routing.postRoute() {

    val repository by inject<PostRepository>()

    staticFiles("/images", File("static/post_pictures"))

    route(path = "/post") {
        post("/add") {
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
                                    fileName = partData.save(POST_IMAGE_PATH)
                                    val imageUrl = "${EXTERNAL_POST_IMAGE_PATH}/$fileName"
                                    imageUrls.add(imageUrl)
                                }
                            }

                            is PartData.FormItem -> {
                                if (partData.name == MESSAGE_PARAM_NAME) message = partData.value
                                if (partData.name == USER_ID_PARAM_NAME) userId = partData.value.toInt()
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
                    call.respond(
                        status = HttpStatusCode.BadRequest,
                        message = AuthResponse(
                            errorMessage = invalid_credentials
                        )
                    )
                    return@post
                }
                val response = repository.addPost(
                    post = AddPostParams(
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

        get("list/{userId}") {
            val userId = call.parameters["userId"]?.toIntOrNull()

            if (userId == null) {
                call.respond(
                    status = HttpStatusCode.BadRequest,
                    message = AuthResponse(
                        errorMessage = invalid_credentials
                    )
                )
                return@get
            }
            val result = repository.fetchUserPosts(userId)
            call.respond(
                status = result.code,
                message = result.data
            )
        }
    }
}

fun PartData.FileItem.save(path: String): String {
    val fileBytes = streamProvider().readBytes()
    val fileExtension = originalFileName?.takeLastWhile { it != '.' }
    val fileName = UUID.randomUUID().toString() + "." + fileExtension
    val folder = File(path)
    if (!folder.parentFile.exists()) {
        folder.parentFile.mkdirs()
    }
    folder.mkdir()
    File("$path$fileName").writeBytes(fileBytes)
    return fileName
}
