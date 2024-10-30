package com.pandroid.greenhaven.domain.usecase

import com.pandroid.greenhaven.data.resource.Result
import com.pandroid.greenhaven.data.resource.State
import com.pandroid.greenhaven.domain.model.BookingModel
import com.pandroid.greenhaven.domain.repo.BookingRepo
import javax.inject.Inject

class BookingUseCase @Inject constructor(
    private val bookingRepo: BookingRepo
) : BookingRepo {

    override suspend fun booking(bookingModel: BookingModel): Result<Unit> {
        return bookingRepo.booking(bookingModel)
    }

    override suspend fun loadBooking(): Result<List<BookingModel>> {
        return bookingRepo.loadBooking()
    }

}
