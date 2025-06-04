package com.coplanin.terrainfo.ui.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coplanin.terrainfo.data.local.dao.UserDao
import com.coplanin.terrainfo.data.local.entity.UserEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userDao: UserDao
) : ViewModel() {
    private val TAG = "ProfileViewModel"

    val user: StateFlow<UserEntity?> =
        userDao.observeCurrent()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null
            ).also {
                Log.d(TAG, "User StateFlow initialized")
            }

    init {
        Log.d(TAG, "ProfileViewModel initialized")
        viewModelScope.launch {
            user.collect { user ->
                Log.d(TAG, "User state updated: ${user?.username ?: "null"}")
            }
        }
    }

    fun logout() {
        Log.d(TAG, "Logout initiated")
        viewModelScope.launch {
            Log.d(TAG, "Clearing user data from database")
            userDao.insert(UserEntity(0, "", "", "", "", "", "[]", "[]", 0L, 0.0, 0.0))
            Log.d(TAG, "User data cleared")
        }
    }
}