package com.boostcampwm2023.snappoint.data.repository

import com.boostcampwm2023.snappoint.data.local.dao.LocalPostDao
import com.boostcampwm2023.snappoint.data.mapper.asPostSummaryState
import com.boostcampwm2023.snappoint.data.mapper.asSerializedPost
import com.boostcampwm2023.snappoint.presentation.model.PostSummaryState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalPostRepositoryImpl @Inject constructor(
    private val localPostDao: LocalPostDao
) : LocalPostRepository {

    override fun getLocalPosts(): Flow<List<PostSummaryState>> {
        return localPostDao.getPosts()
            .map { serializedPosts ->
                serializedPosts.map { serializedPost ->
                    serializedPost.asPostSummaryState()
                }
            }
    }

    override suspend fun insertPosts(postSummaryState: PostSummaryState) {
        localPostDao.insertPost(postSummaryState.asSerializedPost())
    }
}