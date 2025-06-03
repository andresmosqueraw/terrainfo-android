package com.coplanin.terrainfo.data.repository

import com.coplanin.terrainfo.data.local.dao.UserDao
import com.coplanin.terrainfo.data.local.entity.UserEntity
import com.coplanin.terrainfo.data.model.LoginRequest
import com.coplanin.terrainfo.data.utils.GsonUtils
import javax.inject.Inject
import android.location.Location

class AuthRepository @Inject constructor(
    private val authService: AuthService,
    private val userDao: UserDao,
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
                municipios = GsonUtils.getGson().toJson(u.municipios),
                permissions = GsonUtils.getGson().toJson(u.permissions),
                loginTimestamp = System.currentTimeMillis(),
                lat = loc?.latitude ?: 0.0,
                lon = loc?.longitude ?: 0.0
            )
        )
        return res.token
    }
}
