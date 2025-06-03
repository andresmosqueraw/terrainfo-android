package com.coplanin.terrainfo.data.repository

import com.coplanin.terrainfo.data.local.dao.CommonDataDao
import com.coplanin.terrainfo.data.utils.AuthUtils
import com.coplanin.terrainfo.data.utils.CommonDataMapper
import javax.inject.Inject

class CommonDataRepository @Inject constructor(
    private val service: CommonDataService,
    private val dao: CommonDataDao,
    private val mapper: CommonDataMapper
) {
    suspend fun sync(username: String, token: String) {
        val list = service.getCommonData(username, AuthUtils.formatToken(token))
        dao.insertAll(list.map { dto -> mapper.map(dto) })
    }
}