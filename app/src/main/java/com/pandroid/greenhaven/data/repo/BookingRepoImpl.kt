package com.pandroid.greenhaven.data.repo

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.pandroid.greenhaven.data.resource.Result
import com.pandroid.greenhaven.domain.model.BookingModel
import com.pandroid.greenhaven.domain.repo.BookingRepo
import com.pandroid.greenhaven.utils.NodeRef
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class BookingRepoImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase,
    private val firebaseAuth: FirebaseAuth
) : BookingRepo {

    override suspend fun booking(bookingModel: BookingModel): Result<Unit> {
        return try {
            // Generate booking ID
            val reference =
                firebaseDatabase.getReference(NodeRef.BOOKING_REF).child(bookingModel.userId)
            val bookingId = firebaseDatabase.reference.push().key
                ?: throw Exception("Failed to generate booking ID")

            // Format current time
            val currentDateTime = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
            val formattedDateTime = currentDateTime.format(formatter)

            // Store booking data
            reference.child(bookingId)
                .setValue(bookingModel.copy(bookingId = bookingId, time = formattedDateTime))
                .await()

            // Return success state
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    override suspend fun loadBooking(): Result<List<BookingModel>> {
        return try {
            // Ensure the user is authenticated before proceeding
            val userId = firebaseAuth.currentUser?.uid
                ?: return Result.Failure(Exception("Plz Login to see order's"))

            // Get reference to the bookings of the current user
            val reference = firebaseDatabase.getReference(NodeRef.BOOKING_REF).child(userId)

            // Fetch the data snapshot asynchronously
            val snapshot = reference.get().await()

            // Map the snapshot to a list of BookingModel objects
            val bookingList = snapshot.children.mapNotNull { dataSnapshot ->
                dataSnapshot.getValue(BookingModel::class.java)
            }

            // Return the booking list (or empty list) wrapped in a success state
            Result.Success(bookingList.ifEmpty { emptyList() })
        } catch (e: Exception) {
            // Catch and return error state
            Result.Failure(e)
        }
    }

}

