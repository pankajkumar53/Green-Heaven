package com.pandroid.greenhaven.data.repo

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.pandroid.greenhaven.domain.model.PlantModel
import com.pandroid.greenhaven.domain.repo.CartRepo
import com.pandroid.greenhaven.utils.NodeRef
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CartRepoImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase
) : CartRepo {

    // Get the currently logged-in user ID
    private val userId: String
        get() = firebaseAuth.currentUser?.uid ?: throw IllegalStateException("Plz login to see cart!")

    // Reference to the user's favorite plants node in the database
    private val cartRef
        get() = firebaseDatabase.reference.child(NodeRef.CART_REF).child(userId)

    private val ordersRef
        get() = firebaseDatabase.reference.child(NodeRef.BOOKING_REF).child(userId)

    override suspend fun addCart(plantModel: PlantModel): Result<Unit> {
        return try {
            val plantRef = cartRef.child(plantModel.plantId)

            // Save the plant to the favorites
            plantRef.setValue(plantModel).await()

            // Return success state
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun loadCart(): Result<List<PlantModel>> {
        return try {
            // Fetch the user's favorite plants
            val snapshot = cartRef.get().await()

            // Parse the data snapshot into a list of PlantModel objects
            val cartList = mutableListOf<PlantModel>()
            for (childSnapshot in snapshot.children) {
                val plant = childSnapshot.getValue(PlantModel::class.java)
                plant?.let { cartList.add(it) }
            }

            // Return success state with the favorite list
            Result.success(cartList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteCart(plantId: String): Result<Unit> {
        return try {
            val plantRef = cartRef.child(plantId)

            // Remove the plant from the favorites
            plantRef.removeValue().await()

            // Return success state
            Result.success(Unit)
        } catch (e: Exception) {
            // Return error state if there's any exception
            Result.failure(e)
        }
    }


    override suspend fun checkoutCart(selectedPlantIds: Set<String>): Result<Unit> {
        return try {
            val selectedPlants = mutableListOf<PlantModel>()

            // Fetch each selected plant from the cart
            selectedPlantIds.forEach { plantId ->
                val plantSnapshot = cartRef.child(plantId).get().await()
                val plant = plantSnapshot.getValue(PlantModel::class.java)
                plant?.let { selectedPlants.add(it) }
            }

            // Save selected plants to orders node
            val orderId =
                ordersRef.push().key ?: throw IllegalStateException("Could not generate order ID")
            val orderRef = ordersRef.child(orderId)
            orderRef.setValue(selectedPlants).await()

            // Remove each selected plant from the cart
            selectedPlantIds.forEach { plantId ->
                cartRef.child(plantId).removeValue().await()
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


}