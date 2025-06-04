package com.coplanin.terrainfo.data.repository

import android.util.Log
import com.coplanin.terrainfo.data.local.dao.UserDao
import com.coplanin.terrainfo.data.local.entity.UserEntity
import com.coplanin.terrainfo.data.model.LoginRequest
import com.google.gson.Gson
import javax.inject.Inject
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient

class AuthRepository @Inject constructor(
    private val authService: AuthService,
    private val userDao: UserDao,
) {
    private val TAG = "AuthRepository"

    suspend fun login(
        username: String,
        password: String,
        municipio: String,
        loc: Location?          // ahora llega desde el VM
    ): String {
        Log.d(TAG, "Starting login process for user: $username in municipio: $municipio")
        Log.d(TAG, "Location: ${loc?.latitude}, ${loc?.longitude}")
        
        try {
            Log.d(TAG, "Making login request to backend")
            val res = authService.login(LoginRequest(username, password, municipio))
            Log.d(TAG, "Login response received successfully")
            
            val u = res.user
            Log.d(TAG, "User data received - ID: ${u.id_usuario}, Username: ${u.username}")
            
            Log.d(TAG, "Storing user data in local database")
            userDao.insert(
                UserEntity(
                    id = u.id_usuario,
                    username = u.username,
                    email = u.email,
                    firstName = u.firstName,
                    lastName = u.lastName,
                    dateJoined = u.dateJoined,
                    municipios = Gson().toJson(u.municipios),
                    permissions = Gson().toJson(u.permissions),
                    loginTimestamp = System.currentTimeMillis(),
                    lat = loc?.latitude ?: 0.0,
                    lon = loc?.longitude ?: 0.0
                )
            )
            Log.d(TAG, "User data stored successfully")
            
            Log.d(TAG, "Login process completed successfully")
            return res.token
        } catch (e: Exception) {
            Log.e(TAG, "Error during login process", e)
            throw e
        }
    }
}
