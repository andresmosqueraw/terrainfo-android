package com.coplanin.terrainfo.ui.map

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.coplanin.terrainfo.R
import com.coplanin.terrainfo.data.local.entity.CommonDataEntity
import com.coplanin.terrainfo.ui.icons.ArrowBack
import com.coplanin.terrainfo.ui.icons.Plus
import com.coplanin.terrainfo.ui.icons.SearchIcon
import com.coplanin.terrainfo.ui.icons.User
import com.google.android.gms.maps.model.LatLng
import com.mapbox.maps.Style
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotation
import com.mapbox.maps.extension.compose.annotation.rememberIconImage
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.annotation.generated.PolygonAnnotation
import com.mapbox.maps.extension.compose.annotation.generated.PolygonAnnotationState
import com.mapbox.maps.extension.compose.style.MapStyle
import com.mapbox.maps.extension.compose.annotation.generated.PolylineAnnotation
import com.mapbox.maps.extension.compose.MapEffect
import androidx.compose.material3.OutlinedTextField
import com.mapbox.maps.plugin.gestures.gestures

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: MapViewModel = hiltViewModel()
) {
    // Configuración de límites de zoom
    // Referencia de niveles de zoom:
    // 1-3: Mundo/Continente  4-6: País/Estado  7-10: Región/Ciudad
    // 11-14: Zona urbana     15-17: Barrio     18-20: Calle/Edificio
    val MIN_ZOOM = 16.0   // Zoom mínimo (nivel región - para ver contexto geográfico amplio) (lo maximo que se puede ver)
    val MAX_ZOOM = 20.0  // Zoom máximo (nivel calle - detalle máximo para inspección de predios) (lo minimo que se puede ver)
    val DEFAULT_ZOOM = 16.0 // Zoom por defecto (nivel barrio - ideal para ver predios)
    /* --- Flujos de datos --- */
    val points by viewModel.points.collectAsState()
    val visits by viewModel.visits.collectAsState()
    val polygonPoints by viewModel.polygonPoints.collectAsState()
    val isAddingPoint by viewModel.isAddingPoint.collectAsState()

    /* --- Hoja inferior y cámara --- */
    val scaffoldState = rememberBottomSheetScaffoldState()

    /* --- Búsqueda en barra superior --- */
    var searchText by remember { mutableStateOf("") }

    /* --- Detalle seleccionado --- */
    var selectedVisit by remember { mutableStateOf<CommonDataEntity?>(null) }

    /* --- Centrar mapa cuando hay puntos --- */
    val viewportState = rememberMapViewportState {
        setCameraOptions {
            zoom(DEFAULT_ZOOM)
            center(Point.fromLngLat(-74.182224, 4.611598)) // Bogotá como posición inicial
        }
    }

    // Actualizar el viewport cuando los puntos estén disponibles
    LaunchedEffect(points) {
        if (points.isNotEmpty()) {
            val firstPoint = points.first()
            // Asegurar que el zoom inicial esté dentro de los límites configurados
            val zoomLevel = DEFAULT_ZOOM.coerceIn(MIN_ZOOM, MAX_ZOOM)
            viewportState.setCameraOptions {
                zoom(zoomLevel)
                center(Point.fromLngLat(firstPoint.latLng.longitude, firstPoint.latLng.latitude))
            }
            Log.d("MapScreen", "📸 Cámara centrada en: ${firstPoint.latLng} con zoom: $zoomLevel")
        }
    }

    // var showAddPropertyForm by remember { mutableStateOf(false) }
    // var newPointLatLng by remember { mutableStateOf<LatLng?>(null) }

    LaunchedEffect(selectedVisit) {
        scaffoldState.bottomSheetState.expand()
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
                Text(
                    text = "Puntos a visitar",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
                /* ---------- LISTA PRINCIPAL ---------- */
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                        .padding(start = 16.dp, end = 32.dp, bottom = 32.dp)
                        .verticalScroll(rememberScrollState()) // Habilitar scroll
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp)
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        // Lista de elementos
                        items(visits) { v ->
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedVisit = v }
                                    .padding(bottom = 4.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(IntrinsicSize.Min) // Para que el borde azul ocupe toda la altura del contenido
                                ) {
                                    // Borde azul izquierdo
                                    Box(
                                        modifier = Modifier
                                            .width(8.dp)
                                            .fillMaxHeight()
                                            .background(Color(0xFF0D47A1)) // Azul oscuro (puedes ajustarlo)
                                    )

                                    // Contenido del card
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
                }
            } else {
                /* Botón regreso */
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(onClick = { selectedVisit = null }) {
                        Icon(
                            imageVector = ArrowBack,
                            contentDescription = "Atrás"
                        )
                    }

                    Text(
                        text = "Detalle Predio",
                        style = MaterialTheme.typography.titleLarge
                    )
                }

                HorizontalDivider(modifier = Modifier.padding(top = 12.dp, bottom = 4.dp))

                /* ---------- DETALLE ---------- */
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                        .padding(horizontal = 24.dp)
                        .verticalScroll(rememberScrollState()) // Habilitar scroll
                ) {
                    // Card Información básica
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Min) // Asegura que el borde ocupe toda la altura
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(6.dp)
                                    .fillMaxHeight()
                                    .background(Color(0xFF0D47A1)) // Borde azul
                            )
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = "Información básica", style = MaterialTheme.typography.headlineSmall)
                                DetailRow("Id Operación", selectedVisit!!.idSearch)
                                DetailRow("Dirección", selectedVisit!!.address)
                                DetailRow("Ciudad", "${selectedVisit!!.cityDesc} (${selectedVisit!!.cityCode})")
                                DetailRow("Actividad", selectedVisit!!.activityName)
                                DetailRow("Código actividad", selectedVisit!!.activityCode)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Card Asignación
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
                                Text(text = "Asignación", style = MaterialTheme.typography.headlineSmall)
                                DetailRow("Fecha creación", selectedVisit!!.createDate)
                                DetailRow("Creado por", selectedVisit!!.createUserName)
                                DetailRow("Fecha evento", selectedVisit!!.eventDate)
                                DetailRow("Usuario evento", selectedVisit!!.eventUserName)
                                DetailRow("Evento Longitud/ Latitud", "${selectedVisit!!.eventX} / ${selectedVisit!!.eventY}") }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Card Captura
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
                                Text(text = "Captura", style = MaterialTheme.typography.headlineSmall)
                                DetailRow("Fecha captura", selectedVisit!!.captureDate)
                                DetailRow("Capturado por", selectedVisit!!.captureUserName)
                                DetailRow("Captura Longitud/ Latitud", "${selectedVisit!!.captureX} / ${selectedVisit!!.captureY}")
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Card Última edición
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
                                Text(text = "Última edición", style = MaterialTheme.typography.headlineSmall)
                                DetailRow("Última edición Longitud/ Latitud", "${selectedVisit!!.lastEditX} / ${selectedVisit!!.lastEditY}")
                                DetailRow("Última edición por", selectedVisit!!.lastEditUserName)
                                DetailRow("Última edición fecha", selectedVisit!!.lastEditDate)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Botón ir al predio
                Button(
                    onClick = {
                        selectedVisit?.let { navController.navigate("predio/${it.idSearch}") }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, top = 20.dp, end = 20.dp, bottom = 36.dp),
                    shape = RoundedCornerShape(30),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1))
                ) {
                    Text(text = "Ir al Predio", color = Color.White, style = MaterialTheme.typography.titleLarge)
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
            // Move marker icon creation outside to avoid recreation on recomposition
            val markerIcon = rememberIconImage(
                key = R.drawable.red_marker,
                painter = painterResource(R.drawable.red_marker)
            )

            MapboxMap(
                modifier = Modifier.fillMaxSize(),
                mapViewportState = viewportState,
                compass = {},        // Oculta brújula
                scaleBar = {},       // Oculta barra de escala
                logo = {},           // Oculta logo Mapbox
                attribution = {},    // Oculta atribución
                style = { MapStyle(style = Style.LIGHT) },
                onMapClickListener = if (isAddingPoint) { point ->
                    val latLng = LatLng(point.latitude(), point.longitude())
                    viewModel.addPoint(latLng)
                    navController.navigate("add_predio?lat=${point.latitude()}&lng=${point.longitude()}")
                    true
                } else null
            ) {
                // Configurar gestures y límites de zoom usando MapEffect
                MapEffect(key1 = Unit) { mapView ->
                    // Configurar límites de zoom usando CameraBoundsOptions
                    val cameraBoundsOptions = com.mapbox.maps.CameraBoundsOptions.Builder()
                        .minZoom(MIN_ZOOM)
                        .maxZoom(MAX_ZOOM)
                        .build()
                    mapView.mapboxMap.setBounds(cameraBoundsOptions)
                    Log.d("MapScreen", "🔍 Límites de zoom configurados: min=$MIN_ZOOM, max=$MAX_ZOOM")
                    
                    // Configurar controles de gestures
                    mapView.gestures.apply {
                        pinchToZoomEnabled = true                // ✅ Zoom con pellizco
                        scrollEnabled = true                     // ✅ Desplazamiento con un dedo
                        rotateEnabled = true                     // ✅ Rotación con dos dedos
                        pitchEnabled = false                     // ❌ Deshabilitar inclinación para mejor rendimiento
                        doubleTapToZoomInEnabled = true         // ✅ Doble tap para acercar
                        doubleTouchToZoomOutEnabled = true      // ✅ Doble tap con dos dedos para alejar
                        quickZoomEnabled = true                  // ✅ Quick zoom (doble tap y arrastrar)
                        simultaneousRotateAndPinchToZoomEnabled = true // ✅ Zoom y rotación simultáneos
                        Log.d("MapScreen", "🎮 Gestures configurados: pinchZoom✓ scroll✓ rotate✓ pitch✗")
                    }
                    
                    // Camera change listener disabled to prevent annotation source errors
                    // Previously: mapView.mapboxMap.subscribeCameraChanged { ... }
                    // This was causing frequent annotation recreation leading to orphaned sources
                    Log.d("MapScreen", "📷 Camera change listener disabled to prevent annotation source errors")
                }

                // Agregar polígonos
                polygonPoints.forEachIndexed { index, latLngList ->
                    val mappedPolygonPoints = latLngList.map {
                        Point.fromLngLat(it.longitude, it.latitude)
                    }

                    val polygonState = remember(index) { PolygonAnnotationState() }
                    polygonState.fillColor = Color(0xFFFFA500) // Color naranja
                    polygonState.fillOutlineColor = Color(0xFF000000) // Color negro
                    polygonState.fillOpacity = 0.3 // Opacidad del relleno

                    PolygonAnnotation(
                        points = listOf(mappedPolygonPoints),
                        polygonAnnotationState = polygonState
                    )
                }

                points.forEach { p ->
                    val geo = Point.fromLngLat(p.latLng.longitude, p.latLng.latitude)
                    key(p.id) { // Use key composable function to help Compose track annotations
                        PointAnnotation(point = geo) {
                            iconImage = markerIcon
                            iconSize = 0.8
                            interactionsState.onClicked {
                                Log.d("MapScreen", "Punto clicado: ${p.title} (Lat: ${p.latLng.latitude}, Lng: ${p.latLng.longitude})")
                                val matchingVisit = visits.find { it.idSearch == p.title }
                                selectedVisit = matchingVisit
                                true
                            }
                        }
                    }
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
                        Icon(imageVector = SearchIcon, contentDescription = "Buscar")
                    },
                    trailingIcon = {
                        IconButton(onClick = { navController.navigate("profile") }) {
                            Icon(imageVector = User, contentDescription = "Account", modifier = Modifier.size(24.dp))
                        }
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

            /* ---------- FAB con ícono "+" ---------- */
            FloatingActionButton(
                onClick = { viewModel.toggleAddPointMode() },
                containerColor = if (isAddingPoint) Color.Red else Color(0xFF0D47A1),
                shape = CircleShape,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(24.dp)
            ) {
                Icon(
                    imageVector = Plus,
                    contentDescription = if (isAddingPoint) "Cancelar" else "Agregar",
                    tint = Color.White
                )
            }

            /* ---------- Refresh FAB ---------- */
            FloatingActionButton(
                onClick = { viewModel.refreshMapData() },
                containerColor = Color(0xFF4CAF50),
                shape = CircleShape,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(start = 24.dp, end = 24.dp, bottom = 100.dp) // Position above the add button
            ) {
                Text(
                    text = "🔄",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )
            }

            // Show a message when in add point mode
            if (isAddingPoint) {
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 100.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White.copy(alpha = 0.9f),
                    shadowElevation = 4.dp
                ) {
                    Text(
                        text = "Toque en el mapa para agregar un punto",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

/* ---------- helper composable para el detalle ---------- */
@Composable
private fun DetailRow(label: String, value: String?) {
    var content = value
    if (value.isNullOrEmpty() || value == "null" || value == "N/A") {
        content = "No disponible"
    }
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(text = label, style = MaterialTheme.typography.labelLarge, color = Color.Gray)
        Text(text = content ?: "No disponible", style = MaterialTheme.typography.bodyLarge)
    }
}