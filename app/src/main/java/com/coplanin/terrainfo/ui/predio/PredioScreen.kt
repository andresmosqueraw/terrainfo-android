package com.coplanin.terrainfo.ui.predio

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.coplanin.terrainfo.data.local.entity.PredioEntity
import com.coplanin.terrainfo.ui.icons.ArrowBack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PredioScreen(
    navController: NavController,
    visitId: String,
    viewModel: PredioViewModel = hiltViewModel()
) {
    // ───── Recuperar el registro de base de datos ─────
    val context = LocalContext.current
    val predio by viewModel.getPredioFromGpkg(context, visitId).collectAsState(initial = null)


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("ColSmart IGAC") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(ArrowBack, contentDescription = "Atrás")
                    }
                },
                colors = topAppBarColors()
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
private fun PredioContent(predio: PredioEntity, modifier: Modifier = Modifier) {
    Log.d("PredioScreen", "🧾 Mostrando predio: $predio")
    Column(modifier.padding(24.dp)) {
        Text("Detalle Predio", style = MaterialTheme.typography.displaySmall)

        Spacer(Modifier.height(24.dp))

        Card(
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .width(6.dp)
                        .fillMaxHeight()
                        .background(Color(0xFF0D47A1))
                )
                Column(modifier = Modifier.padding(16.dp)) {
                    Detail("Código ORIP", predio.codigoOrip)
                    Detail("Matrícula Inmobiliaria", predio.matricula)
                    Detail("Área Catastral Terreno", predio.areaTerreno)
                    Detail("Número Predial Nacional", predio.numeroPredial)
                    Detail("Tipo", predio.tipo)
                    Detail("Condición Predio", predio.condicion)
                    Detail("Destino Económico", predio.destino)
                    Detail("Área Registral m²", predio.areaRegistral)
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