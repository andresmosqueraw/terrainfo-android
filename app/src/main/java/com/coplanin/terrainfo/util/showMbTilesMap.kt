package com.coplanin.terrainfo.util

import android.content.Context
import android.graphics.Color
import android.net.Uri
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.CircleLayer
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.*
import com.mapbox.mapboxsdk.style.sources.VectorSource
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter

/**
 * Aplica el estilo `bright.json`, sustituyendo dinámicamente los
 * siguientes *place-holders* por rutas MBTiles:
 *
 *  - ___FILE_URI___      → vector base
 *  - ___TERRENOS_URI___  → polígonos de terreno (opcional)
 *  - ___PREDIOS_URI___   → polígonos de predio  (opcional)
 *
 * @param rasterFile   MBTiles vector (p.e. planet2.mbtiles)
 * @param terrenosFile MBTiles vectorial con capa "terrenos"   (nullable)
 * @param prediosFile  MBTiles vectorial con capa "unidad"     (nullable)
 * @param lockBounds   Si true, fija el mapa al BBOX del ráster
 */
fun showMbTilesMap(
    map: MapboxMap,
    ctx: Context,
    mbtilesFile: File,
    prediosFile : File? = null,
    terrenosFile: File? = null,
    lockBounds: Boolean = true
) {
    /* ---------- 1. copiar style JSON a fichero temporal ---------- */
    val styleAsset = "bright.json"
    val styleTemp   = File(ctx.filesDir, styleAsset)
    ctx.assets.open(styleAsset).copyTo(styleTemp.outputStream())

    /* ─ 2. Sustituimos los place-holders por URIs MBTiles ─────────────────── */
    var json = styleTemp.readText()
        .replace("___FILE_URI___",     "mbtiles:///${mbtilesFile.absolutePath}")

    json = if (terrenosFile != null)
        json.replace("___TERRENOS_URI___", "mbtiles:///${terrenosFile.absolutePath}")
    else   json.replace("___TERRENOS_URI___", "")

    json = if (prediosFile != null)
        json.replace("___PREDIOS_URI___",  "mbtiles:///${prediosFile.absolutePath}")
    else   json.replace("___PREDIOS_URI___", "")

    BufferedWriter(FileWriter(styleTemp)).use { it.write(json) }

    /* ---------- 3. límites y zoom ----------- */
    val bounds  : LatLngBounds = getLatLngBounds(mbtilesFile)
    val minZoom : Double       = getMinZoom(mbtilesFile).toDouble()

    /* ---------- 4. aplicar estilo ----------- */
    map.setStyle(Style.Builder().fromUri(Uri.fromFile(styleTemp).toString())) { style ->
        /* Si quisieras debug visual, añade aquí capas adicionales */

        /* 4a · capa de puntos si existe */
        /*prediosFile?.let { pf ->
            style.addSource(
                VectorSource("points-source", "mbtiles:///${pf.absolutePath}")
            )
            style.addLayer(
                CircleLayer(
                    "points-layer", "points-source"
                ).withSourceLayer("points")   // ajusta al nombre real de la capa
                    .withProperties(
                        circleRadius(6f),
                        circleColor(Color.RED),
                        circleOpacity(0.9f),
                        circleStrokeWidth(1.5f),
                        circleStrokeColor(Color.WHITE)
                    )
            )
        }*/

        /* 4b · opcional: debug de bbox (capa semitransparente) */
        //showBoundsArea(style, bounds, Color.MAGENTA, "bbox-src", "bbox-lyr", 0.15f)
    }

    /* ---------- 5. cámara ---------- */
    map.animateCamera(
        CameraUpdateFactory.newLatLngBounds(bounds, 0),
        object : MapboxMap.CancelableCallback {
            override fun onFinish() {
                if (lockBounds) {
                    map.setMinZoomPreference(minZoom)
                    map.setLatLngBoundsForCameraTarget(bounds)
                }
            }
            override fun onCancel() { /* no-op */ }
        }
    )
}