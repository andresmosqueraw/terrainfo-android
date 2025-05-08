package com.coplanin.terrainfo.ui.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
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
    val points by viewModel.points.collectAsState()

    val bogota = LatLng(4.7110, -74.0721)
    val cameraPositionState = rememberCameraPositionState {
        /* Bogotá por defecto mientras carga la BD */
        position = CameraPosition.fromLatLngZoom(bogota, 12f)
    }
    var searchText by remember { mutableStateOf("") }

    /* Cuando cambie la lista, anima la cámara al primer punto */
    LaunchedEffect(points) {
        if (points.isNotEmpty()) {
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(points.first().latLng, 14f),
                durationMs = 1_000
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
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

        // Search bar UI
        Surface(
            modifier = Modifier
                .padding(16.dp, 48.dp, 16.dp, 0.dp)
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
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar"
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MapScreenPreview() {
    Box(modifier = Modifier.fillMaxSize()) {
        // Mapa simulado (placeholder en preview)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFE0E0E0)) // Gris claro simulando fondo de mapa
        )

        // Search bar UI (igual al original)
        Surface(
            modifier = Modifier
                .padding(16.dp, 48.dp, 16.dp, 0.dp)
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            shape = RoundedCornerShape(50),
            shadowElevation = 8.dp,
            tonalElevation = 8.dp,
            color = Color.White
        ) {
            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Buscar aquí") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar"
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
