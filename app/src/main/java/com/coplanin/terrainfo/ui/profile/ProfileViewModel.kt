package com.coplanin.terrainfo.ui.profile

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

    val user: StateFlow<UserEntity?> =
        userDao.observeCurrent()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null
            )

    fun logout() {
        viewModelScope.launch {
            userDao.insert(UserEntity(0, "", "", "", "", "", "[]", "[]", 0L, 0.0, 0.0))
        }
    }
}