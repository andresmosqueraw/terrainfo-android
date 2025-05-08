package com.coplanin.terrainfo.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.coplanin.terrainfo.data.local.entity.CommonDataEntity
import com.coplanin.terrainfo.data.local.entity.PointEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CommonDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<CommonDataEntity>)

    @Query("SELECT * FROM commondata")
    fun observeAll(): Flow<List<CommonDataEntity>>

    @Query("""
        SELECT id, activityName, eventX, eventY 
        FROM commondata
        WHERE eventX IS NOT NULL AND eventY IS NOT NULL
    """)
        fun observePoints(): Flow<List<PointEntity>>

}