package com.coplanin.terrainfo.data.utils

import com.coplanin.terrainfo.data.local.entity.CommonDataEntity
import com.coplanin.terrainfo.data.model.CommonDataDTO
import javax.inject.Inject

class CommonDataMapper @Inject constructor() : Mapper<CommonDataDTO, CommonDataEntity> {
    override fun map(dto: CommonDataDTO): CommonDataEntity {
        return CommonDataEntity(
            id = dto.id,
            activityName = dto.activityName,
            activityCode = dto.activityCode,
            idSearch = dto.idSearch,
            address = dto.address,
            cityCode = dto.cityCode,
            cityDesc = dto.cityDesc,
            captureDate = dto.captureDate,
            captureX = dto.captureX.toString(),
            captureY = dto.captureY.toString(),
            captureUserName = dto.captureUserName ?: "N/A",
            eventUserName = dto.eventUserName,
            createDate = dto.createDate,
            createUserName = dto.createUserName,
            eventDate = dto.eventDate,
            eventX = dto.eventX,
            eventY = dto.eventY,
            lastEditDate = dto.lastEditDate ?: "N/A",
            lastEditUserName = dto.lastEditUserName ?: "N/A",
            lastEditX = dto.lastEditX ?: 0.0,
            lastEditY = dto.lastEditY ?: 0.0
        )
    }
} 