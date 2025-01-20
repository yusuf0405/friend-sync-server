package org.joseph.friendsync

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.joseph.friendsync.db.dao.DatabaseFactory
import org.joseph.friendsync.di.configureDi
import org.joseph.friendsync.plugins.configureRouting
import org.joseph.friendsync.plugins.configureSecurity
import org.joseph.friendsync.plugins.configureSerialization

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    DatabaseFactory.init()
    configureSerialization()
    configureDi()
    configureSecurity()
    configureRouting()
}