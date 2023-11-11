package org.joseph.friendsync.route

import org.joseph.friendsync.repository.user.UserRepository
import org.joseph.friendsync.util.*
import org.joseph.friendsync.util.extensions.invalidCredentialsError
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

private const val USERS_ROUTE_NAME = "/users"
private const val ONBOARDING_USERS_NAME = "/onboarding"
fun Routing.usersRoute() {
    val repository by inject<UserRepository>()

    route(USERS_ROUTE_NAME) {
        getById(repository)
        search(repository)
        onboarding(repository)
    }
}

private fun Route.onboarding(repository: UserRepository) {
    get("$ONBOARDING_USERS_NAME/{$USER_ID_PARAM}") {
        val userId = call.parameters[USER_ID_PARAM]?.toIntOrNull()
        if (userId == null) {
            call.invalidCredentialsError(USER_ID_PARAM)
            return@get
        }
        val result = repository.fetchOnboardingUsers(userId)
        call.respond(
            status = result.code,
            message = result.data
        )
    }
}

private fun Route.search(repository: UserRepository) {
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

        val result = repository.searchUsers(
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

private fun Route.getById(repository: UserRepository) {
    get("/{$USER_ID_PARAM}") {
        val userId = call.parameters[USER_ID_PARAM]?.toIntOrNull()
        if (userId == null) {
            call.invalidCredentialsError(USER_ID_PARAM)
            return@get
        }
        val result = repository.fetchUserById(userId)
        call.respond(
            status = result.code,
            message = result.data
        )
    }
}

