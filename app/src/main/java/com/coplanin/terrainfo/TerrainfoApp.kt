package com.coplanin.terrainfo

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp
import mil.nga.geopackage.GeoPackage
import mil.nga.geopackage.GeoPackageFactory
import java.io.File

@HiltAndroidApp
class TerrainfoApp : Application() {
    private var geoPackage: GeoPackage? = null
    private val geoPackageLock = Any()

    fun getGeoPackage(): GeoPackage? {
        synchronized(geoPackageLock) {
            if (geoPackage != null) {
                Log.d(TAG, "📦 GeoPackage ya está abierto, reutilizando instancia")
                return geoPackage
            }

            Log.d(TAG, "🔒 Intentando abrir GeoPackage...")
            val file = File(filesDir, "modelo_col_smart_vc.gpkg")
            Log.d(TAG, "📁 Verificando archivo en ${file.absolutePath}")
            
            if (!file.exists()) {
                Log.e(TAG, "❌ Archivo GeoPackage no encontrado en ${file.absolutePath}")
                return null
            }
            Log.d(TAG, "✅ Archivo GeoPackage encontrado")

            Log.d(TAG, "🔄 Abriendo GeoPackage...")
            geoPackage = GeoPackageFactory.getManager(this)
                .openExternal(file)

            if (geoPackage == null) {
                Log.e(TAG, "❌ No se pudo abrir GeoPackage")
            } else {
                Log.d(TAG, "📦 GeoPackage abierto correctamente")
                Log.d(TAG, "📊 Tablas disponibles: ${geoPackage?.tables?.joinToString()}")
            }
            return geoPackage
        }
    }

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
