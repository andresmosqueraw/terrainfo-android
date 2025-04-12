package com.coplanin.terrainfo.data.repository

import com.coplanin.terrainfo.data.model.LoginRequest
import com.coplanin.terrainfo.data.model.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse
}