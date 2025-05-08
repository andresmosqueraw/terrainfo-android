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

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TerrainfoTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "login") {
                    composable("login") {
                        val loginViewModel: LoginViewModel = hiltViewModel()
                        LoginScreen(
                            loginViewModel = loginViewModel,
                            onLoginSuccess = { navController.navigate("map") }
                        )
                    }
                    composable("map") {
                        MapScreen()
                    }
                }
            }
        }
    }
}
