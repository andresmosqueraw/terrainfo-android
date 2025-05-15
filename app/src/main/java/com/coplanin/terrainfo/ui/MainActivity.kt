package com.coplanin.terrainfo.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.coplanin.terrainfo.ui.login.LoginScreen
import com.coplanin.terrainfo.ui.login.LoginViewModel
import com.coplanin.terrainfo.ui.map.MapScreen
import com.coplanin.terrainfo.ui.theme.TerrainfoTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.hilt.navigation.compose.hiltViewModel
import android.content.Context
import android.content.SharedPreferences

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val sharedPreferences = getSharedPreferences("terrainfo_prefs", Context.MODE_PRIVATE)
        val lastLoginTimestamp = sharedPreferences.getLong("last_login_timestamp", 0L)
        val daysInMillis = 1 * 24 * 60 * 60 * 1000L
        val isLoggedIn = System.currentTimeMillis() - lastLoginTimestamp < daysInMillis

        setContent {
            TerrainfoTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = if (isLoggedIn) "map" else "login") {
                    composable("login") {
                        val loginViewModel: LoginViewModel = hiltViewModel()
                        LoginScreen(
                            loginViewModel = loginViewModel,
                            onLoginSuccess = {
                                saveLoginTimestamp(sharedPreferences)
                                navController.navigate("map") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        )
                    }
                    composable("map") {
                        MapScreen()
                    }
                }
            }
        }
    }

    private fun saveLoginTimestamp(sharedPreferences: SharedPreferences) {
        sharedPreferences.edit()
            .putLong("last_login_timestamp", System.currentTimeMillis())
            .apply()
    }
}
