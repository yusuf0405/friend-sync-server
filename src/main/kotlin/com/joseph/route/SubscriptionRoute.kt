package com.joseph.route

import com.joseph.models.subscription.CreateOrCancelSubscription
import com.joseph.repository.subscription.SubscriptionRepository
import com.joseph.util.USER_ID_PARAM
import com.joseph.util.extensions.invalidCredentialsError
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

private const val SUBSCRIPTIONS_REQUEST_PATH = "/subscriptions"
private const val CREATE_SUBSCRIPTION_REQUEST_PATH = "/create"
private const val CANCEL_SUBSCRIPTION_REQUEST_PATH = "/cancel"
fun Routing.subscriptionRoute() {
    val repository by inject<SubscriptionRepository>()

    route(SUBSCRIPTIONS_REQUEST_PATH) {
        createSubscription(repository)
        cancelSubscription(repository)
        subscriptionUsers(repository)
        subscriptionUserIds(repository)
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
    get("/user-ids/{$USER_ID_PARAM}") {
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