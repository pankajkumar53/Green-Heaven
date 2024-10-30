package com.pandroid.greenhaven.data.di

import com.pandroid.greenhaven.data.repo.HomeRepoImpl
import com.pandroid.greenhaven.domain.repo.HomeRepo
import com.pandroid.greenhaven.domain.usecase.HomeUseCase
import com.pandroid.greenhaven.presentation.screens.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val homeModule = module {

    // Home instance
    single<HomeRepo> { HomeRepoImpl(get(), get()) }
    single { HomeUseCase(get()) }

    viewModel<HomeViewModel> { HomeViewModel(get(), get()) }

}