package com.pandroid.greenhaven.data.repo

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.pandroid.greenhaven.domain.model.PlantModel
import com.pandroid.greenhaven.domain.repo.FavRepo
import com.pandroid.greenhaven.utils.NodeRef
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FavRepoImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase
) : FavRepo {

    // Get the currently logged-in user ID
    private val userId: String
        get() = firebaseAuth.currentUser?.uid ?: throw IllegalStateException("Plz login to see favourites!")

    // Reference to the user's favorite plants node in the database
    private val favRef
        get() = firebaseDatabase.reference.child(NodeRef.FAVORITE_REF).child(userId)

    // Add a plant to the user's favorites
    override suspend fun addFav(plantModel: PlantModel): Result<Unit> {
        return try {
            val plantRef = favRef.child(plantModel.plantId)

            // Save the plant to the favorites
            plantRef.setValue(plantModel).await()

            // Return success state
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Load the user's favorite plants
    override suspend fun loadFav(): Result<List<PlantModel>> {
        return try {
            // Fetch the user's favorite plants
            val snapshot = favRef.get().await()

            // Parse the data snapshot into a list of PlantModel objects
            val favList = mutableListOf<PlantModel>()
            for (childSnapshot in snapshot.children) {
                val plant = childSnapshot.getValue(PlantModel::class.java)
                plant?.let { favList.add(it) }
            }

            // Return success state with the favorite list
            Result.success(favList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Delete a plant from the user's favorites
    override suspend fun deleteFav(plantId: String): Result<Unit> {
        return try {
            val plantRef = favRef.child(plantId)

            // Remove the plant from the favorites
            plantRef.removeValue().await()

            // Return success state
           Result.success(Unit)
        } catch (e: Exception) {
            // Return error state if there's any exception
            Result.failure(e)
        }
    }

}

