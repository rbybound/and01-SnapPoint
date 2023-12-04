package com.boostcampwm2023.snappoint.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.boostcampwm2023.snappoint.data.local.converter.PostConverter
import com.boostcampwm2023.snappoint.data.local.dao.LocalPostDao
import com.boostcampwm2023.snappoint.data.local.entity.SerializedPost

@Database(entities = [SerializedPost::class], version = 1)
@TypeConverters(PostConverter::class)
abstract class LocalPostDatabase: RoomDatabase() {
    abstract fun localPostDao(): LocalPostDao
}