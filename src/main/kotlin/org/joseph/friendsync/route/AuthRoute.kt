package org.joseph.friendsync.route

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.joseph.friendsync.models.auth.SignInParams
import org.joseph.friendsync.models.auth.SignUpParams
import org.joseph.friendsync.repository.auth.AuthRepository
import org.joseph.friendsync.util.extensions.invalidCredentialsError
import org.koin.ktor.ext.inject

private const val SIGN_UP_REQUEST_PATH = "/signup"
private const val LOGIN_REQUEST_PATH = "/login"
fun Routing.authRouting() {
    val repository by inject<AuthRepository>()
    signup(repository)
    login(repository)
}

private fun Route.signup(repository: AuthRepository) {
    route(path = SIGN_UP_REQUEST_PATH) {
        post {
            val params = call.receiveNullable<SignUpParams>()
            if (params == null) {
                call.invalidCredentialsError()
                return@post
            }

            val result = repository.signUp(params = params)

            call.respond(
                status = result.code,
                message = result.data
            )
        }
    }
}

private fun Route.login(repository: AuthRepository) {
    route(path = LOGIN_REQUEST_PATH) {
        post {
            val params = call.receiveNullable<SignInParams>()
            if (params == null) {
                call.invalidCredentialsError()
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