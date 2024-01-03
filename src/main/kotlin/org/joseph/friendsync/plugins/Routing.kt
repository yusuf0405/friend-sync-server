package org.joseph.friendsync.plugins

import org.joseph.friendsync.route.*
import io.ktor.server.routing.*
import io.ktor.server.application.*

fun Application.configureRouting() {
    routing {
        authRouting()
        postRoute()
        subscriptionRoute()
        usersRoute()
        categoryRoute()
        commentsRoute()
        likedPostRoute()
    }
}
