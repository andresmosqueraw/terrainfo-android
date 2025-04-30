package com.coplanin.terrainfo.ui.login

import android.location.Location
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coplanin.terrainfo.data.repository.AuthRepository
import com.coplanin.terrainfo.data.repository.CommonDataRepository
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepo: AuthRepository,
    private val commonRepo: CommonDataRepository
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

    fun onLoginClicked(onLoginSuccess: () -> Unit, location: Location?) {
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
                val token = authRepo.login(
                    username = uiState.email,
                    password = uiState.password,
                    municipio = uiState.municipio,
                    loc = location
                )
                commonRepo.sync(uiState.email, token)
                uiState = uiState.copy(isLoading = false)
                onLoginSuccess()
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val gson = GsonBuilder().create()
                val errorResponse = try {
                    gson.fromJson(errorBody, ErrorResponse::class.java)
                } catch (_: Exception) {
                    null
                }
                val messageFromServer = errorResponse?.error ?: e.message()
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = messageFromServer
                )
            } catch (e: Exception) {
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

data class ErrorResponse(val error: String?)