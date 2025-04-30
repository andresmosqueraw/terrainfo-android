package com.coplanin.terrainfo.data.repository

import retrofit2.http.Query
import com.coplanin.terrainfo.data.model.CommonDataDTO
import retrofit2.http.GET
import retrofit2.http.Header

interface CommonDataService {
    @GET("commondata")
    suspend fun getCommonData(
        @Query("username") username: String,
        @Header("Authorization") auth: String
    ): List<CommonDataDTO>
}
