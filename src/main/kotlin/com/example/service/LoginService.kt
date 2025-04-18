package com.example.service

import com.example.config.AuthenticatedUser
import com.example.config.AuthenticatedUser.Companion.SESSION_NAME
import com.example.shared.dto.UserDto
import io.ktor.server.sessions.*

class LoginService(
    private val userService: UserService
) {
    fun login(
        cafeUserLoginRequest: UserDto.LoginRequest,
        currentSession: CurrentSession
    ) {

        checkNoSession(currentSession)

        val user = userService.getCafeUser(cafeUserLoginRequest.nickname, cafeUserLoginRequest.plainPassword)

        currentSession.set(
            AuthenticatedUser(user.id!!, user.roles)
        )

    }

    fun signup(cafeUserSignupRequest: UserDto.LoginRequest, currentSession: CurrentSession) {
        checkNoSession(currentSession)
        val user = userService.createCustomer(
            nickname = cafeUserSignupRequest.nickname,
            plainPassword = cafeUserSignupRequest.plainPassword
        )
        currentSession.set(
            AuthenticatedUser(user.id!!, user.roles)
        )
    }

    fun logout(currentSession: CurrentSession) {
        currentSession.clear(name = SESSION_NAME)
    }


    private fun checkNoSession(currentSession: CurrentSession) {
        val authenticatedUser = currentSession.get<AuthenticatedUser>()
        if (authenticatedUser != null)
            throw RuntimeException()
    }
}