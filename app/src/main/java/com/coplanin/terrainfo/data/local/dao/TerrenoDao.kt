package com.coplanin.terrainfo.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.coplanin.terrainfo.data.local.entity.TerrainEntity

@Dao
interface TerrenoDao {
    @Query("SELECT * FROM terrainentity WHERE idOperacionPredio = :id LIMIT 1")
    suspend fun getByOperacion(id: String): TerrainEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(terreno: TerrainEntity)

    @Update
    suspend fun update(terreno: TerrainEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(terrenos: List<TerrainEntity>)
}
