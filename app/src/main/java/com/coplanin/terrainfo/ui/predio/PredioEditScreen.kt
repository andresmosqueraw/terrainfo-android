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
import androidx.compose.ui.graphics.Color

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
    navController: NavController
) {
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
            EditFieldCard(label = "Código ORIP", value = codigoOrip)
            EditFieldCard(label = "Matrícula Inmobiliaria", value = matricula)
            EditFieldCard(label = "Área Catastral Terreno", value = areaTerreno)
            EditFieldCard(label = "Número Predial Nacional", value = id)
            DropdownFieldCard(label = "Tipo", options = listOf("Privado", "Público"), selected = tipo)
            DropdownFieldCard(label = "Condición Predio", options = listOf("NPH", "Propio"), selected = condicion)
            DropdownFieldCard(label = "Destino Económico", options = listOf("Residencial", "Comercial"), selected = destino)
            EditFieldCard(label = "Área Registral m²", value = areaRegistral)

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = { /* guardar */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1))
            ) {
                Text("Guardar", color = Color.White)
            }
        }
    }
}
