package com.coplanin.terrainfo.ui.map

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coplanin.terrainfo.TerrainfoApp
import com.coplanin.terrainfo.data.local.dao.CommonDataDao
import com.coplanin.terrainfo.data.local.entity.CommonDataEntity
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import mil.nga.geopackage.GeoPackage
import mil.nga.proj.ProjectionFactory
import mil.nga.proj.ProjectionTransform
import mil.nga.sf.GeometryType
import org.locationtech.proj4j.ProjCoordinate
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    dao: CommonDataDao,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _points = MutableStateFlow<List<MapPoint>>(emptyList())
    val points: StateFlow<List<MapPoint>> = _points

    private val _isAddingPoint = MutableStateFlow(false)
    val isAddingPoint: StateFlow<Boolean> = _isAddingPoint

    fun toggleAddPointMode() {
        _isAddingPoint.value = !_isAddingPoint.value
    }

    fun addPoint(latLng: LatLng) {
        val newPoint = MapPoint(
            id = (_points.value.size + 1).toLong(),
            title = "Nuevo punto ${_points.value.size + 1}",
            latLng = latLng
        )
        _points.value += newPoint
        _isAddingPoint.value = false
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val app = context.applicationContext as TerrainfoApp
            val gpkg = app.getGeoPackage()
            if (gpkg != null) {
                loadGpkgPolygons(gpkg)
                loadGpkgPoints(gpkg)
            }
        }
    }

    private fun loadGpkgPoints(gpkg: GeoPackage) = viewModelScope.launch(Dispatchers.IO) {
        Log.d("PointDebug", "→ Iniciando carga de puntos (límite: 10)")
        val dao = gpkg.getFeatureDao("ilc_predio")
        Log.d("PointDebug", "📦 Tabla ilc_predio abierta con ${dao.count()} filas")

        val transform = ProjectionTransform(
            ProjectionFactory.getProjection(9377),
            ProjectionFactory.getProjection(4326)
        )

        val out = mutableListOf<MapPoint>()
        val maxPoints = 10 // Límite de puntos a mostrar

        dao.queryForAll().use { cursor ->
            var count = 0
            while (cursor.moveToNext() && out.size < maxPoints) {
                val row = cursor.row
                val geom = row.geometry?.geometry

                if (geom == null) {
                    Log.d("PointDebug", "⚠️ Fila sin geometría. Se omite.")
                    continue
                }

                if (geom.geometryType == GeometryType.POINT) {
                    val src = geom as mil.nga.sf.Point
                    val dstCoord = transform.transform(ProjCoordinate(src.x, src.y))

                    val id = try {
                        row.getValue("T_Id")?.toString()?.toLong() ?: -1L
                    } catch (e: NumberFormatException) {
                        Log.w("PointDebug", "⚠️ No se pudo parsear T_Id: ${row.getValue("T_Id")}, usando -1")
                        -1L
                    }
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
            Log.d("PointDebug", "✅ Total puntos cargados: $count (máximo permitido: $maxPoints)")
        }

        _points.emit(out)
    }

    private val _polygonPoints = MutableStateFlow<List<List<LatLng>>>(emptyList())
    val polygonPoints: StateFlow<List<List<LatLng>>> = _polygonPoints

    private fun loadGpkgPolygons(gpkg: GeoPackage) = viewModelScope.launch(Dispatchers.IO) {
        Log.d("PolygonDebug", "→ Iniciando carga de polígonos (límite: 10)")
        val tables = gpkg.featureTables
        Log.d("PolygonDebug", "📂 Tablas disponibles: $tables")

        if (!tables.contains("cr_terreno")) {
            Log.d("PolygonDebug", "❌ La tabla 'cr_terreno' NO existe.")
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
        val maxPolygons = 10 // Límite de polígonos a mostrar

        dao.queryForAll().use { cursor ->
            var index = 0
            while (cursor.moveToNext() && polygons.size < maxPolygons) {
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
                        if (polygons.size < maxPolygons) {
                            val polygon = geometry as mil.nga.sf.Polygon
                            val ring = polygon.rings.firstOrNull()
                            if (ring == null) {
                                Log.d("PolygonDebug", "⚠️ POLYGON sin anillo exterior")
                                continue
                            }
                            val latLngs = ring.points.map {
                                val dst = transform.transform(ProjCoordinate(it.x, it.y))
                                LatLng(dst.y, dst.x)
                            }
                            polygons.add(latLngs)
                            Log.d("PolygonDebug", "✔ POLYGON agregado con ${latLngs.size} puntos")
                        }
                    }

                    GeometryType.MULTIPOLYGON -> {
                        val multiPolygon = geometry as mil.nga.sf.MultiPolygon
                        multiPolygon.polygons.forEachIndexed { i, poly ->
                            if (polygons.size >= maxPolygons) return@forEachIndexed
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
        Log.d("PolygonDebug", "✅ Total de polígonos emitidos: ${polygons.size} (máximo permitido: $maxPolygons)")
    }

    /** Elementos completos para la hoja inferior  */
    val visits: StateFlow<List<CommonDataEntity>> =
        dao.observeAll()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}

data class MapPoint(val id: Long, val title: String, val latLng: LatLng)
// data class VisitItem(val idSearch: String, val address: String)