package com.joseph.util.extensions

import com.joseph.models.auth.AuthResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

suspend fun ApplicationCall.invalidCredentialsError(parameter: String = String()) {
    respond(
        status = HttpStatusCode.BadRequest,
        message = AuthResponse(errorMessage = "Invalid $parameter credentials!")
    )
}
