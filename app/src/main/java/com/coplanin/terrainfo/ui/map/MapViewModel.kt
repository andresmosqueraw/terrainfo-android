package com.coplanin.terrainfo.ui.map

import android.content.Context
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

    init { loadGpkgPoints() }

    private fun loadGpkgPoints() = viewModelScope.launch(Dispatchers.IO) {
        // 1. Copiar & abrir el GeoPackage
        val file   = copyAssetToFile(context, "modelo_col_smart.gpkg")
        val mgr    = GeoPackageFactory.getManager(context)               // :contentReference[oaicite:3]{index=3}
        if (!mgr.exists("modelo_col_smart")) mgr.importGeoPackage(file, true)
        val gpkg   = mgr.open("modelo_col_smart") ?: return@launch
        val dao    = gpkg.getFeatureDao("ilc_predio")                    // :contentReference[oaicite:4]{index=4}

        // 2. Transformador 9377 → 4326
        val transform = ProjectionTransform(
            ProjectionFactory.getProjection(9377),                       // :contentReference[oaicite:5]{index=5}
            ProjectionFactory.getProjection(4326)
        )

        val out = mutableListOf<MapPoint>()

        dao.queryForAll().use { cursor ->
            while (cursor.moveToNext()) {
                val row   = cursor.row
                val geom  = row.geometry?.geometry ?: continue

                if (geom.geometryType == GeometryType.POINT) {
                    val src = geom as mil.nga.sf.Point          // punto en 9377
                    val dst = ProjCoordinate()                  // destino 4326
                    val srcCoord = ProjCoordinate(src.x, src.y)
                    val dstCoord = transform.transform(srcCoord) // ⇢ lon / lat

                    out += MapPoint(
                        id      = row.getValue("T_Id")?.toString()?.toInt() ?: 0,
                        title   = row.getValue("id_operacion")?.toString() ?: "",
                        latLng  = LatLng(dstCoord.y, dstCoord.x) // Mapbox espera lat,lon
                    )
                }
            }
        }

        _points.emit(out)
        gpkg.close()
    }

    /** Elementos completos para la hoja inferior  */
    val visits: StateFlow<List<CommonDataEntity>> =
        dao.observeAll()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}

data class MapPoint(val id: Int, val title: String, val latLng: LatLng)
// data class VisitItem(val idSearch: String, val address: String)