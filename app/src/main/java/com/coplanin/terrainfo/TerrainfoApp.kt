package com.coplanin.terrainfo

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp
import mil.nga.geopackage.GeoPackage

@HiltAndroidApp
class TerrainfoApp : Application() {
    private var geoPackage: GeoPackage? = null
    private val geoPackageLock = Any()

    fun closeGeoPackage() {
        synchronized(geoPackageLock) {
            Log.d(TAG, "🔒 Cerrando GeoPackage...")
            geoPackage?.close()
            geoPackage = null
            Log.d(TAG, "✅ GeoPackage cerrado y referencia limpiada")
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        closeGeoPackage()
    }

    companion object {
        private const val TAG = "TerrainfoApp"
    }
}
