package com.coplanin.terrainfo.ui.login

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val municipio: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

