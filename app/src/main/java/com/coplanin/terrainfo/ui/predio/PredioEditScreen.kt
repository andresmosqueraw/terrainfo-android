package com.coplanin.terrainfo.ui.predio

import androidx.compose.foundation.layout.Column
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PredioEditScreen(id: String, navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Predio") },
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
            EditFieldCard(label = "Código_ORIP", value = "ABC123")
            EditFieldCard(label = "Matrícula Inmobiliaria", value = "987654321")
            EditFieldCard(label = "Área Catastral Terreno", value = "250.75")
            EditFieldCard(label = "Número Predial Nacional", value = "123456789012345")

            DropdownFieldCard(label = "Tipo", options = listOf("Privado", "Público"), selected = "Privado")
            DropdownFieldCard(label = "Condición Predio", options = listOf("NPH", "Propio"), selected = "NPH")
            DropdownFieldCard(label = "Destino Económico", options = listOf("Residencial", "Comercial"), selected = "Residencial")
        }
    }
}
