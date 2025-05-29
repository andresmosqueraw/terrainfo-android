package com.coplanin.terrainfo.data.repository

import com.coplanin.terrainfo.data.local.dao.PredioDao
import com.coplanin.terrainfo.data.local.dao.TerrenoDao
import com.coplanin.terrainfo.data.local.entity.PredioEntity
import com.coplanin.terrainfo.data.local.entity.TerrainEntity
import javax.inject.Inject

class LocalDataRepository @Inject constructor(
    private val predioDao: PredioDao,
    private val terrenoDao: TerrenoDao
) {
    suspend fun getPredio(id: String) = predioDao.getByNumeroPredial(id)
    suspend fun getTerreno(id: String) = terrenoDao.getByOperacion(id)

    suspend fun savePredio(predio: PredioEntity) = predioDao.update(predio)
    suspend fun saveTerreno(terreno: TerrainEntity) = terrenoDao.update(terreno)

    suspend fun getPredioCount(): Int = predioDao.getCount()
}
