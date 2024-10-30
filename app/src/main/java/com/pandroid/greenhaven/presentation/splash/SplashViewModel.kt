package com.pandroid.greenhaven.presentation.splash

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pandroid.greenhaven.domain.usecase.UserPrefUseCase
import com.pandroid.greenhaven.presentation.navigation.Routes
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashViewModel @Inject constructor(private val userPrefUseCase: UserPrefUseCase) :
    ViewModel() {

    // Declare _startDestination at the class level
    private val _startDestination: MutableState<String> = mutableStateOf(Routes.Splash.route)
    val startDestination: State<String> = _startDestination

    // Initialize and set the start destination in the init block
    init {
        viewModelScope.launch {
            val isFirstTime = userPrefUseCase.isFirstTime().first()
            _startDestination.value =
                if (isFirstTime) Routes.GetStarted.route else Routes.Home.route
        }
    }

    // Function to be called after onboarding is completed
    fun completeOnboarding() {
        viewModelScope.launch {
            userPrefUseCase.setFirstTime(false)
        }
    }
}




