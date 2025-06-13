package com.coplanin.terrainfo.util

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.util.Log
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.*
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.mapbox.mapboxsdk.style.sources.VectorSource
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter

/**
 * Configura el estilo con un MBTiles base (raster/vector) y, opcionalmente,
 * otro MBTiles de puntos.  Limit-in automáticamente la cámara a la BBOX del
 * archivo principal.
 *
 * @param map         Instancia obtenida desde MapView.getMapAsync
 * @param ctx         Contexto para leer assets / F.S.
 * @param mbtilesFile MBTiles base (p. ej. planet2.mbtiles)
 * @param pointsFile  MBTiles vectorial con layer de puntos (nullable)
 */
fun showMbTilesMap(
    map: MapboxMap,
    ctx: Context,
    mbtilesFile: File,
    pointsFile: File? = null,
    lockBounds: Boolean = true
) {
    /* ---------- 1. copiar style JSON a fichero temporal ---------- */
    val styleAsset = "bright.json"
    val styleTemp   = File(ctx.filesDir, styleAsset)
    ctx.assets.open(styleAsset).copyTo(styleTemp.outputStream())

    /* ---------- 2. ajustar placeholders ---------- */
    var styleText = styleTemp.readText()
        .replace("___FILE_URI___",   "mbtiles:///${mbtilesFile.absolutePath}")
    pointsFile?.let {
        styleText = styleText.replace(
            "___POINTS_FILE_URI___", "mbtiles:///${it.absolutePath}"
        )
    }
    BufferedWriter(FileWriter(styleTemp)).use { it.write(styleText) }

    /* ---------- 3. límites y zoom ----------- */
    val bounds  : LatLngBounds = getLatLngBounds(mbtilesFile)
    val minZoom : Double       = getMinZoom(mbtilesFile).toDouble()

    /* ---------- 4. aplicar estilo ----------- */
    map.setStyle(Style.Builder().fromUri(Uri.fromFile(styleTemp).toString())) { style ->

        /* 4a · capa de puntos si existe */
        pointsFile?.let { pf ->
            style.addSource(
                VectorSource("points-source", "mbtiles:///${pf.absolutePath}")
            )
            style.addLayer(
                com.mapbox.mapboxsdk.style.layers.CircleLayer(
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
        }

        /* 4b · opcional: debug de bbox (capa semitransparente) */
        //showBoundsArea(style, bounds, Color.MAGENTA, "bbox-src", "bbox-lyr", 0.15f)
    }

    /* ---------- 5. cámara ---------- */
    map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 0),
        object : MapboxMap.CancelableCallback {
            override fun onFinish() {
                if (lockBounds) {
                    map.setMinZoomPreference(minZoom)
                    map.setLatLngBoundsForCameraTarget(bounds)
                }
            }
            override fun onCancel() { /* no-op */ }
        })
}