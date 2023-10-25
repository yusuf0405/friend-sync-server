package com.joseph

import com.joseph.db.dao.DatabaseFactory
import com.joseph.di.configureDi
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.joseph.plugins.*

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
