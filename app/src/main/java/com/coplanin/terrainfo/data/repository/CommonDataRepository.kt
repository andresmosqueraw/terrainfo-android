package com.coplanin.terrainfo.data.repository

import android.util.Log
import com.coplanin.terrainfo.data.local.dao.CommonDataDao
import com.coplanin.terrainfo.data.local.entity.CommonDataEntity
import javax.inject.Inject

class CommonDataRepository @Inject constructor(
    private val service: CommonDataService,
    private val dao: CommonDataDao
) {
    private val TAG = "CommonDataRepository"

    suspend fun sync(username: String, token: String) {
        Log.d(TAG, "Starting sync process for user: $username")
        Log.d(TAG, "Using token: ${token.take(10)}...") // Only log first 10 chars for security
        
        try {
            Log.d(TAG, "Requesting common data from backend")
            val list = service.getCommonData(username, "Token $token")
            Log.d(TAG, "Received ${list.size} items from backend")
            
            Log.d(TAG, "Converting and storing data in local database")
            val entities = list.map { dto ->
                CommonDataEntity(
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
            
            Log.d(TAG, "Inserting ${entities.size} records into database")
            dao.insertAll(entities)
            Log.d(TAG, "Sync process completed successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Error during sync process", e)
            throw e
        }
    }
}