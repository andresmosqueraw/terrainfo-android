package com.coplanin.terrainfo.ui.map

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coplanin.terrainfo.data.local.dao.CommonDataDao
import com.coplanin.terrainfo.data.local.entity.CommonDataEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    dao: CommonDataDao,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _isAddingPoint = MutableStateFlow(false)

    val isAddingPoint: StateFlow<Boolean> = _isAddingPoint
    
    // Add debouncing for zoom updates
    private var zoomUpdateJob: Job? = null

    fun toggleAddPointMode() {
        _isAddingPoint.value = !_isAddingPoint.value
    }

    /** Elementos completos para la hoja inferior  */
    val visits: StateFlow<List<CommonDataEntity>> =
        dao.observeAll()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    
    override fun onCleared() {
        super.onCleared()
        zoomUpdateJob?.cancel()
    }
}