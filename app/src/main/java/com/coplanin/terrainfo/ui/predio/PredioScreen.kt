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
import com.coplanin.terrainfo.data.local.entity.TerrainEntity
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
    val detail by viewModel
        .getPredioAndTerrain(context, visitId)
        .collectAsState(initial = null)


    if (detail == null) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        val (predio, terreno) = detail!!

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
            PredioContent(
                predio = predio,
                terreno = terreno,
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
private fun Detail(label: String, value: String?) {
    val content = value?.takeIf { it.isNotBlank() } ?: "No disponible"
    Text("$label:  $content", style = MaterialTheme.typography.bodyLarge)
}

@Composable
private fun PredioContent(
    predio: PredioEntity,
    terreno: TerrainEntity?,
    modifier: Modifier = Modifier
) {
    Log.d("PredioScreen", "🧾 UI predio=$predio | terreno=$terreno")

    Column(modifier.padding(24.dp)) {

        /* ---------- CARD - PREDIO ---------- */
        CardSection(title = "Predio") {
            Detail("Código ORIP", predio.codigoOrip)
            Detail("Matrícula Inmobiliaria", predio.matricula)
            Detail("Área Catastral Terreno", predio.areaTerreno)
            Detail("Número Predial Nacional", predio.numeroPredial)
            Detail("Tipo", predio.tipo)
            Detail("Condición Predio", predio.condicion)
            Detail("Destino Económico", predio.destino)
            Detail("Área Registral m²", predio.areaRegistral)
        }

        Spacer(Modifier.height(24.dp))

        /* ---------- CARD - TERRENO ---------- */
        CardSection(title = "Terreno") {
            Detail("Id Operación Predio", terreno?.idOperacionPredio)
            Detail("Etiqueta", terreno?.etiqueta)
        }
    }
}

/* Reutilizable: envuelve bloques de detalles con look&feel uniforme */
@Composable
private fun CardSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(
                Modifier.width(6.dp).fillMaxHeight()
                    .background(Color(0xFF0D47A1))
            )
            Column(Modifier.padding(16.dp)) {
                Text(title, style = MaterialTheme.typography.headlineSmall)
                content()
            }
        }
    }
}
