package org.joseph.friendsync.plugins

import com.joseph.route.*
import io.ktor.server.routing.*
import io.ktor.server.application.*

fun Application.configureRouting() {
    routing {
        authRouting()
        postRoute()
        subscriptionRoute()
        usersRoute()
        categoryRoute()
    }
}
