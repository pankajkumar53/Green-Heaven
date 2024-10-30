package com.pandroid.greenhaven.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pandroid.greenhaven.data.resource.State
import com.pandroid.greenhaven.domain.model.UserModel
import com.pandroid.greenhaven.domain.usecase.AuthUseCase
import com.pandroid.greenhaven.domain.usecase.UserPrefUseCase
import com.pandroid.greenhaven.presentation.screens.auth.AuthViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val authViewModel: AuthViewModel,
    private val userPrefUseCase: UserPrefUseCase
) : ViewModel() {

    init {
        viewModelScope.launch {
            val token = userPrefUseCase.loadToken()
            val credentials = userPrefUseCase.loadCredentials()

            if (token != null && token != "") {
                authViewModel.autoLogin()
            } else if (credentials != null && credentials.first != "" && credentials.second != "") {
                authViewModel.autoLogin()
            }
        }

    }




}
