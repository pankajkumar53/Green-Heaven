package com.pandroid.greenhaven.data.di

import com.pandroid.greenhaven.data.repo.AuthRepoImpl
import com.pandroid.greenhaven.domain.repo.AuthRepo
import com.pandroid.greenhaven.domain.usecase.AuthUseCase
import com.pandroid.greenhaven.presentation.screens.auth.AuthViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val authModule = module {

    // Reel instance
    single<AuthRepo> { AuthRepoImpl(get(), get()) }
    single { AuthUseCase(get()) }

    viewModel<AuthViewModel> { AuthViewModel(get(), get()) }

}