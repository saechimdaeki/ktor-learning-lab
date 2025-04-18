package com.example.config

import com.example.config.AuthenticatedUser.Companion.SESSION_NAME
import com.example.shared.CafeUserRole
import io.ktor.server.application.*
import io.ktor.server.sessions.*
import kotlinx.serialization.Serializable

fun Application.configureSession() {
    install(Sessions) {
        cookie<AuthenticatedUser>(SESSION_NAME, SessionStorageMemory()) {
            cookie.path = "/"
        }
    }
}

@Serializable
data class AuthenticatedUser(
    val userId: Long,
    val userRoles: List<CafeUserRole>
) {
    companion object {
        fun none(): AuthenticatedUser = AuthenticatedUser(0, listOf())

        const val SESSION_NAME = "CU_SESSION_ID"
    }
}