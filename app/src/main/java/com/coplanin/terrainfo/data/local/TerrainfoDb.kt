package com.coplanin.terrainfo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.coplanin.terrainfo.data.local.dao.CommonDataDao
import com.coplanin.terrainfo.data.local.dao.UserDao
import com.coplanin.terrainfo.data.local.entity.CommonDataEntity
import com.coplanin.terrainfo.data.local.entity.UserEntity

@Database(
    entities = [UserEntity::class, CommonDataEntity::class],
    version = 1,
    exportSchema = false
)
abstract class TerrainfoDb : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun commonDataDao(): CommonDataDao
}