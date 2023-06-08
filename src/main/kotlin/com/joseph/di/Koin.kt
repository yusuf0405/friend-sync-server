package com.joseph.di

import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin

fun Application.configureDi() {
    install(Koin) {
        modules(appModule)
    }
}