package com.joseph.plugins

import com.joseph.route.authRouting
import com.joseph.route.postRoute
import com.joseph.route.subscriptionRoute
import com.joseph.route.usersRoute
import io.ktor.server.routing.*
import io.ktor.server.application.*

fun Application.configureRouting() {
    routing {
        authRouting()
        postRoute()
        subscriptionRoute()
        usersRoute()
    }
}
