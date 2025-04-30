package com.coplanin.terrainfo.data.repository

import android.health.connect.datatypes.ExerciseRoute
import com.coplanin.terrainfo.data.local.dao.UserDao
import com.coplanin.terrainfo.data.local.entity.UserEntity
import com.coplanin.terrainfo.data.model.LoginRequest
import com.coplanin.terrainfo.data.model.LoginResponse
import com.google.gson.Gson
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient

class AuthRepository @Inject constructor(
    private val authService: AuthService,
    private val userDao: UserDao,
    private val fused: FusedLocationProviderClient
) {
    suspend fun login(
        username: String,
        password: String,
        municipio: String,
        loc: Location?          // ahora llega desde el VM
    ): String {
        val res = authService.login(LoginRequest(username, password, municipio))
        val u = res.user
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
        return res.token
    }
}
