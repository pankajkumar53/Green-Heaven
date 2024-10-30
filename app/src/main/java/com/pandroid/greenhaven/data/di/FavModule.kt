package com.pandroid.greenhaven.data.di

import com.pandroid.greenhaven.data.repo.FavRepoImpl
import com.pandroid.greenhaven.domain.repo.FavRepo
import com.pandroid.greenhaven.domain.usecase.FavUseCase
import com.pandroid.greenhaven.presentation.screens.fav.FavViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val favModule = module {

    // Fav instance
    single<FavRepo> { FavRepoImpl(get(), get()) }
    single { FavUseCase(get()) }

    viewModel<FavViewModel> { FavViewModel(get()) }


}