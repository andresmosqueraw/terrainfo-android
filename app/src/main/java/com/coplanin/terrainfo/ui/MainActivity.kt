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
import com.coplanin.terrainfo.ui.maps.MapScreen
import com.coplanin.terrainfo.ui.theme.TerrainfoTheme
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TerrainfoTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "login") {
                    composable("login") {
                        // Inyectamos (o creamos localmente) el ViewModel
                        val loginViewModel = viewModel<LoginViewModel>()
                        LoginScreen(
                            loginViewModel = loginViewModel,
                            onLoginSuccess = {
                                navController.navigate("map")
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
}
