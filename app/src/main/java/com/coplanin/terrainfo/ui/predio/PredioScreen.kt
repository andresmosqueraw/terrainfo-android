package com.coplanin.terrainfo.ui.predio

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.coplanin.terrainfo.data.local.entity.CommonDataEntity
import com.coplanin.terrainfo.ui.icons.ArrowBack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PredioScreen(
    navController: NavController,
    visitId: String,
    viewModel: PredioViewModel = hiltViewModel()
) {
    // ───── Recuperar el registro de base de datos ─────
    val predio by viewModel.getPredio(visitId).collectAsState(initial = null)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("ColSmart IGAC") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(ArrowBack, contentDescription = "Atrás")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors()
            )
        }
    ) { inner ->
        if (predio == null) {
            Box(Modifier
                .padding(inner)
                .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            PredioContent(
                predio = predio!!,
                modifier = Modifier
                    .padding(inner)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            )
        }
    }
}

/* --------------------- UI DETALLE --------------------- */

@Composable
private fun PredioContent(predio: CommonDataEntity, modifier: Modifier = Modifier) {
    Column(modifier.padding(24.dp)) {

        Text("Actualización", style = MaterialTheme.typography.displaySmall)

        Spacer(Modifier.height(24.dp))

        Card(
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
            ) {
                Box(
                    modifier = Modifier
                        .width(6.dp)
                        .fillMaxHeight()
                        .background(Color(0xFF0D47A1))
                )
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Predio", style = MaterialTheme.typography.headlineMedium)
                    Detail("Código_ORIP", "test")
                    Detail("Matrícula Inmobiliaria", "test")
                    Detail("Área Catastral Terreno", "test")
                    Detail("Número Predial Nacional", "test")
                    Detail("Tipo", "test")
                    Detail("Condición Predio", "test")
                    Detail("Destino Económico", "test")
                    Detail("Área Registral M2", "test")
                }
            }
        }
    }
}

@Composable
private fun Detail(label: String, value: String?) {
    val content = value?.takeIf { it.isNotBlank() } ?: "No disponible"
    Text("$label:  $content", style = MaterialTheme.typography.bodyLarge)
}