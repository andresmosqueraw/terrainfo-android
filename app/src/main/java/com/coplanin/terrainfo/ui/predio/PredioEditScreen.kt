package com.coplanin.terrainfo.ui.predio

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.coplanin.terrainfo.ui.components.DropdownFieldCard
import com.coplanin.terrainfo.ui.components.EditFieldCard
import com.coplanin.terrainfo.ui.icons.ArrowBack
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.coplanin.terrainfo.data.local.entity.PredioEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PredioEditScreen(
    id: String,
    codigoOrip: String,
    matricula: String,
    areaTerreno: String,
    tipo: String,
    condicion: String,
    destino: String,
    areaRegistral: String,
    navController: NavController,
    viewModel: PredioViewModel = hiltViewModel()
) {
    // Estados locales para los campos
    var codigoOripState by remember { mutableStateOf(codigoOrip) }
    var matriculaState by remember { mutableStateOf(matricula) }
    var areaTerrenoState by remember { mutableStateOf(areaTerreno) }
    var tipoState by remember { mutableStateOf(tipo) }
    var condicionState by remember { mutableStateOf(condicion) }
    var destinoState by remember { mutableStateOf(destino) }
    var areaRegistralState by remember { mutableStateOf(areaRegistral) }

    // Si estás usando Compose y necesitas un Context
    val context = LocalContext.current

    // Asegúrate de pasar este `context` a las funciones que lo requieran
    viewModel.getPredioAndTerrain(context, id)
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Predio") },
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            EditFieldCard(label = "Código ORIP", value = codigoOripState, onValueChange = { codigoOripState = it })
            EditFieldCard(label = "Matrícula Inmobiliaria", value = matriculaState, onValueChange = { matriculaState = it })
            EditFieldCard(label = "Área Catastral Terreno", value = areaTerrenoState, onValueChange = { areaTerrenoState = it })
            EditFieldCard(
                label = "Número Predial Nacional",
                value = id,
                onValueChange = {}, // Lambda vacía para cumplir con el requisito
                readOnly = true
            )
            DropdownFieldCard(label = "Tipo", options = listOf("Privado", "Público"), selected = tipoState, onSelectedChange = { tipoState = it })
            DropdownFieldCard(label = "Condición Predio", options = listOf("NPH", "Propio"), selected = condicionState, onSelectedChange = { condicionState = it })
            DropdownFieldCard(label = "Destino Económico", options = listOf("Residencial", "Comercial"), selected = destinoState, onSelectedChange = { destinoState = it })
            EditFieldCard(label = "Área Registral m²", value = areaRegistralState, onValueChange = { areaRegistralState = it })

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    val updatedPredio = PredioEntity(
                        codigoOrip = codigoOripState,
                        matricula = matriculaState,
                        areaTerreno = areaTerrenoState,
                        numeroPredial = id,
                        tipo = tipoState,
                        condicion = condicionState,
                        destino = destinoState,
                        areaRegistral = areaRegistralState
                    )
                    viewModel.updatePredio(id, updatedPredio)
                    viewModel.getPredioAndTerrain(context, id) // Forzar recarga
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
