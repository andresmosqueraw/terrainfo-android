package com.coplanin.terrainfo.ui.map

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coplanin.terrainfo.data.local.dao.CommonDataDao
import com.coplanin.terrainfo.data.local.entity.CommonDataEntity
import com.coplanin.terrainfo.util.copyAssetToFile
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import mil.nga.geopackage.GeoPackageFactory
import mil.nga.proj.ProjectionFactory
import mil.nga.proj.ProjectionTransform
import mil.nga.sf.GeometryType
import org.locationtech.proj4j.ProjCoordinate
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    dao: CommonDataDao,
    @ApplicationContext private val context: Context
) : ViewModel() {

    /** Lista reactiva de puntos a pintar en el mapa */
    /*val points: StateFlow<List<MapPoint>> =
        dao.observePoints()
            .map { list ->
                list.map { p ->
                    MapPoint(
                        id = p.id,
                        title = p.activityName,
                        //  ⬇️  LAT = eventX ,  LNG = eventY
                        latLng = LatLng(p.eventX, p.eventY)
                    )
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )*/

    private val _points = MutableStateFlow<List<MapPoint>>(emptyList())
    val points: StateFlow<List<MapPoint>> = _points

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val file = copyAssetToFile(context, "modelo_col_smart.gpkg")
            Log.d("GeoPkgInit", "GeoPackage copiado a ruta: ${file.absolutePath}")

            loadGpkgPolygons(file.absolutePath)
            loadGpkgPoints(file.absolutePath)
        }
    }


    private fun loadGpkgPoints(geoPackagePath: String) = viewModelScope.launch(Dispatchers.IO) {
        Log.d("PointDebug", "→ Iniciando carga de puntos desde: $geoPackagePath")
        val manager = GeoPackageFactory.getManager(context)
        val gpkg = manager.openExternal(File(geoPackagePath)) ?: run {
            Log.e("PointDebug", "❌ No se pudo abrir el GeoPackage en $geoPackagePath")
            return@launch
        }

        Log.d("PointDebug", "✅ GeoPackage abierto correctamente")
        val dao = gpkg.getFeatureDao("ilc_predio")
        Log.d("PointDebug", "📦 Tabla ilc_predio abierta con ${dao.count()} filas")

        val transform = ProjectionTransform(
            ProjectionFactory.getProjection(9377),
            ProjectionFactory.getProjection(4326)
        )

        val out = mutableListOf<MapPoint>()

        dao.queryForAll().use { cursor ->
            var count = 0
            while (cursor.moveToNext()) {
                val row = cursor.row
                val geom = row.geometry?.geometry

                if (geom == null) {
                    Log.d("PointDebug", "⚠️ Fila sin geometría. Se omite.")
                    continue
                }

                if (geom.geometryType == GeometryType.POINT) {
                    val src = geom as mil.nga.sf.Point
                    val dstCoord = transform.transform(ProjCoordinate(src.x, src.y))

                    val id = row.getValue("T_Id")?.toString()?.toInt() ?: -1
                    val title = row.getValue("id_operacion")?.toString() ?: "Sin título"

                    val point = MapPoint(
                        id = id,
                        title = title,
                        latLng = LatLng(dstCoord.y, dstCoord.x)
                    )

                    Log.d("PointDebug", "✔ Punto agregado: $point")
                    out += point
                    count++
                } else {
                    Log.d("PointDebug", "⛔ Tipo de geometría ignorado: ${geom.geometryType}")
                }
            }
            Log.d("PointDebug", "✅ Total puntos cargados: $count")
        }

        _points.emit(out)
        gpkg.close()
        Log.d("PointDebug", "📦 GeoPackage cerrado tras procesar puntos")
    }

    private val _polygonPoints = MutableStateFlow<List<List<LatLng>>>(emptyList())
    val polygonPoints: StateFlow<List<List<LatLng>>> = _polygonPoints

    private fun loadGpkgPolygons(geoPackagePath: String) = viewModelScope.launch(Dispatchers.IO) {
        Log.d("PolygonDebug", "→ Iniciando carga de polígonos desde: $geoPackagePath")
        val manager = GeoPackageFactory.getManager(context)
        val gpkg = manager.openExternal(File(geoPackagePath)) ?: run {
            Log.e("PolygonDebug", "❌ No se pudo abrir el GeoPackage en $geoPackagePath")
            return@launch
        }

        val tables = gpkg.featureTables
        Log.d("PolygonDebug", "📂 Tablas disponibles: $tables")

        if (!tables.contains("cr_terreno")) {
            Log.d("PolygonDebug", "❌ La tabla 'cr_terreno' NO existe.")
            gpkg.close()
            return@launch
        }

        Log.d("PolygonDebug", "✅ Tabla 'cr_terreno' encontrada")
        val dao = gpkg.getFeatureDao("cr_terreno")
        val columns = dao.columnNames
        Log.d("PolygonDebug", "📑 Columnas de 'cr_terreno':"); columns.forEach { Log.d("PolygonDebug", "- $it") }

        val transform = ProjectionTransform(
            ProjectionFactory.getProjection(9377),
            ProjectionFactory.getProjection(4326)
        )

        val polygons = mutableListOf<List<LatLng>>()

        dao.queryForAll().use { cursor ->
            var index = 0
            while (cursor.moveToNext()) {
                index++
                val row = cursor.row
                Log.d("PolygonDebug", "📌 Fila #$index")

                columns.forEach { col ->
                    val value = row.getValue(col)
                    Log.d("PolygonDebug", "$col = $value")
                }

                val geometry = row.geometry?.geometry
                if (geometry == null) {
                    Log.d("PolygonDebug", "⚠️ Fila sin geometría. Se omite.")
                    continue
                }

                when (geometry.geometryType) {
                    GeometryType.POLYGON -> {
                        val polygon = geometry as mil.nga.sf.Polygon
                        val ring = polygon.rings.firstOrNull() ?: continue
                        val latLngs = ring.points.map {
                            val dst = transform.transform(ProjCoordinate(it.x, it.y))
                            LatLng(dst.y, dst.x)
                        }
                        polygons.add(latLngs)
                        Log.d("PolygonDebug", "✔ POLYGON agregado con ${latLngs.size} puntos")
                    }

                    GeometryType.MULTIPOLYGON -> {
                        val multiPolygon = geometry as mil.nga.sf.MultiPolygon
                        multiPolygon.polygons.forEachIndexed { i, poly ->
                            val ring = poly.rings.firstOrNull() ?: return@forEachIndexed
                            val latLngs = ring.points.map {
                                val dst = transform.transform(ProjCoordinate(it.x, it.y))
                                LatLng(dst.y, dst.x)
                            }
                            polygons.add(latLngs)
                            Log.d("PolygonDebug", "✔ MULTIPOLYGON[$i] agregado con ${latLngs.size} puntos")
                        }
                    }

                    else -> Log.d("PolygonDebug", "⛔ Tipo de geometría no soportado: ${geometry.geometryType}")
                }
            }
        }

        _polygonPoints.emit(polygons)
        Log.d("PolygonDebug", "✅ Total de polígonos emitidos: ${polygons.size}")
        gpkg.close()
        Log.d("PolygonDebug", "📦 GeoPackage cerrado tras procesar polígonos")
    }


    /** Elementos completos para la hoja inferior  */
    val visits: StateFlow<List<CommonDataEntity>> =
        dao.observeAll()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}

data class MapPoint(val id: Int, val title: String, val latLng: LatLng)
// data class VisitItem(val idSearch: String, val address: String)