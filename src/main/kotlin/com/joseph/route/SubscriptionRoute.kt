package com.joseph.route

import com.joseph.models.auth.AuthResponse
import com.joseph.models.subscription.CreateOrCancelSubscription
import com.joseph.models.subscription.FetchSubscriptionInfo
import com.joseph.repository.subscription.SubscriptionRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Routing.subscriptionRoute() {

    val repository by inject<SubscriptionRepository>()

    route("/subscriptions") {

        post("/create") {
            val params = call.receiveNullable<CreateOrCancelSubscription>()

            if (params == null) {
                call.respond(
                    status = HttpStatusCode.BadRequest,
                    message = AuthResponse(
                        errorMessage = invalid_credentials
                    )
                )
                return@post
            }

            val result = repository.createSubscription(params)
            call.respond(
                status = result.code,
                message = result.data
            )
        }
        get("/user-ids") {
            val params = call.receiveNullable<FetchSubscriptionInfo>()

            if (params == null) {
                call.respond(
                    status = HttpStatusCode.BadRequest,
                    message = AuthResponse(
                        errorMessage = invalid_credentials
                    )
                )
                return@get
            }

            val result = repository.fetchSubscriptionUserIds(params.userId)
            call.respond(
                status = result.code,
                message = result.data
            )
        }
        get("/users") {
            val params = call.receiveNullable<FetchSubscriptionInfo>()

            if (params == null) {
                call.respond(
                    status = HttpStatusCode.BadRequest,
                    message = AuthResponse(
                        errorMessage = invalid_credentials
                    )
                )
                return@get
            }

            val result = repository.fetchSubscriptionUsers(params.userId)
            call.respond(
                status = result.code,
                message = result.data
            )
        }

        post("/cancel") {
            val params = call.receiveNullable<CreateOrCancelSubscription>()
            if (params == null) {
                call.respond(
                    status = HttpStatusCode.BadRequest,
                    message = AuthResponse(
                        errorMessage = invalid_credentials
                    )
                )
                return@post
            }

            val result = repository.cancelSubscription(params)
            call.respond(
                status = result.code,
                message = result.data
            )
        }
    }

}