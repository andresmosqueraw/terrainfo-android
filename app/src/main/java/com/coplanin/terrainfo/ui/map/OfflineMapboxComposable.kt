package com.coplanin.terrainfo.ui.map

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.coplanin.terrainfo.util.getFileFromAssets
import com.coplanin.terrainfo.util.showMbTilesMap

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
    mbTilesName : String,          // ráster
    terrenosTiles: String? = null, // vector 1
    prediosTiles : String? = null  // vector 2  ← NUEVO
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
                val prediosMbtiles = prediosTiles?.let { getFileFromAssets(context, it) }
                val terrenosMbtiles = terrenosTiles?.let { getFileFromAssets(context, it) }
                showMbTilesMap(mapboxMap, context, mainMbtiles, prediosMbtiles, terrenosMbtiles, lockBounds = true)
            }
        }
    )
}