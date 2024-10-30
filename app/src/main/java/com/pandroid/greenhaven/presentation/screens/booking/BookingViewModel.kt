package com.pandroid.greenhaven.presentation.screens.booking

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pandroid.greenhaven.data.resource.State
import com.pandroid.greenhaven.data.resource.Result
import com.pandroid.greenhaven.domain.model.BookingModel
import com.pandroid.greenhaven.domain.usecase.BookingUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class BookingViewModel @Inject constructor(private val bookingUseCase: BookingUseCase) :
    ViewModel() {

    private val _bookState = MutableLiveData<State<Any>>()
    val bookState: LiveData<State<Any>> get() = _bookState

    fun uploadBooking(bookingModel: BookingModel) {
        _bookState.value = State.Loading()
        viewModelScope.launch {
            val result = bookingUseCase.booking(bookingModel)
            when (result) {
                is Result.Failure -> {
                    _bookState.value = State.Error(result.exception.localizedMessage)
                }
                is Result.Success -> {
                    _bookState.value = State.Success(result.data)
                }
            }
        }

    }


    fun loadBookings() {
        _bookState.value = State.Loading()
        viewModelScope.launch {
            val result = bookingUseCase.loadBooking()
            when (result) {
                is Result.Failure -> {
                    _bookState.value = State.Error(result.exception.localizedMessage)
                }
                is Result.Success -> {
                    _bookState.value = State.Success(result.data)
                }
            }
        }
    }


}
