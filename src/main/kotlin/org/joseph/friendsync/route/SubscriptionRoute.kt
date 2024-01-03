package org.joseph.friendsync.route

import org.joseph.friendsync.models.subscription.CreateOrCancelSubscription
import org.joseph.friendsync.repository.subscription.SubscriptionRepository
import org.joseph.friendsync.util.USER_ID_PARAM
import org.joseph.friendsync.util.extensions.invalidCredentialsError
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.joseph.friendsync.util.FOLLOWING_ID_PARAM
import org.koin.ktor.ext.inject

private const val SUBSCRIPTIONS_REQUEST_PATH = "/subscriptions"
private const val CREATE_SUBSCRIPTION_REQUEST_PATH = "/create"
private const val CANCEL_SUBSCRIPTION_REQUEST_PATH = "/cancel"
private const val HAS_SUBSCRIPTION = "/has_subscription"
fun Routing.subscriptionRoute() {
    val repository by inject<SubscriptionRepository>()

    route(SUBSCRIPTIONS_REQUEST_PATH) {
        createSubscription(repository)
        cancelSubscription(repository)
        subscriptionUsers(repository)
        hasUserSubscription(repository)
        subscriptionUserIds(repository)
        subscriptionUser(repository)
    }
}

private fun Route.hasUserSubscription(repository: SubscriptionRepository) {
    get(HAS_SUBSCRIPTION) {
        val userId = call.parameters[USER_ID_PARAM]?.toIntOrNull()
        val followingId = call.parameters[FOLLOWING_ID_PARAM]?.toIntOrNull()
        if (userId == null) {
            call.invalidCredentialsError(USER_ID_PARAM)
            return@get
        }
        if (followingId == null) {
            call.invalidCredentialsError(FOLLOWING_ID_PARAM)
            return@get
        }
        val result = repository.hasUserSubscription(userId, followingId)
        call.respond(
            status = result.code,
            message = result.data
        )
    }
}

private fun Route.createSubscription(repository: SubscriptionRepository) {
    post(CREATE_SUBSCRIPTION_REQUEST_PATH) {
        val params = call.receiveNullable<CreateOrCancelSubscription>()
        if (params == null) {
            call.invalidCredentialsError()
            return@post
        }

        val result = repository.createSubscription(params)
        call.respond(
            status = result.code,
            message = result.data
        )
    }
}

private fun Route.cancelSubscription(repository: SubscriptionRepository) {
    post(CANCEL_SUBSCRIPTION_REQUEST_PATH) {
        val params = call.receiveNullable<CreateOrCancelSubscription>()
        if (params == null) {
            call.invalidCredentialsError()
            return@post
        }

        val result = repository.cancelSubscription(params)
        call.respond(
            status = result.code,
            message = result.data
        )
    }
}

private fun Route.subscriptionUsers(repository: SubscriptionRepository) {
    get("/users/{$USER_ID_PARAM}") {
        val userId = call.parameters[USER_ID_PARAM]?.toIntOrNull()
        if (userId == null) {
            call.invalidCredentialsError(USER_ID_PARAM)
            return@get
        }
        val result = repository.fetchSubscriptionUsers(userId)
        call.respond(
            status = result.code,
            message = result.data
        )
    }
}

private fun Route.subscriptionUserIds(repository: SubscriptionRepository) {
    get("/{$USER_ID_PARAM}") {
        val userId = call.parameters[USER_ID_PARAM]?.toIntOrNull()
        if (userId == null) {
            call.invalidCredentialsError(USER_ID_PARAM)
            return@get
        }

        val result = repository.fetchSubscriptionUserIds(userId)
        call.respond(
            status = result.code,
            message = result.data
        )
    }
}

private fun Route.subscriptionUser(repository: SubscriptionRepository) {
    get("list/{$USER_ID_PARAM}") {
        val userId = call.parameters[USER_ID_PARAM]?.toIntOrNull()
        if (userId == null) {
            call.invalidCredentialsError(USER_ID_PARAM)
            return@get
        }

        val result = repository.fetchUserSubscriptions(userId)
        call.respond(
            status = result.code,
            message = result.data
        )
    }
}