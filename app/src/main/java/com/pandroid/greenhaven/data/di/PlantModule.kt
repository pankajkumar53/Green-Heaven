package com.pandroid.greenhaven.data.di

import com.pandroid.greenhaven.data.repo.PlantRepoImpl
import com.pandroid.greenhaven.domain.repo.PlantRepo
import com.pandroid.greenhaven.domain.usecase.PlantUseCase
import com.pandroid.greenhaven.presentation.screens.createPlant.PlantViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val plantModule = module {

    // Login instance
    single<PlantRepo> { PlantRepoImpl(get(), get()) }
    single<PlantUseCase> { PlantUseCase(get()) }

    // ViewModel
    viewModel<PlantViewModel> { PlantViewModel(get()) }


}