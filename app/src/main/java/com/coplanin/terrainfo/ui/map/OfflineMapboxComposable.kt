package com.coplanin.terrainfo.ui.map

import android.graphics.Color
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.coplanin.terrainfo.util.getFileFromAssets
import com.coplanin.terrainfo.util.showMbTilesMap
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import java.io.File

/**
 * Shows a MapView that reads a raster MBTiles (background) and an optional
 * vector MBTiles containing points.
 *
 * @param mbTilesName      – raster or vector tiles (planet2.mbtiles)
 * @param pointsTilesName  – vector mbtiles with point layer (predio_terreno_new.mbtiles)
 */
@Composable
fun OfflineMapboxComposable(
    modifier: Modifier = Modifier,
    mbTilesName: String,
    pointsTilesName: String? = null
) {
    val context = LocalContext.current
    val mapView = rememberMapViewWithLifecycle(context)

    // This callback fires exactly once (MapView ready)
    AndroidView(
        modifier = modifier,
        factory = { mapView },
        update = { view ->
            view.getMapAsync { mapboxMap ->
                // NB:  we reuse the helper functions from MainActivity.kt verbatim
                val mainMbtiles = getFileFromAssets(context, mbTilesName)
                val pointsMbtiles = pointsTilesName?.let { getFileFromAssets(context, it) }
                showMbTilesMap(mapboxMap, context, mainMbtiles, pointsMbtiles)
            }
        }
    )
}