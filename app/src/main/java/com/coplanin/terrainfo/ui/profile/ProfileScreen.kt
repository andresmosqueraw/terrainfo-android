package com.coplanin.terrainfo.ui.profile

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
@Composable                     // ⬅️  No Experimental annotation needed now
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val user by viewModel.user.collectAsState()

    Scaffold(
        topBar = {
            /*  ✅  Use the stable composable  */
            TopAppBar(
                title = { Text("Perfil") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        user?.let { u ->
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
                DetailRow("Username",    u.username)
                DetailRow("Joined",      u.dateJoined)
                DetailRow("Municipios",  u.municipios)
                DetailRow("Permisos",    u.permissions)
            }
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