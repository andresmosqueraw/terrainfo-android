package com.coplanin.terrainfo.ui.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coplanin.terrainfo.data.local.dao.CommonDataDao
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    dao: CommonDataDao
) : ViewModel() {

    /** Lista reactiva de puntos a pintar en el mapa */
    val points: StateFlow<List<MapPoint>> =
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
            )
}

data class MapPoint(val id: Int, val title: String, val latLng: LatLng)