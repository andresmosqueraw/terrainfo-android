package com.coplanin.terrainfo.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.coplanin.terrainfo.data.local.entity.CommonDataEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CommonDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<CommonDataEntity>)

    @Query("SELECT * FROM commondata")
    fun observeAll(): Flow<List<CommonDataEntity>>
}