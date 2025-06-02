package com.coplanin.terrainfo.ui.terreno

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.coplanin.terrainfo.data.local.entity.TerrainEntity
import com.coplanin.terrainfo.ui.components.EditFieldCard
import com.coplanin.terrainfo.ui.icons.ArrowBack
import com.coplanin.terrainfo.ui.predio.PredioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TerrenoEditScreen(
    id: String,
    etiqueta: String,
    ilcPredio: String,
    navController: NavController,
    viewModel: PredioViewModel = hiltViewModel()
) {
    // Estado local para el campo
    var etiquetaState by remember { mutableStateOf(etiqueta) }
    var ilcPredioState by remember { mutableStateOf(ilcPredio) }

    // Asegurarse de que el GeoPackage esté abierto al cargar la pantalla
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.openGeoPackage(context)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Terreno") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .padding(horizontal = 32.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            EditFieldCard(label = "Etiqueta", value = etiquetaState, onValueChange = { etiquetaState = it })
            EditFieldCard(label = "ILC Predio", value = ilcPredioState, onValueChange = { ilcPredioState = it })

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    val updatedTerreno = TerrainEntity(
                        idOperacionPredio = id,
                        etiqueta = etiquetaState,
                        ilcPredio = ilcPredioState
                    )
                    viewModel.updateTerreno(context, id, updatedTerreno)
                    navController.navigateUp()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1))
            ) {
                Text("Guardar", color = Color.White)
            }
        }
    }
}