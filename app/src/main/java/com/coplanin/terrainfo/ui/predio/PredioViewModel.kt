package com.coplanin.terrainfo.ui.predio

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.coplanin.terrainfo.data.local.entity.PredioEntity
import com.coplanin.terrainfo.util.copyAssetToFile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import mil.nga.geopackage.GeoPackageFactory
import java.io.File
import javax.inject.Inject

@HiltViewModel
class PredioViewModel @Inject constructor() : ViewModel() {

    fun getPredioFromGpkg(context: Context, idOperacion: String): Flow<PredioEntity?> = flow {
        Log.d("PredioViewModel", "📦 Iniciando búsqueda en GeoPackage para id_operacion = $idOperacion")

        val file = copyAssetToFile(context, "modelo_col_smart_vc.gpkg")
        Log.d("PredioViewModel", "📁 GeoPackage copiado a: ${file.absolutePath}")

        val manager = GeoPackageFactory.getManager(context)
        val gpkg = manager.openExternal(File(file.absolutePath)) ?: run {
            Log.e("PredioViewModel", "❌ No se pudo abrir el GeoPackage")
            emit(null)
            return@flow
        }

        Log.d("PredioViewModel", "✅ GeoPackage abierto correctamente")

        val dao = gpkg.getFeatureDao("ilc_predio")
        Log.d("PredioViewModel", "📋 Tabla 'ilc_predio' tiene ${dao.count()} registros")

        val cursor = dao.queryForAll()
        var result: PredioEntity? = null
        var encontrados = 0

        while (cursor.moveToNext()) {
            val row = cursor.row
            val value = row.getValue("id_operacion")?.toString()

            Log.d("PredioViewModel", "🔍 Revisando fila con id_operacion=$value")

            if (value == idOperacion) {
                Log.d("PredioViewModel", "🎯 Coincidencia encontrada para id_operacion=$value")

                result = PredioEntity(
                    codigoOrip = row.getValue("codigo_orip")?.toString(),
                    matricula = row.getValue("matricula_inmobiliaria")?.toString(),
                    areaTerreno = row.getValue("area_catastral_terreno")?.toString(),
                    numeroPredial = row.getValue("numero_predial_nacional")?.toString(),
                    tipo = row.getValue("tipo")?.toString(),
                    condicion = row.getValue("condicion_predio")?.toString(),
                    destino = row.getValue("destinacion_economica")?.toString(),
                    areaRegistral = row.getValue("area_registral_m2")?.toString()
                )

                Log.d("PredioViewModel", "📄 Predio cargado: $result")
                encontrados++
                break
            }
        }

        cursor.close()
        gpkg.close()

        Log.d("PredioViewModel", "🔚 Fin de búsqueda. Total encontrados: $encontrados")
        emit(result)
    }
}
