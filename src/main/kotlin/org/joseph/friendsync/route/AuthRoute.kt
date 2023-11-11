package org.joseph.friendsync.route

import com.joseph.models.auth.SignInParams
import com.joseph.models.auth.SignUpParams
import com.joseph.repository.auth.AuthRepository
import com.joseph.util.extensions.invalidCredentialsError
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
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