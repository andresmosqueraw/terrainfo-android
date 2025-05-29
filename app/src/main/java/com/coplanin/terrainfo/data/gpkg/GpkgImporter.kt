package com.coplanin.terrainfo.data.gpkg

import android.content.Context
import android.util.Log
import com.coplanin.terrainfo.data.local.dao.PredioDao
import com.coplanin.terrainfo.data.local.dao.TerrenoDao
import com.coplanin.terrainfo.data.local.entity.PredioEntity
import com.coplanin.terrainfo.data.local.entity.TerrainEntity
import com.coplanin.terrainfo.util.copyAssetToFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mil.nga.geopackage.GeoPackageFactory
import javax.inject.Inject

class GpkgImporter @Inject constructor(
    private val predioDao: PredioDao,
    private val terrenoDao: TerrenoDao
) {
    suspend fun import(context: Context) = withContext(Dispatchers.IO) {
        val file = copyAssetToFile(context, "modelo_col_smart_vc.gpkg")
        val gpkg = GeoPackageFactory.getManager(context).openExternal(file)
        if (gpkg == null) {
            Log.e("GpkgImporter", "❌ No se pudo abrir el GeoPackage")
            return@withContext
        }

        // ------- PREDIOS -------
        val predioDaoGpkg = gpkg.getFeatureDao("ilc_predio")
        val predios = mutableListOf<PredioEntity>()
        val tIdMap = mutableMapOf<String, Int>()

        predioDaoGpkg.queryForAll().use { cursor ->
            while (cursor.moveToNext()) {
                val row = cursor.row
                val idOperacion = row.getValue("id_operacion")?.toString() ?: continue
                val tId = row.getValue("T_Id")?.toString()?.toIntOrNull() ?: continue
                tIdMap[idOperacion] = tId

                predios.add(
                    PredioEntity(
                        codigoOrip    = row.getValue("codigo_orip")?.toString(),
                        matricula     = row.getValue("matricula_inmobiliaria")?.toString(),
                        areaTerreno   = row.getValue("area_catastral_terreno")?.toString(),
                        numeroPredial = idOperacion,
                        tipo          = row.getValue("tipo")?.toString(),
                        condicion     = row.getValue("condicion_predio")?.toString(),
                        destino       = row.getValue("destinacion_economica")?.toString(),
                        areaRegistral = row.getValue("area_registral_m2")?.toString()
                    )
                )
            }
        }
        predioDao.insertAll(predios)
        Log.i("GpkgImporter", "✅ ${predios.size} predios insertados")

        // ------- TERRENOS -------
        val terrenoDaoGpkg = gpkg.getFeatureDao("cr_terreno")
        val terrenos = mutableListOf<TerrainEntity>()

        terrenoDaoGpkg.queryForAll().use { cursor ->
            while (cursor.moveToNext()) {
                val row = cursor.row
                val fk = row.getValue("ilc_predio")?.toString()?.toIntOrNull() ?: continue
                val idOperacion = tIdMap.entries.find { it.value == fk }?.key ?: continue

                terrenos.add(
                    TerrainEntity(
                        idOperacionPredio = idOperacion,
                        etiqueta = row.getValue("etiqueta")?.toString()
                    )
                )
            }
        }
        terrenoDao.insertAll(terrenos)
        Log.i("GpkgImporter", "✅ ${terrenos.size} terrenos insertados")

        gpkg.close()
    }
}