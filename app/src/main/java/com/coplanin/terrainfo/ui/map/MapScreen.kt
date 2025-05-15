package com.coplanin.terrainfo.ui.map

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.coplanin.terrainfo.data.local.entity.CommonDataEntity
import com.coplanin.terrainfo.ui.icons.ArrowBack
import com.coplanin.terrainfo.ui.icons.SearchIcon
import com.coplanin.terrainfo.ui.icons.User
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    viewModel: MapViewModel = hiltViewModel()
) {
    /* --- Flujos de datos --- */
    val points by viewModel.points.collectAsState()
    val visits by viewModel.visits.collectAsState()

    /* --- Hoja inferior y cámara --- */
    val scaffoldState = rememberBottomSheetScaffoldState()
    val bogota = LatLng(4.7110, -74.0721)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(bogota, 12f)
    }

    /* --- Búsqueda en barra superior --- */
    var searchText by remember { mutableStateOf("") }

    /* --- Detalle seleccionado --- */
    var selectedVisit by remember { mutableStateOf<CommonDataEntity?>(null) }

    /* --- Centrar mapa cuando hay puntos --- */
    LaunchedEffect(points) {
        if (points.isNotEmpty()) {
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(points.first().latLng, 14f),
                durationMs = 1_000
            )
        }
    }

    BottomSheetScaffold(
        modifier = modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        sheetPeekHeight = 120.dp,
        sheetShape = RoundedCornerShape(24.dp),
        sheetTonalElevation = 12.dp,
        sheetShadowElevation = 12.dp,
        sheetContainerColor = Color.White,

        /* --------- CONTENIDO HOJA INFERIOR --------- */
        sheetContent = {
            if (selectedVisit == null) {
                /* ---------- LISTA PRINCIPAL ---------- */
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(800.dp)
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "Puntos a visitar",
                        style = MaterialTheme.typography.titleLarge,
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(visits) { v ->
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedVisit = v }          // ⬅️ CLICK
                                    .padding(bottom = 4.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = SearchIcon,
                                        contentDescription = "Buscar",
                                        tint = Color(0xFF388E3C),
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = v.idSearch,
                                            style = MaterialTheme.typography.titleLarge
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = v.address,
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = Color.Gray
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                /* ---------- DETALLE ---------- */
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(800.dp)
                        .padding(horizontal = 24.dp)
                ) {
                    /* Botón regreso */
                    IconButton(onClick = { selectedVisit = null }) {
                        Icon(
                            imageVector = ArrowBack,
                            contentDescription = "Atrás"
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = selectedVisit!!.idSearch,
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = selectedVisit!!.address,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    /* Información completa (una por línea) */
                    DetailRow("Actividad", selectedVisit!!.activityName)
                    DetailRow("Código actividad", selectedVisit!!.activityCode)
                    DetailRow("Ciudad", "${selectedVisit!!.cityDesc} (${selectedVisit!!.cityCode})")
                    DetailRow("Capture date", selectedVisit!!.captureDate)
                    DetailRow("Capture X / Y", "${selectedVisit!!.captureX} / ${selectedVisit!!.captureY}")
                    DetailRow("Usuario evento", selectedVisit!!.eventUserName)
                    DetailRow("Fecha evento", selectedVisit!!.eventDate)
                    DetailRow("Evento X / Y", "${selectedVisit!!.eventX} / ${selectedVisit!!.eventY}")
                    DetailRow("Creado por", selectedVisit!!.createUserName)
                    DetailRow("Fecha creación", selectedVisit!!.createDate)
                }
            }
        }
    ) { innerPadding ->
        /* ------------- MAPA ------------- */
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState
            ) {
                points.forEach { p ->
                    Marker(
                        state = MarkerState(position = p.latLng),
                        title = p.title
                    )
                }
            }

            /* ----- Barra de búsqueda flotante ----- */
            Surface(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 48.dp)
                    .fillMaxWidth()
                    .align(Alignment.TopCenter),
                shape = RoundedCornerShape(50),
                shadowElevation = 8.dp,
                tonalElevation = 8.dp,
                color = Color.White
            ) {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    placeholder = { Text("Buscar aquí") },
                    leadingIcon = {
                        Icon(
                            imageVector = SearchIcon,
                            contentDescription = "Buscar"
                        )
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = User, // Cambia este ícono según lo que necesites
                            contentDescription = "Account",
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        disabledBorderColor = Color.Transparent,
                        errorBorderColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        errorContainerColor = Color.Transparent,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                )
            }
        }
    }
}

/* ---------- helper composable para el detalle ---------- */
@Composable
private fun DetailRow(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(text = label, style = MaterialTheme.typography.labelLarge, color = Color.Gray)
        Text(text = value, style = MaterialTheme.typography.bodyLarge)
    }
}