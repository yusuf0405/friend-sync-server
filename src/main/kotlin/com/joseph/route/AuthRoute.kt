package com.joseph.route

import com.joseph.models.auth.AuthResponse
import com.joseph.models.auth.SignInParams
import com.joseph.models.auth.SignUpParams
import com.joseph.repository.user.UserRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

val invalid_credentials = "Invalid credentials!"

fun Routing.authRouting() {

    val repository by inject<UserRepository>()

    route(path = "/signup") {
        post {
            val params = call.receiveNullable<SignUpParams>()
            if (params == null) {
                call.respond(
                    status = HttpStatusCode.BadRequest,
                    message = AuthResponse(
                        errorMessage = invalid_credentials
                    )
                )
                return@post
            }

            val result = repository.signUp(params = params)

            call.respond(
                status = result.code,
                message = result.data
            )
        }
    }
    route(path = "/login") {
        post {
            val params = call.receiveNullable<SignInParams>()
            if (params == null) {
                call.respond(
                    status = HttpStatusCode.BadRequest,
                    message = AuthResponse(
                        errorMessage = invalid_credentials
                    )
                )
                return@post
            }

            val result = repository.signIn(params = params)
            call.respond(
                status = result.code,
                message = result.data
            )
        }
    }
}