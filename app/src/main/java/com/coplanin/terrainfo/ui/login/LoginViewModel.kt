package com.coplanin.terrainfo.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    // Estado inmutable expuesto a la UI
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

    fun onLoginClicked() {
        // Validaciones básicas
        if (uiState.email.isBlank() || uiState.password.isBlank()) {
            uiState = uiState.copy(errorMessage = "Campos vacíos", isLoading = false)
            return
        }

        // Simular una llamada de red o autenticación
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null)
            delay(2000) // simula retraso de 2 segundos

            // Ejemplo: Si email y password coinciden con un "dummy" (test)
            if (uiState.email == "user@example.com" && uiState.password == "1234") {
                // Login exitoso
                uiState = uiState.copy(isLoading = false, errorMessage = null)
                // Navegar a otra pantalla o hacer algo...
            } else {
                // Error de credenciales
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