package com.coplanin.terrainfo.ui.profile

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*              // unchanged
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.coplanin.terrainfo.ui.icons.ArrowBack
import com.coplanin.terrainfo.ui.icons.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val TAG = "ProfileScreen"
    Log.d(TAG, "ProfileScreen composable started")
    
    val user by viewModel.user.collectAsState()
    Log.d(TAG, "User state collected: ${user != null}")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil") },
                navigationIcon = {
                    IconButton(onClick = { 
                        Log.d(TAG, "Back button clicked")
                        navController.popBackStack() 
                    }) {
                        Icon(ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        user?.let { u ->
            Log.d(TAG, "Rendering user profile for: ${u.username}")
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(User, null, Modifier.size(96.dp))
                Spacer(Modifier.height(16.dp))
                Text("${u.firstName} ${u.lastName}", style = MaterialTheme.typography.headlineSmall)
                Text(u.email, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                Spacer(Modifier.height(24.dp))
                DetailRow("Nombre de usuario", u.username)
                DetailRow("Fecha de registro", u.dateJoined)
                DetailRow("Municipios", u.municipios)
                DetailRow("Permisos", u.permissions)

                Spacer(Modifier.height(32.dp))

                // Botón de cerrar sesión
                Button(
                    onClick = {
                        Log.d(TAG, "Logout button clicked")
                        viewModel.logout()
                        Log.d(TAG, "Navigating to login screen")
                        navController.navigate("login") {
                            popUpTo("profile") { inclusive = true }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Cerrar sesión", color = Color.White)
                }
            }
        } ?: run {
            Log.w(TAG, "No user data available")
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Column(Modifier.fillMaxWidth()) {
        Text(label, style = MaterialTheme.typography.labelLarge, color = Color.Gray)
        Text(value, style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(8.dp))
    }
}