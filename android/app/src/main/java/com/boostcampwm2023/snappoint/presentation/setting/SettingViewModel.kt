package com.boostcampwm2023.snappoint.presentation.setting

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcampwm2023.snappoint.data.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : ViewModel() {

    fun tryLogout() {
        loginRepository.getLogout()
            .onEach {
                Log.d("LOG", "LOGOUT: $it")
            }
            .catch {
                Log.d("LOG", "CATCH: ${it}")
                Log.d("LOG", "CATCH: ${it.message}")
            }
            .launchIn(viewModelScope)
    }
}