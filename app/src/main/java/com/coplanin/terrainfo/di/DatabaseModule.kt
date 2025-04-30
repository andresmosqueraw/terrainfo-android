package com.coplanin.terrainfo.di

import android.content.Context
import androidx.room.Room
import com.coplanin.terrainfo.data.local.TerrainfoDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides @Singleton
    fun provideDb(@ApplicationContext ctx: Context): TerrainfoDb =
        Room.databaseBuilder(ctx, TerrainfoDb::class.java, "terrainfo.db").build()

    @Provides fun userDao(db: TerrainfoDb) = db.userDao()
    @Provides
    fun commonDataDao(db: TerrainfoDb) = db.commonDataDao()
}