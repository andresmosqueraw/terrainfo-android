package com.coplanin.terrainfo.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coplanin.terrainfo.data.repository.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: AuthRepository = AuthRepository() // Inyecta aquí si usas DI
) : ViewModel() {

    var uiState by mutableStateOf(LoginUiState())
        private set

    fun onMunicipioChange(newMunicipio: String) {
        uiState = uiState.copy(municipio = newMunicipio)
    }

    fun onEmailChange(newEmail: String) {
        uiState = uiState.copy(email = newEmail)
    }

    fun onPasswordChange(newPassword: String) {
        uiState = uiState.copy(password = newPassword)
    }

    fun onLoginClicked(onLoginSuccess: () -> Unit) {
        // Validación básica
        if (uiState.email.isBlank() || uiState.password.isBlank() || uiState.municipio.isBlank()) {
            uiState = uiState.copy(
                errorMessage = "Campos vacíos",
                isLoading = false
            )
            return
        }

        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null)

            try {
                // Llamada real al servicio de login
                val response = repository.login(
                    username = uiState.email,
                    password = uiState.password,
                    municipio = uiState.municipio
                )

                // Si la llamada fue exitosa, aquí tienes el token y el user
                // val token = response.token
                // val user = response.user

                // Login exitoso
                uiState = uiState.copy(isLoading = false, errorMessage = null)
                onLoginSuccess()

            } catch (e: Exception) {
                // Maneja el error que ocurra (timeout, credenciales inválidas, etc.)
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Error de red"
                )
            }
        }
    }

    fun clearErrorMessage() {
        uiState = uiState.copy(errorMessage = null)
    }
}