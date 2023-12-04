package com.boostcampwm2023.snappoint.data.local.datasource

import com.boostcampwm2023.snappoint.data.local.dao.LocalPostDao
import com.boostcampwm2023.snappoint.data.local.entity.SerializedPost
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalPostDataSource @Inject constructor(
    private val localPostDao: LocalPostDao
) {

    suspend fun insertPosts(posts: List<SerializedPost>) {
        localPostDao.insertPost(posts = posts.toTypedArray())
    }

    fun getLocalPosts(): Flow<List<SerializedPost>> {
        return localPostDao.getPosts()
    }
}