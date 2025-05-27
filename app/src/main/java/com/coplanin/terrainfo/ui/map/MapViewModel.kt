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
        val manager = GeoPackageFactory.getManager(context)
        val gpkg = manager.openExternal(File(geoPackagePath)) ?: return@launch

        val dao = gpkg.getFeatureDao("ilc_predio")

        val transform = ProjectionTransform(
            ProjectionFactory.getProjection(9377),
            ProjectionFactory.getProjection(4326)
        )

        val out = mutableListOf<MapPoint>()

        dao.queryForAll().use { cursor ->
            while (cursor.moveToNext()) {
                val row = cursor.row
                val geom = row.geometry?.geometry ?: continue

                if (geom.geometryType == GeometryType.POINT) {
                    val src = geom as mil.nga.sf.Point
                    val dstCoord = transform.transform(ProjCoordinate(src.x, src.y))

                    out += MapPoint(
                        id = row.getValue("T_Id")?.toString()?.toInt() ?: 0,
                        title = row.getValue("id_operacion")?.toString() ?: "",
                        latLng = LatLng(dstCoord.y, dstCoord.x)
                    )
                }
            }
        }

        _points.emit(out)
        gpkg.close()
    }

    private val _polygonPoints = MutableStateFlow<List<List<LatLng>>>(emptyList())
    val polygonPoints: StateFlow<List<List<LatLng>>> = _polygonPoints

    private fun loadGpkgPolygons(geoPackagePath: String) = viewModelScope.launch(Dispatchers.IO) {
        val manager = GeoPackageFactory.getManager(context)
        val gpkg = manager.openExternal(File(geoPackagePath)) ?: return@launch

        val tables = gpkg.featureTables
        if (!tables.contains("cr_terreno")) {
            Log.d("PolygonDebug", "❌ La tabla 'cr_terreno' NO existe.")
            gpkg.close()
            return@launch
        }

        Log.d("PolygonDebug", "✅ La tabla 'cr_terreno' SÍ existe.")
        val dao = gpkg.getFeatureDao("cr_terreno")
        val columns = dao.columnNames
        Log.d("PolygonDebug", "Columnas:"); columns.forEach { Log.d("PolygonDebug", "- $it") }

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
                Log.d("PolygonDebug", "Fila #$index")
                columns.forEach { col -> Log.d("PolygonDebug", "$col = ${row.getValue(col)}") }

                val geometry = row.geometry?.geometry ?: continue

                when (geometry.geometryType) {
                    GeometryType.POLYGON -> {
                        val polygon = geometry as mil.nga.sf.Polygon
                        val ring = polygon.rings.firstOrNull() ?: continue
                        val latLngs = ring.points.map {
                            val dst = transform.transform(ProjCoordinate(it.x, it.y))
                            LatLng(dst.y, dst.x)
                        }
                        polygons.add(latLngs)
                    }
                    GeometryType.MULTIPOLYGON -> {
                        val multiPolygon = geometry as mil.nga.sf.MultiPolygon
                        multiPolygon.polygons.forEach { poly ->
                            val ring = poly.rings.firstOrNull() ?: return@forEach
                            val latLngs = ring.points.map {
                                val dst = transform.transform(ProjCoordinate(it.x, it.y))
                                LatLng(dst.y, dst.x)
                            }
                            polygons.add(latLngs)
                        }
                    }
                    else -> Log.d("PolygonDebug", "Tipo no soportado: ${geometry.geometryType}")
                }
            }
        }

        _polygonPoints.emit(polygons)
        Log.d("PolygonDebug", "Total de polígonos emitidos: ${polygons.size}")
        gpkg.close()
    }


    /** Elementos completos para la hoja inferior  */
    val visits: StateFlow<List<CommonDataEntity>> =
        dao.observeAll()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}

data class MapPoint(val id: Int, val title: String, val latLng: LatLng)
// data class VisitItem(val idSearch: String, val address: String)