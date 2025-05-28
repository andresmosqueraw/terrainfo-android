package com.coplanin.terrainfo.ui.predio

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.coplanin.terrainfo.data.local.entity.PredioEntity
import com.coplanin.terrainfo.data.local.entity.TerrainEntity
import com.coplanin.terrainfo.util.copyAssetToFile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import mil.nga.geopackage.GeoPackage
import mil.nga.geopackage.GeoPackageFactory
import javax.inject.Inject
import java.io.File

@HiltViewModel
class PredioViewModel @Inject constructor() : ViewModel() {

    /**  🔑 Punto de entrada desde la UI  */
    fun getPredioAndTerrain(
        context: Context,
        idOperacion: String
    ): Flow<Pair<PredioEntity, TerrainEntity?>?> = flow {

        Log.d(TAG, "🚀 getPredioAndTerrain(idOperacion=$idOperacion)")

        // --- 1. Abrir GeoPackage -----------------------------
        val gpkg = openGeoPackage(context) ?: run {
            emit(null);  return@flow
        }

        // --- 2. Cargar Predio (ilc_predio) -------------------
        val (predio, tId) = loadPredio(gpkg, idOperacion) ?: run {
            Log.w(TAG, "❌ id_operacion=$idOperacion NO existe en ilc_predio")
            gpkg.close(); emit(null); return@flow
        }

        // --- 3. Cargar Terreno (cr_terreno) ------------------
        val terreno = loadTerrain(gpkg, tId)

        // --- 4. Cerrar y emitir ------------------------------
        gpkg.close()
        Log.d(TAG, "✅ Emitiendo resultado: predio=$predio  terreno=$terreno")
        emit(predio to terreno)
    }

    /* ---------- helpers privados ---------- */

    private fun openGeoPackage(context: Context): GeoPackage? {
        val file = copyAssetToFile(context, "modelo_col_smart_vc.gpkg")
        Log.d(TAG, "📁 Copia local en ${file.absolutePath}")
        val gpkg = GeoPackageFactory.getManager(context)
            .openExternal(File(file.absolutePath))

        if (gpkg == null) Log.e(TAG, "❌ No se pudo abrir GeoPackage")
        else Log.d(TAG, "📦 GeoPackage abierto OK")
        return gpkg
    }

    /**
     * Devuelve el PredioEntity + su T_Id (clave foránea).
     */
    private fun loadPredio(gpkg: GeoPackage, idOperacion: String):
            Pair<PredioEntity, Int>? {

        val dao = gpkg.getFeatureDao("ilc_predio")
        Log.d(TAG, "🗂 ilc_predio rows=${dao.count()}")

        dao.queryForAll().use { c ->
            while (c.moveToNext()) {
                val row = c.row
                val idOp = row.getValue("id_operacion")?.toString()
                val tId  = row.getValue("T_Id")?.toString()?.toIntOrNull()
                Log.d(TAG, "🔍 fila ilc_predio id_operacion=$idOp  T_Id=$tId")

                if (idOp == idOperacion && tId != null) {
                    val entity = PredioEntity(
                        codigoOrip   = row.getValue("codigo_orip")?.toString(),
                        matricula    = row.getValue("matricula_inmobiliaria")?.toString(),
                        areaTerreno  = row.getValue("area_catastral_terreno")?.toString(),
                        numeroPredial= row.getValue("numero_predial_nacional")?.toString(),
                        tipo         = row.getValue("tipo")?.toString(),
                        condicion    = row.getValue("condicion_predio")?.toString(),
                        destino      = row.getValue("destinacion_economica")?.toString(),
                        areaRegistral= row.getValue("area_registral_m2")?.toString()
                    )
                    Log.d(TAG, "🎯 Predio encontrado: $entity")
                    return entity to tId
                }
            }
        }
        return null
    }

    /**
     * Busca en cr_terreno donde ilc_predio == tId.
     */
    private fun loadTerrain(gpkg: GeoPackage, tId: Int): TerrainEntity? {
        val dao = gpkg.getFeatureDao("cr_terreno")
        Log.d(TAG, "🗂 cr_terreno rows=${dao.count()}  (buscando ilc_predio=$tId)")

        dao.queryForAll().use { c ->
            while (c.moveToNext()) {
                val row = c.row
                val fk  = row.getValue("ilc_predio")?.toString()?.toIntOrNull()
                if (fk == tId) {
                    val terreno = TerrainEntity(
                        idOperacionPredio = row.getValue("id_operacion_predio")?.toString(),
                        etiqueta          = row.getValue("etiqueta")?.toString()
                    )
                    Log.d(TAG, "🎯 Terreno encontrado: $terreno")
                    return terreno
                }
            }
        }
        Log.w(TAG, "⚠️ No hay terreno para ilc_predio=$tId")
        return null
    }

    companion object { private const val TAG = "PredioVM" }
}