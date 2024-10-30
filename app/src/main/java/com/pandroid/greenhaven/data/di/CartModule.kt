package com.pandroid.greenhaven.data.di

import com.pandroid.greenhaven.data.repo.CartRepoImpl
import com.pandroid.greenhaven.domain.repo.CartRepo
import com.pandroid.greenhaven.domain.usecase.CartUseCase
import com.pandroid.greenhaven.presentation.screens.cart.CartViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val cartModule = module {

    // Fav instance
    single<CartRepo> { CartRepoImpl(get(), get()) }
    single { CartUseCase(get()) }

    viewModel<CartViewModel> { CartViewModel(get()) }

}