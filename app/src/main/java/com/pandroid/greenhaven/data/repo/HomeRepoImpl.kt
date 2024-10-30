package com.pandroid.greenhaven.data.repo

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.pandroid.greenhaven.data.repo.UploadImage.uploadImage
import com.pandroid.greenhaven.data.resource.State
import com.pandroid.greenhaven.domain.model.PlantImageUrlModel
import com.pandroid.greenhaven.domain.model.PlantModel
import com.pandroid.greenhaven.domain.model.UserModel
import com.pandroid.greenhaven.domain.repo.HomeRepo
import com.pandroid.greenhaven.utils.NodeRef
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class HomeRepoImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase,
    private val firebaseAuth: FirebaseAuth
) : HomeRepo {

    // Load User
    override suspend fun loadUser(): State<UserModel> {
        return try {
            val userId = firebaseAuth.currentUser?.uid
            val userSnapshot = userId?.let {
                firebaseDatabase.getReference(NodeRef.USER_REF).child(it).get().await()
            }
            val user = userSnapshot?.getValue(UserModel::class.java)
            if (user != null) {
                State.Success(user)
            } else {
                State.Error(Exception("User not found").toString())
            }
        } catch (e: Exception) {
            State.Error(e.toString())
        }
    }

    // Add Plant
    override suspend fun addPlant(
        plantModel: PlantModel,
        plantImageUri: Uri,
        imageUris: List<Uri>
    ): State<Unit> {

        return try {
            val plantId = firebaseDatabase.reference.push().key
                ?: throw Exception("Failed to generate plant ID")

            val uploadedImageUrls = mutableListOf<String>()

            // 1. Upload the main plantImage
            val mainImageUploadResult =
                uploadImage(plantImageUri, "${plantModel.plantId}_main_image", path = NodeRef.PLANT_IMAGE_REF)
            mainImageUploadResult.onSuccess { mainImageUrl ->
                // Update the plantImage field in the PlantModel
                plantModel.plantImage = mainImageUrl
            }.onFailure { error ->
                throw Exception("Main plant image upload failed: ${error.message}")
            }

            // 2. Upload additional plantImages asynchronously and collect URLs
            imageUris.forEachIndexed { index, uri ->
                val uploadResult = uploadImage(uri, title = "${plantModel.plantId}_image_$index", path = NodeRef.PLANT_IMAGE_REF)
                uploadResult.onSuccess { url ->
                    uploadedImageUrls.add(url)
                }.onFailure { error ->
                    throw Exception("Additional image upload failed: ${error.message}")
                }
            }

            // 3. Add the URLs to the plantImages field
            val plantImages = uploadedImageUrls.map { imageUrl -> PlantImageUrlModel(imageUrl) }
            val updatedPlantModel =
                plantModel.copy(plantId = plantId, plantImages = ArrayList(plantImages))

            // 4. Save the plant data to Firebase
            firebaseDatabase.getReference(NodeRef.PLANTS_REF)
                .child(plantId)
                .setValue(updatedPlantModel)
                .await()

            State.Success(Unit)
        } catch (e: Exception) {
            State.Error(e.toString())
        }
    }

    // Load Plant
    override suspend fun loadPlant(): State<List<PlantModel>> {
        return try {
            val productSnapshot =
                firebaseDatabase.getReference(NodeRef.PLANTS_REF).get().await()
            val products =
                productSnapshot.children.mapNotNull { it.getValue(PlantModel::class.java) }
            State.Success(products.shuffled())
        } catch (e: Exception) {
            State.Error(e.toString())
        }
    }

}
