package com.coplanin.terrainfo.data.model

data class LoginRequest(
    val username: String,
    val password: String,
    val municipio: String
)

data class LoginResponse(
    val token: String,
    val user: User
)

data class User(
    val id: Int,
    val username: String,
    val email: String
)