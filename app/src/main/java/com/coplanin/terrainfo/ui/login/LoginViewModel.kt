package com.coplanin.terrainfo.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
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
        if (uiState.email.isBlank() || uiState.password.isBlank()) {
            uiState = uiState.copy(errorMessage = "Campos vacíos", isLoading = false)
            return
        }

        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null)
            delay(2000) // Simula una llamada de red

            if (uiState.email == "user@example.com" && uiState.password == "1234") {
                // Login exitoso
                uiState = uiState.copy(isLoading = false, errorMessage = null)
                onLoginSuccess() // Invoca la navegación al mapa
            } else {
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = "Credenciales incorrectas"
                )
            }
        }
    }

    fun clearErrorMessage() {
        uiState = uiState.copy(errorMessage = null)
    }
}