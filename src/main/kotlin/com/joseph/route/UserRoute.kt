package com.joseph.route

import com.joseph.models.auth.AuthResponse
import com.joseph.repository.user.UserRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Routing.usersRoute() {

    val repository by inject<UserRepository>()

    route("/users") {

        get("/onboarding/{userId}") {
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
            val result = repository.fetchOnboardingUsers(userId)
            call.respond(
                status = result.code,
                message = result.data
            )
        }
        get("/{userId}") {
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
            val result = repository.fetchUserById(userId)
            call.respond(
                status = result.code,
                message = result.data
            )
        }
    }
}