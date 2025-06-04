package com.coplanin.terrainfo.ui.login

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.Manifest
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.VisualTransformation
import com.coplanin.terrainfo.R
import com.coplanin.terrainfo.ui.icons.Eye
import com.coplanin.terrainfo.ui.icons.EyeSlash
import com.coplanin.terrainfo.ui.icons.Person
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import android.os.Build

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class,
    ExperimentalPermissionsApi::class
)
@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel,      // ❶ sin valor por defecto
    onLoginSuccess: () -> Unit = {}
) {
    val tag = "LoginScreen"
    Log.d(tag, "LoginScreen composable started")

    val context = LocalContext.current

    val locationPermission = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    Log.d(tag, "Location permission status: ${locationPermission.status.isGranted}")

    val uiState = loginViewModel.uiState
    val municipios = listOf("CUITIVA", "IZA", "TRINIDAD")
    var expanded by remember { mutableStateOf(false) }
    var showPermissionDialog by remember { mutableStateOf(false) }

    // Verificar permisos necesarios
    val requiredPermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VIDEO,
            Manifest.permission.READ_MEDIA_AUDIO
        )
    } else {
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

    // Launcher para solicitar permisos
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.entries.all { it.value }
        Log.d(tag, "Permission result: allGranted=$allGranted, permissions=$permissions")
        if (allGranted) {
            // Todos los permisos concedidos, proceder con el login inmediatamente
            Log.d(tag, "All permissions granted, proceeding with login")
            loginViewModel.onLoginClicked(onLoginSuccess, null)
        } else {
            // Algunos permisos fueron denegados
            Log.d(tag, "Some permissions were denied, showing dialog")
            showPermissionDialog = true
        }
    }

    // Función para verificar y solicitar permisos
    fun checkAndRequestPermissions() {
        Log.d(tag, "Checking permissions")
        val permissionsToRequest = requiredPermissions.filter {
            ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
        }.toTypedArray()

        Log.d(tag, "Permissions to request: ${permissionsToRequest.joinToString()}")
        if (permissionsToRequest.isEmpty()) {
            // Ya tenemos todos los permisos, proceder con el login
            Log.d(tag, "All permissions already granted, proceeding with login")
            loginViewModel.onLoginClicked(onLoginSuccess, null)
        } else {
            // Solicitar permisos faltantes
            Log.d(tag, "Requesting missing permissions")
            permissionLauncher.launch(permissionsToRequest)
        }
    }

    uiState.errorMessage?.let { error ->
        Log.e(tag, "Showing error dialog: $error")
        AlertDialog(
            onDismissRequest = { 
                Log.d(tag, "Error dialog dismissed")
                loginViewModel.clearErrorMessage() 
            },
            confirmButton = {
                TextButton(onClick = { loginViewModel.clearErrorMessage() }) {
                    Text("OK")
                }
            },
            title = { Text("Error") },
            text = { Text(error) }
        )
    }

    // Diálogo para cuando se deniegan los permisos
    if (showPermissionDialog) {
        Log.d(tag, "Showing permission dialog")
        AlertDialog(
            onDismissRequest = { 
                Log.d(tag, "Permission dialog dismissed")
                showPermissionDialog = false 
            },
            title = { Text("Permisos necesarios") },
            text = { Text("Esta aplicación necesita permisos de ubicación y almacenamiento para funcionar correctamente.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        Log.d(tag, "Request permissions again clicked")
                        showPermissionDialog = false
                        // Volver a solicitar los permisos
                        permissionLauncher.launch(requiredPermissions)
                    }
                ) {
                    Text("Solicitar de nuevo")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { 
                        Log.d(tag, "Cancel permissions clicked")
                        showPermissionDialog = false 
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF004D00), Color(0xFF00B300))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Fondo con imagen
        Image(
            painter = painterResource(id = R.drawable.background_login),
            contentDescription = "Card Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.7f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text(
                    text = "TerraInfo",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Inicio de sesión",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Centro de control de predios, por favor ingrese sus credenciales para acceder.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(16.dp))

                // MUNICIPIO Dropdown
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = uiState.municipio,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Municipio", color = Color.White) },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White,
                            cursorColor = Color.White,
                            focusedLabelColor = Color.White
                        ),
                        modifier = Modifier
                            .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable)
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        municipios.forEach { municipio ->
                            DropdownMenuItem(
                                text = { Text(municipio) },
                                onClick = {
                                    loginViewModel.onMunicipioChange(municipio)
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // USUARIO
                OutlinedTextField(
                    value = uiState.email,
                    onValueChange = { loginViewModel.onEmailChange(it) },
                    label = { Text("Usuario", color = Color.White) },
                    singleLine = true,
                    trailingIcon = {
                        Icon(imageVector = Person, contentDescription = "Usuario", tint = Color.White)
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White,
                        cursorColor = Color.White,
                        focusedLabelColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))
                // Estado para alternar la visibilidad de la contraseña
                var passwordVisible by remember { mutableStateOf(false) }

                OutlinedTextField(
                    value = uiState.password,
                    onValueChange = { loginViewModel.onPasswordChange(it) },
                    label = { Text("Clave", color = Color.White) },
                    singleLine = true,
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Eye else EyeSlash,
                                contentDescription = if (passwordVisible) "Ocultar clave" else "Mostrar clave",
                                tint = Color.White
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White,
                        cursorColor = Color.White,
                        focusedLabelColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        Log.d(tag, "Login button clicked")
                        checkAndRequestPermissions()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006400)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    if (uiState.isLoading) {
                        Log.d(tag, "Showing loading indicator")
                        CircularProgressIndicator(color = Color.White)
                    } else {
                        Text("Ingresar", color = Color.White, fontSize = 18.sp)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    /* MaterialTheme {
        LoginScreen()
    }*/
}
