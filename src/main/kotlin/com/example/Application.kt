package com.example

import com.example.config.*
import com.example.domain.CafeMenuTable
import com.example.domain.repository.CafeMenuRepository
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureDatabase()
    configureDependencyInjection()
    configureSerialization()
    configureHttp()
    configureRouting()
    configureLogging()
}
