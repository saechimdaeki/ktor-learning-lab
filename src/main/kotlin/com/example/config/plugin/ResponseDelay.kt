package com.example.config.plugin

import io.ktor.server.application.*

val ResponseDelayPlugin = createRouteScopedPlugin(name = "ResponseDelayPlugin") {
    onCall {
        Thread.sleep(500)
    }
}