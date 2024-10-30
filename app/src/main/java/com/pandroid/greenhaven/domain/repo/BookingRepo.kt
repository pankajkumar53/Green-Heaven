package com.pandroid.greenhaven.domain.repo

import com.pandroid.greenhaven.data.resource.Result
import com.pandroid.greenhaven.data.resource.State
import com.pandroid.greenhaven.domain.model.BookingModel

interface BookingRepo {
    suspend fun booking(bookingModel: BookingModel): Result<Unit>
    suspend fun loadBooking(): Result<List<BookingModel>>
}