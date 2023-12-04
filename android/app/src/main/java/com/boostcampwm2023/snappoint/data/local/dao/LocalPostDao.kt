package com.boostcampwm2023.snappoint.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.boostcampwm2023.snappoint.data.local.entity.SerializedPost
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalPostDao {

    @Query("SELECT * FROM postTable")
    fun getPosts() : Flow<List<SerializedPost>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPost(vararg posts: SerializedPost)
}