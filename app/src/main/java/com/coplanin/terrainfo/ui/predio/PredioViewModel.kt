package com.coplanin.terrainfo.ui.predio

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.coplanin.terrainfo.data.local.entity.PredioEntity
import com.coplanin.terrainfo.data.local.entity.TerrainEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import mil.nga.geopackage.GeoPackage
import mil.nga.geopackage.GeoPackageFactory
import javax.inject.Inject
import java.io.File
import java.io.FileOutputStream

@HiltViewModel
class PredioViewModel @Inject constructor() : ViewModel() {

    private var geoPackage: GeoPackage? = null
    private val geoPackageLock = Any()
    private var context: Context? = null

    init {
        Log.d(TAG, "🚀 Inicializando PredioViewModel")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "🧹 Limpiando PredioViewModel")
        closeGeoPackage()
    }

    /**  🔑 Punto de entrada desde la UI  */
    fun getPredioAndTerrain(
        context: Context,
        idOperacion: String
    ): Flow<Pair<PredioEntity, TerrainEntity?>?> = flow {
        Log.d(TAG, "🚀 Iniciando getPredioAndTerrain(idOperacion=$idOperacion)")
        this@PredioViewModel.context = context

        try {
            // --- 1. Abrir GeoPackage -----------------------------
            Log.d(TAG, "📦 Abriendo GeoPackage...")
            val gpkg = ensureGeoPackageOpen() ?: run {
                Log.e(TAG, "❌ No se pudo abrir GeoPackage")
                emit(null)
                return@flow
            }
            Log.d(TAG, "✅ GeoPackage abierto correctamente")

            // --- 2. Cargar Predio (ilc_predio) -------------------
            Log.d(TAG, "🔍 Buscando predio con id_operacion=$idOperacion")
            val (predio, tId) = loadPredio(gpkg, idOperacion) ?: run {
                Log.w(TAG, "❌ id_operacion=$idOperacion NO existe en ilc_predio")
                emit(null)
                return@flow
            }
            Log.d(TAG, "✅ Predio encontrado: $predio (T_Id=$tId)")

            // --- 3. Cargar Terreno (cr_terreno) ------------------
            Log.d(TAG, "🔍 Buscando terreno con T_Id=$tId")
            val terreno = loadTerrain(gpkg, tId)
            Log.d(TAG, "✅ Terreno encontrado: $terreno")

            // --- 4. Emitir resultado ------------------------------
            Log.d(TAG, "📤 Emitiendo resultado: predio=$predio  terreno=$terreno")
            emit(predio to terreno)

        } catch (e: Exception) {
            Log.e(TAG, "❌ Error en getPredioAndTerrain", e)
            emit(null)
        }
    }

    /* ---------- helpers privados ---------- */
    fun openGeoPackage(context: Context): GeoPackage? {
        this.context = context
        return ensureGeoPackageOpen()
    }

    private fun ensureGeoPackageOpen(): GeoPackage? {
        synchronized(geoPackageLock) {
            if (geoPackage != null) {
                Log.d(TAG, "📦 GeoPackage ya está abierto, reutilizando instancia")
                return geoPackage
            }

            val ctx = context ?: run {
                Log.e(TAG, "❌ Context no disponible para abrir GeoPackage")
                return null
            }

            Log.d(TAG, "🔒 Intentando abrir GeoPackage...")
            val file = copyAssetToFile(ctx, "modelo_col_smart_vc.gpkg")
            Log.d(TAG, "📁 Verificando archivo en ${file.absolutePath}")
            if (!file.exists()) {
                Log.e(TAG, "❌ Archivo GeoPackage no encontrado en ${file.absolutePath}")
                return null
            }
            Log.d(TAG, "✅ Archivo GeoPackage encontrado")

            Log.d(TAG, "🔄 Abriendo GeoPackage...")
            geoPackage = GeoPackageFactory.getManager(ctx)
                .openExternal(File(file.absolutePath))

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

    private fun loadPredio(gpkg: GeoPackage, idOperacion: String): Pair<PredioEntity, Int>? {
        Log.d(TAG, "🔍 Iniciando loadPredio(idOperacion=$idOperacion)")
        val dao = gpkg.getFeatureDao("ilc_predio")
        Log.d(TAG, "📊 Total de registros en ilc_predio: ${dao.count()}")

        dao.queryForAll().use { c ->
            while (c.moveToNext()) {
                val row = c.row
                val idOp = row.getValue("id_operacion")?.toString()
                val tId = row.getValue("T_Id")?.toString()?.toIntOrNull()
                Log.d(TAG, "🔍 Procesando fila ilc_predio id_operacion=$idOp T_Id=$tId")

                if (idOp == idOperacion && tId != null) {
                    val entity = PredioEntity(
                        codigoOrip = row.getValue("codigo_orip")?.toString(),
                        matriculaInmobiliaria = row.getValue("matricula_inmobiliaria")?.toString(),
                        areaCatastralTerreno = row.getValue("area_catastral_terreno")?.toString(),
                        numeroPredialNacional = row.getValue("numero_predial_nacional")?.toString(),
                        tipo = row.getValue("tipo")?.toString(),
                        condicionPredio = row.getValue("condicion_predio")?.toString(),
                        destinacionEconomica = row.getValue("destinacion_economica")?.toString(),
                        areaRegistral = row.getValue("area_registral_m2")?.toString(),
                        tipoReferenciaFmiAntiguo = row.getValue("tipo_referencia_fmi_antiguo")?.toString()
                    )
                    Log.d(TAG, "🎯 Predio encontrado: $entity")
                    return entity to tId
                }
            }
        }
        Log.w(TAG, "⚠️ No se encontró el predio con id_operacion=$idOperacion")
        return null
    }

    private fun loadTerrain(gpkg: GeoPackage, tId: Int): TerrainEntity? {
        Log.d(TAG, "🔍 Iniciando loadTerrain(tId=$tId)")
        val dao = gpkg.getFeatureDao("cr_terreno")
        Log.d(TAG, "📊 Total de registros en cr_terreno: ${dao.count()}")

        dao.queryForAll().use { c ->
            while (c.moveToNext()) {
                val row = c.row
                val fk = row.getValue("ilc_predio")?.toString()?.toIntOrNull()
                Log.d(TAG, "🔍 Procesando fila cr_terreno ilc_predio=$fk")

                if (fk == tId) {
                    val terreno = TerrainEntity(
                        idOperacionPredio = row.getValue("id_operacion_predio")?.toString(),
                        etiqueta = row.getValue("etiqueta")?.toString(),
                        ilcPredio = row.getValue("ilc_predio")?.toString()
                    )
                    Log.d(TAG, "🎯 Terreno encontrado: $terreno")
                    return terreno
                }
            }
        }
        Log.w(TAG, "⚠️ No se encontró terreno para ilc_predio=$tId")
        return null
    }

    fun updatePredio(context: Context, id: String, updatedPredio: PredioEntity) {
        Log.d(TAG, "🔄 Iniciando updatePredio(id=$id)")
        this.context = context
        synchronized(geoPackageLock) {
            val gpkg = ensureGeoPackageOpen() ?: run {
                Log.e(TAG, "❌ No se pudo abrir GeoPackage para actualizar")
                return
            }

            val dao = gpkg.getFeatureDao("ilc_predio") ?: run {
                Log.e(TAG, "❌ DAO para 'ilc_predio' no disponible")
                return
            }
            Log.d(TAG, "📊 Total de registros en ilc_predio: ${dao.count()}")

            var found = false
            dao.queryForAll().use { cursor ->
                while (cursor.moveToNext()) {
                    val row = cursor.row
                    val numPredial = row.getValue("numero_predial_nacional")?.toString()
                    Log.d(TAG, "🔍 Procesando fila con numero_predial_nacional=$numPredial")
                    
                    if (numPredial == id) {
                        Log.d(TAG, "📝 Actualizando campos del predio...")
                        val codigoOrip = updatedPredio.codigoOrip?.take(4) // Truncar a máximo 4 caracteres
                        row.setValue("codigo_orip", codigoOrip)
                        val matriculaLong = updatedPredio.matriculaInmobiliaria?.toLongOrNull()
                        if (matriculaLong != null) {
                            row.setValue("matricula_inmobiliaria", matriculaLong)
                        } else {
                            Log.w(TAG, "⚠️ No se pudo convertir matriculaInmobiliaria a Long: ${updatedPredio.matriculaInmobiliaria}")
                        }
                        val areaDouble = updatedPredio.areaCatastralTerreno?.toDoubleOrNull()
                        if (areaDouble != null) {
                            row.setValue("area_catastral_terreno", areaDouble)
                        } else {
                            Log.w(TAG, "⚠️ No se pudo convertir area_catastral_terreno a Double: ${updatedPredio.areaCatastralTerreno}")
                        }
                        val tipoLong = updatedPredio.tipo?.toLongOrNull()
                        if (tipoLong != null) {
                            row.setValue("tipo", tipoLong)
                        } else {
                            Log.w(TAG, "⚠️ No se pudo convertir tipo a Long: ${updatedPredio.tipo}")
                        }
                        val condicionLong = updatedPredio.condicionPredio?.toLongOrNull()
                        if (condicionLong != null) {
                            row.setValue("condicion_predio", condicionLong)
                        } else {
                            Log.w(TAG, "⚠️ No se pudo convertir condicion_predio a Long: ${updatedPredio.condicionPredio}")
                        }
                        val destinoLong = updatedPredio.destinacionEconomica?.toLongOrNull()
                        if (destinoLong != null) {
                            row.setValue("destinacion_economica", destinoLong)
                        } else {
                            Log.w(TAG, "⚠️ No se pudo convertir destinacion_economica a Long: ${updatedPredio.destinacionEconomica}")
                        }
                        val areaRegistralDouble = updatedPredio.areaRegistral?.toDoubleOrNull()
                        if (areaRegistralDouble != null) {
                            row.setValue("area_registral_m2", areaRegistralDouble)
                        } else {
                            Log.w(TAG, "⚠️ No se pudo convertir area_registral_m2 a Double: ${updatedPredio.areaRegistral}")
                        }
                        val tipoRefLong = updatedPredio.tipoReferenciaFmiAntiguo?.toLongOrNull()
                        if (tipoRefLong != null) {
                            row.setValue("tipo_referencia_fmi_antiguo", tipoRefLong)
                        } else {
                            Log.w(TAG, "⚠️ No se pudo convertir tipo_referencia_fmi_antiguo a Long: ${updatedPredio.tipoReferenciaFmiAntiguo}")
                        }
                        
                        val updated = dao.update(row)
                        Log.d(TAG, "✅ Predio actualizado: $updatedPredio (filas afectadas: $updated)")
                        found = true
                        break
                    }
                }
            }
            if (!found) Log.w(TAG, "⚠️ No se encontró el predio con numero_predial_nacional=$id")
        }
    }

    fun updateTerreno(context: Context, id: String, updatedTerreno: TerrainEntity) {
        Log.d(TAG, "🔄 Iniciando updateTerreno(id=$id)")
        this.context = context
        synchronized(geoPackageLock) {
            val gpkg = ensureGeoPackageOpen() ?: run {
                Log.e(TAG, "❌ No se pudo abrir GeoPackage para actualizar")
                return
            }

            val dao = gpkg.getFeatureDao("cr_terreno") ?: run {
                Log.e(TAG, "❌ DAO para 'cr_terreno' no disponible")
                return
            }
            Log.d(TAG, "📊 Total de registros en cr_terreno: ${dao.count()}")

            var found = false
            dao.queryForAll().use { cursor ->
                while (cursor.moveToNext()) {
                    val row = cursor.row
                    val idOp = row.getValue("id_operacion_predio")?.toString()
                    Log.d(TAG, "🔍 Procesando fila con id_operacion_predio=$idOp")
                    
                    if (idOp == id) {
                        Log.d(TAG, "📝 Actualizando campos del terreno...")
                        row.setValue("etiqueta", updatedTerreno.etiqueta)
                        val ilcPredioLong = updatedTerreno.ilcPredio?.toLongOrNull()
                        if (ilcPredioLong != null) {
                            row.setValue("ilc_predio", ilcPredioLong)
                        } else {
                            Log.w(TAG, "⚠️ No se pudo convertir ilc_predio a Long: ${updatedTerreno.ilcPredio}")
                        }
                        
                        val updated = dao.update(row)
                        Log.d(TAG, "✅ Terreno actualizado: $updatedTerreno (filas afectadas: $updated)")
                        found = true
                        break
                    }
                }
            }
            if (!found) Log.w(TAG, "⚠️ No se encontró el terreno con id_operacion_predio=$id")
        }
    }

    fun saveNewPredio(context: Context, predio: PredioEntity) {
        Log.d(TAG, "🆕 Iniciando saveNewPredio")
        this.context = context
        synchronized(geoPackageLock) {
            val gpkg = ensureGeoPackageOpen() ?: run {
                Log.e(TAG, "❌ No se pudo abrir GeoPackage para guardar")
                return
            }

            val dao = gpkg.getFeatureDao("ilc_predio") ?: run {
                Log.e(TAG, "❌ DAO para 'ilc_predio' no disponible")
                return
            }
            Log.d(TAG, "📊 Total de registros en ilc_predio: ${dao.count()}")

            // Crear nueva fila
            val row = dao.newRow()
            
            // Establecer valores
            val codigoOrip = predio.codigoOrip?.take(4) // Truncar a máximo 4 caracteres
            row.setValue("codigo_orip", codigoOrip)
            val matriculaLong = predio.matriculaInmobiliaria?.toLongOrNull()
            if (matriculaLong != null) {
                row.setValue("matricula_inmobiliaria", matriculaLong)
            } else {
                Log.w(TAG, "⚠️ No se pudo convertir matriculaInmobiliaria a Long: ${predio.matriculaInmobiliaria}")
            }
            val areaDouble = predio.areaCatastralTerreno?.toDoubleOrNull()
            if (areaDouble != null) {
                row.setValue("area_catastral_terreno", areaDouble)
            } else {
                Log.w(TAG, "⚠️ No se pudo convertir area_catastral_terreno a Double: ${predio.areaCatastralTerreno}")
            }
            row.setValue("numero_predial_nacional", predio.numeroPredialNacional)
            val tipoLong = predio.tipo?.toLongOrNull()
            if (tipoLong != null) {
                row.setValue("tipo", tipoLong)
            } else {
                Log.w(TAG, "⚠️ No se pudo convertir tipo a Long: ${predio.tipo}")
            }
            val condicionLong = predio.condicionPredio?.toLongOrNull()
            if (condicionLong != null) {
                row.setValue("condicion_predio", condicionLong)
            } else {
                Log.w(TAG, "⚠️ No se pudo convertir condicion_predio a Long: ${predio.condicionPredio}")
            }
            val destinoLong = predio.destinacionEconomica?.toLongOrNull()
            if (destinoLong != null) {
                row.setValue("destinacion_economica", destinoLong)
            } else {
                Log.w(TAG, "⚠️ No se pudo convertir destinacion_economica a Long: ${predio.destinacionEconomica}")
            }
            val areaRegistralDouble = predio.areaRegistral?.toDoubleOrNull()
            if (areaRegistralDouble != null) {
                row.setValue("area_registral_m2", areaRegistralDouble)
            } else {
                Log.w(TAG, "⚠️ No se pudo convertir area_registral_m2 a Double: ${predio.areaRegistral}")
            }
            val tipoRefLong = predio.tipoReferenciaFmiAntiguo?.toLongOrNull()
            if (tipoRefLong != null) {
                row.setValue("tipo_referencia_fmi_antiguo", tipoRefLong)
            } else {
                Log.w(TAG, "⚠️ No se pudo convertir tipo_referencia_fmi_antiguo a Long: ${predio.tipoReferenciaFmiAntiguo}")
            }

            // Insertar la nueva fila
            val inserted = dao.insert(row)
            Log.d(TAG, "✅ Nuevo predio guardado (filas afectadas: $inserted)")
        }
    }

    companion object { 
        private const val TAG = "PredioVM" 
    }
}

fun copyAssetToFile(context: Context, assetName: String): File {
    val outFile = File(context.filesDir, assetName)
    if (!outFile.exists()) {
        context.assets.open(assetName).use { input ->
            FileOutputStream(outFile).use { output -> input.copyTo(output) }
        }
    }
    return outFile
}