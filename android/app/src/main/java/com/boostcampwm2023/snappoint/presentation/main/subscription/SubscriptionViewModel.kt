package com.boostcampwm2023.snappoint.presentation.main.subscription

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcampwm2023.snappoint.data.local.converter.PostConverter
import com.boostcampwm2023.snappoint.data.local.entity.LocalPost
import com.boostcampwm2023.snappoint.data.local.entity.SerializedPost
import com.boostcampwm2023.snappoint.data.mapper.asPostSummaryState
import com.boostcampwm2023.snappoint.data.repository.LocalPostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubscriptionViewModel @Inject constructor(
    private val localPostRepository: LocalPostRepository
) : ViewModel() {

    fun insertPost(localPost: LocalPost) {
        viewModelScope.launch(Dispatchers.IO) {
            // TODO insert 삭제, delete 추가
            localPostRepository.insertPosts(SerializedPost(localPost).asPostSummaryState())
        }
    }

    fun getLocalPost() {
        localPostRepository.getLocalPosts()
            .flowOn(Dispatchers.IO)
            .onEach {
                Log.d("LOG", "${it}")
            }.catch {
                Log.d("LOG", "ERROR: ${it.message}")
            }.launchIn(viewModelScope)
    }
}