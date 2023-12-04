package com.boostcampwm2023.snappoint.data.repository

import com.boostcampwm2023.snappoint.data.local.entity.SerializedPost
import kotlinx.coroutines.flow.Flow

interface LocalPostRepository {

    fun getLocalPosts(): Flow<List<SerializedPost>>
    suspend fun insertPosts(serializedPost: SerializedPost)
}