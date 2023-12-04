package com.boostcampwm2023.snappoint.data.repository

import com.boostcampwm2023.snappoint.data.local.entity.SerializedPost
import com.boostcampwm2023.snappoint.presentation.model.PostSummaryState
import kotlinx.coroutines.flow.Flow

interface LocalPostRepository {

    fun getLocalPosts(): Flow<List<PostSummaryState>>
    suspend fun insertPosts(postSummaryState: PostSummaryState)
}