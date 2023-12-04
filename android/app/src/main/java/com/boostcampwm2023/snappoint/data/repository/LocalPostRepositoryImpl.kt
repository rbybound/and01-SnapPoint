package com.boostcampwm2023.snappoint.data.repository

import com.boostcampwm2023.snappoint.data.local.datasource.LocalPostDataSource
import com.boostcampwm2023.snappoint.data.local.entity.SerializedPost
import com.boostcampwm2023.snappoint.data.mapper.asPostSummaryState
import com.boostcampwm2023.snappoint.data.mapper.asSerializedPost
import com.boostcampwm2023.snappoint.presentation.model.PostSummaryState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class LocalPostRepositoryImpl @Inject constructor(
    private val localPostDataSource: LocalPostDataSource
) : LocalPostRepository {

    override fun getLocalPosts(): Flow<List<PostSummaryState>> {
        return localPostDataSource.getLocalPosts()
            .map { serializedPosts ->
                serializedPosts.map { serializedPost ->
                    serializedPost.asPostSummaryState()
                }
            }
    }

    override suspend fun insertPosts(postSummaryState: PostSummaryState) {
        localPostDataSource.insertPosts(listOf(postSummaryState.asSerializedPost()))
    }
}