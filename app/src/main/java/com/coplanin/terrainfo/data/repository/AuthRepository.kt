package com.coplanin.terrainfo.data.repository

import com.coplanin.terrainfo.data.model.LoginRequest
import com.coplanin.terrainfo.data.model.LoginResponse

class AuthRepository {
    private val authService = ApiClient.retrofit.create(AuthService::class.java)

    suspend fun login(username: String, password: String, municipio: String): LoginResponse {
        val request = LoginRequest(username, password, municipio)
        return authService.login(request)
    }
}