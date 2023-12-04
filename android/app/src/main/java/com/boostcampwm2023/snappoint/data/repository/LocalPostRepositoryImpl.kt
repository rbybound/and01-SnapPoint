package com.boostcampwm2023.snappoint.data.repository

import com.boostcampwm2023.snappoint.data.local.datasource.LocalPostDataSource
import com.boostcampwm2023.snappoint.data.local.entity.SerializedPost
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalPostRepositoryImpl @Inject constructor(
    private val localPostDataSource: LocalPostDataSource
) : LocalPostRepository {

    override fun getLocalPosts(): Flow<List<SerializedPost>> {
        return localPostDataSource.getLocalPosts()
    }

    override suspend fun insertPosts(serializedPost: SerializedPost) {
        localPostDataSource.insertPosts(listOf(serializedPost))
    }
}