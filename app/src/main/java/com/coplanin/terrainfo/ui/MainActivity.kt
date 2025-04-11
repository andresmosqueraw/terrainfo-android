package com.coplanin.terrainfo.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.coplanin.terrainfo.ui.login.LoginScreen
import com.coplanin.terrainfo.ui.login.LoginViewModel
import com.coplanin.terrainfo.ui.theme.TerrainfoTheme
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TerrainfoTheme {
                // Podemos inyectar el ViewModel si usamos Hilt, o simplemente crear uno local
                val loginViewModel = viewModel<LoginViewModel>()

                LoginScreen(
                    loginViewModel = loginViewModel,
                    onLoginSuccess = {
                        // Aquí navegaríamos a otra pantalla
                        // por ejemplo con Navigation Compose, o un simple Intent
                    }
                )
            }
        }
    }
}