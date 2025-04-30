package com.coplanin.terrainfo.data.repository

import com.coplanin.terrainfo.data.local.dao.CommonDataDao
import com.coplanin.terrainfo.data.local.entity.CommonDataEntity
import javax.inject.Inject

class CommonDataRepository @Inject constructor(
    private val service: CommonDataService,
    private val dao: CommonDataDao
) {
    suspend fun sync(username: String, token: String) {
        val list = service.getCommonData(username, "Token $token")
        dao.insertAll(list.map { dto ->
            CommonDataEntity(
                id = dto.id,
                activityName = dto.activityName,
                activityCode = dto.activityCode,
                address = dto.address,
                cityCode = dto.cityCode,
                cityDesc = dto.cityDesc,
                captureDate = dto.captureDate,
                captureX = dto.captureX.toString(),
                captureY = dto.captureY.toString(),
                eventUserName = dto.eventUserName,
                createDate = dto.createDate,
                createUserName = dto.createUserName,
                eventDate = dto.eventDate,
                eventX = dto.eventX.toString(),
                eventY = dto.eventY.toString()
            )
        })
    }
}