package com.example.config

import com.example.config.plugin.ResponseDelayPlugin
import io.ktor.server.application.*
import io.ktor.server.plugins.doublereceive.*

fun Application.configureHttp() {
    install(DoubleReceive)
    install(ResponseDelayPlugin)
}