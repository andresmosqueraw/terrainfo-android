package com.coplanin.terrainfo.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.coplanin.terrainfo.data.local.entity.PredioEntity

@Dao
interface PredioDao {
    @Query("SELECT COUNT(*) FROM predioentity")
    suspend fun getCount(): Int

    @Query("SELECT * FROM predioentity WHERE numeroPredial = :id LIMIT 1")
    suspend fun getByNumeroPredial(id: String): PredioEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(predio: PredioEntity)

    @Update
    suspend fun update(predio: PredioEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(predios: List<PredioEntity>)
}
