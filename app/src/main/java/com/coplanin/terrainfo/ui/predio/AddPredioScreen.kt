package com.coplanin.terrainfo.ui.predio

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.coplanin.terrainfo.ui.icons.ArrowBack
import com.google.android.gms.maps.model.LatLng
import com.coplanin.terrainfo.data.local.entity.PredioEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPredioScreen(
    latLng: LatLng,
    navController: NavController,
    viewModel: PredioViewModel = hiltViewModel()
) {
    var idOperacion by remember { mutableStateOf("") }
    var matriculaInmobiliaria by remember { mutableStateOf("") }
    var numeroPredial by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("Urbano") }
    var expandedTipo by remember { mutableStateOf(false) }
    var condicionPredio by remember { mutableStateOf("Propiedad Privada") }
    var expandedCondicion by remember { mutableStateOf(false) }
    var destinoEconomico by remember { mutableStateOf("Comercial") }
    var expandedDestino by remember { mutableStateOf(false) }
    var areaCatastral by remember { mutableStateOf("") }
    var areaRegistral by remember { mutableStateOf("") }
    var tipoReferenciaFmiAntiguo by remember { mutableStateOf("") }

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agregar Nuevo Predio") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 32.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("Información Básica", style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(
                value = idOperacion,
                onValueChange = { idOperacion = it },
                label = { Text("Id_Operacion") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            
            Text("Información Registral", style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(
                value = matriculaInmobiliaria,
                onValueChange = { matriculaInmobiliaria = it },
                label = { Text("Matricula_Inmobiliaria") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = numeroPredial,
                onValueChange = { numeroPredial = it },
                label = { Text("Numero_Predial_Nacional") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = tipoReferenciaFmiAntiguo,
                onValueChange = { tipoReferenciaFmiAntiguo = it },
                label = { Text("Tipo Referencia FMI Antiguo") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            
            Text("Características", style = MaterialTheme.typography.titleMedium)
            ExposedDropdownMenuBox(
                expanded = expandedTipo,
                onExpandedChange = { expandedTipo = !expandedTipo }
            ) {
                OutlinedTextField(
                    value = tipo,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Tipo") },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTipo) }
                )
                DropdownMenu(
                    expanded = expandedTipo,
                    onDismissRequest = { expandedTipo = false }
                ) {
                    listOf("Urbano", "Rural").forEach {
                        DropdownMenuItem(
                            text = { Text(it) },
                            onClick = {
                                tipo = it
                                expandedTipo = false
                            }
                        )
                    }
                }
            }
            
            ExposedDropdownMenuBox(
                expanded = expandedCondicion,
                onExpandedChange = { expandedCondicion = !expandedCondicion }
            ) {
                OutlinedTextField(
                    value = condicionPredio,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Condicion_Predio") },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCondicion) }
                )
                DropdownMenu(
                    expanded = expandedCondicion,
                    onDismissRequest = { expandedCondicion = false }
                ) {
                    listOf("Propiedad Privada", "Propiedad Pública").forEach {
                        DropdownMenuItem(
                            text = { Text(it) },
                            onClick = {
                                condicionPredio = it
                                expandedCondicion = false
                            }
                        )
                    }
                }
            }
            
            ExposedDropdownMenuBox(
                expanded = expandedDestino,
                onExpandedChange = { expandedDestino = !expandedDestino }
            ) {
                OutlinedTextField(
                    value = destinoEconomico,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Destinacion_Economica") },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDestino) }
                )
                DropdownMenu(
                    expanded = expandedDestino,
                    onDismissRequest = { expandedDestino = false }
                ) {
                    listOf("Comercial", "Residencial", "Industrial", "Otro").forEach {
                        DropdownMenuItem(
                            text = { Text(it) },
                            onClick = {
                                destinoEconomico = it
                                expandedDestino = false
                            }
                        )
                    }
                }
            }
            
            Spacer(Modifier.height(8.dp))
            Text("Áreas", style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(
                value = areaCatastral,
                onValueChange = { areaCatastral = it },
                label = { Text("Area_Catastral_Terreno") },
                suffix = { Text("m²") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = areaRegistral,
                onValueChange = { areaRegistral = it },
                label = { Text("Area_Registral_M2") },
                suffix = { Text("m²") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(Modifier.height(8.dp))
            Text("Ubicación", style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(
                value = latLng.latitude.toString(),
                onValueChange = {},
                label = { Text("Latitud") },
                enabled = false,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = latLng.longitude.toString(),
                onValueChange = {},
                label = { Text("Longitud") },
                enabled = false,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    val predio = PredioEntity(
                        codigoOrip = idOperacion,
                        matricula = matriculaInmobiliaria,
                        areaTerreno = areaCatastral,
                        numeroPredial = numeroPredial,
                        tipo = tipo,
                        condicion = condicionPredio,
                        destino = destinoEconomico,
                        areaRegistral = areaRegistral,
                        tipoReferenciaFmiAntiguo = tipoReferenciaFmiAntiguo
                    )
                    viewModel.saveNewPredio(context, predio)
                    navController.navigateUp()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1))
            ) {
                Text("Guardar", color = Color.White)
            }

            Spacer(Modifier.height(24.dp))
        }
    }
} 