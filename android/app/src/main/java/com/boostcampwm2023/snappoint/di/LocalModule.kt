package com.boostcampwm2023.snappoint.di

import android.content.Context
import androidx.room.Room
import com.boostcampwm2023.snappoint.data.local.LocalPostDatabase
import com.boostcampwm2023.snappoint.data.local.dao.LocalPostDao
import com.boostcampwm2023.snappoint.data.local.datasource.LocalPostDataSource
import com.boostcampwm2023.snappoint.data.repository.LocalPostRepository
import com.boostcampwm2023.snappoint.data.repository.LocalPostRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    fun provideLocalPostDatabase(@ApplicationContext context: Context): LocalPostDatabase =
        Room.databaseBuilder(
            context,
            LocalPostDatabase::class.java,
            "database01",
        ).build()

    @Provides
    @Singleton
    fun provideLocalPostDao(localPostDatabase: LocalPostDatabase): LocalPostDao =
        localPostDatabase.localPostDao()

    @Provides
    @Singleton
    fun provideLocalPostDataSource(localPostDao: LocalPostDao): LocalPostDataSource =
        LocalPostDataSource(localPostDao)

    @Provides
    @Singleton
    fun provideLocalPostRepository(localPostDataSource: LocalPostDataSource): LocalPostRepository =
        LocalPostRepositoryImpl(localPostDataSource)
}