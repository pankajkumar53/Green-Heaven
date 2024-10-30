package com.pandroid.greenhaven.data.di

import com.pandroid.greenhaven.data.repo.BookingRepoImpl
import com.pandroid.greenhaven.domain.repo.BookingRepo
import com.pandroid.greenhaven.domain.usecase.BookingUseCase
import com.pandroid.greenhaven.presentation.screens.booking.BookingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val bookingModule = module {

    single<BookingRepo> { BookingRepoImpl(get(), get()) }

    single { BookingUseCase(get()) }

    viewModel<BookingViewModel> { BookingViewModel(get()) }


}