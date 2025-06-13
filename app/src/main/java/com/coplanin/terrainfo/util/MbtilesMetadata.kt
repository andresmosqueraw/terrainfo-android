package com.coplanin.terrainfo.util

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import java.io.File

private const val TAG = "MbtilesMeta"

/**
 * Devuelve la BBOX del MBTiles como `LatLngBounds`.
 *
 * El valor se lee de la tabla `metadata` → registro name = 'bounds'
 *   Formato:  "minLon,minLat,maxLon,maxLat"
 */
fun getLatLngBounds(mbtilesFile: File): LatLngBounds {
    val db = SQLiteDatabase.openDatabase(
        mbtilesFile.absolutePath, /* cursorFactory = */ null,
        SQLiteDatabase.OPEN_READONLY
    )

    val cursor = db.query(
        "metadata",
        arrayOf("value"),
        "name = ?",
        arrayOf("bounds"),
        null, null, null
    )

    val bounds = if (cursor.moveToFirst()) {
        cursor.getString(0)
    } else {
        ""
    }
    cursor.close(); db.close()

    // Valor de fallback (mundo entero) si el campo no existe
    if (bounds.isBlank()) {
        Log.w(TAG, "No se encontró 'bounds' en metadata de ${mbtilesFile.name}")
        return LatLngBounds.from(85.0, -180.0, -85.0, 180.0)
    }

    val (minLon, minLat, maxLon, maxLat) =
        bounds.split(',').map { it.trim().toDouble() }

    return LatLngBounds.Builder()
        .include(LatLng(minLat, minLon)) // SW
        .include(LatLng(maxLat, maxLon)) // NE
        .build()
}

/**
 * Lee `minzoom` de la tabla `metadata`.
 * Devuelve 0 si no existe o no es numérico.
 */
fun getMinZoom(mbtilesFile: File): Int {
    val db = SQLiteDatabase.openDatabase(
        mbtilesFile.absolutePath, null, SQLiteDatabase.OPEN_READONLY
    )

    val cursor = db.query(
        "metadata",
        arrayOf("value"),
        "name = ?",
        arrayOf("minzoom"),
        null, null, null
    )

    val minZoomStr = if (cursor.moveToFirst()) cursor.getString(0) else null
    cursor.close(); db.close()

    return minZoomStr?.toIntOrNull() ?: 0
}