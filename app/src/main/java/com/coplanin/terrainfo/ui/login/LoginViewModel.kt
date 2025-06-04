package com.coplanin.terrainfo.ui.login

import android.location.Location
import android.util.Log
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
    private val TAG = "LoginViewModel"

    var uiState by mutableStateOf(LoginUiState())
        private set

    fun onMunicipioChange(newMunicipio: String) {
        Log.d(TAG, "Municipio changed to: $newMunicipio")
        uiState = uiState.copy(municipio = newMunicipio)
    }

    fun onEmailChange(newEmail: String) {
        Log.d(TAG, "Email changed to: $newEmail")
        uiState = uiState.copy(email = newEmail)
    }

    fun onPasswordChange(newPassword: String) {
        Log.d(TAG, "Password changed (length: ${newPassword.length})")
        uiState = uiState.copy(password = newPassword)
    }

    fun onLoginClicked(onLoginSuccess: () -> Unit, location: Location?) {
        Log.d(TAG, "Login attempt started")
        Log.d(TAG, "Location: ${location?.latitude}, ${location?.longitude}")
        
        if (uiState.email.isBlank() || uiState.password.isBlank() || uiState.municipio.isBlank()) {
            Log.w(TAG, "Login failed: Empty fields")
            uiState = uiState.copy(
                errorMessage = "Campos vacíos",
                isLoading = false
            )
            return
        }

        viewModelScope.launch {
            Log.d(TAG, "Starting login process")
            uiState = uiState.copy(isLoading = true, errorMessage = null)

            try {
                Log.d(TAG, "Calling authRepo.login with username: ${uiState.email}")
                val token = authRepo.login(
                    username = uiState.email,
                    password = uiState.password,
                    municipio = uiState.municipio,
                    loc = location
                )
                Log.d(TAG, "Login successful, token received (first 10 chars): ${token.take(10)}...")
                
                Log.d(TAG, "Starting sync process with token")
                commonRepo.sync(uiState.email, token)
                Log.d(TAG, "Sync completed successfully")
                
                uiState = uiState.copy(isLoading = false)
                Log.d(TAG, "Calling onLoginSuccess")
                onLoginSuccess()
            } catch (e: HttpException) {
                Log.e(TAG, "HTTP Exception during login", e)
                val errorBody = e.response()?.errorBody()?.string()
                Log.d(TAG, "Error body: $errorBody")
                
                val gson = GsonBuilder().create()
                val errorResponse = try {
                    gson.fromJson(errorBody, ErrorResponse::class.java)
                } catch (_: Exception) {
                    Log.e(TAG, "Failed to parse error response")
                    null
                }
                val messageFromServer = errorResponse?.error ?: e.message()
                Log.e(TAG, "Error message from server: $messageFromServer")
                
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = messageFromServer
                )
            } catch (e: Exception) {
                Log.e(TAG, "Unexpected error during login", e)
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Error de red"
                )
            }
        }
    }

    fun clearErrorMessage() {
        Log.d(TAG, "Clearing error message")
        uiState = uiState.copy(errorMessage = null)
    }
}

data class ErrorResponse(val error: String?)