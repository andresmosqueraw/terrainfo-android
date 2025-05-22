package com.coplanin.terrainfo.ui.predio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coplanin.terrainfo.data.local.entity.CommonDataEntity
import com.coplanin.terrainfo.data.repository.CommonDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PredioViewModel @Inject constructor(
    private val repo: CommonDataRepository
) : ViewModel() {

    // fun getPredio(id: String): Flow<CommonDataEntity?> = repo.getById(id)

    fun getPredio(id: String): Flow<CommonDataEntity?> = flowOf(
        CommonDataEntity(
            id = 1,
            activityName = "Actividad Test",
            activityCode = "ACT123",
            idSearch = "ID123",
            address = "Calle Falsa 123",
            cityCode = "001",
            cityDesc = "Ciudad Test",
            captureDate = "2023-01-01",
            captureX = "123.456",
            captureY = "789.012",
            captureUserName = "Usuario Test",
            eventUserName = "Evento Test",
            createDate = "2023-01-01",
            createUserName = "Creador Test",
            eventDate = "2023-01-02",
            eventX = 123.456,
            eventY = 789.012,
            lastEditDate = "2023-01-03",
            lastEditUserName = "Editor Test",
            lastEditX = 123.456,
            lastEditY = 789.012
        )
    )
}
