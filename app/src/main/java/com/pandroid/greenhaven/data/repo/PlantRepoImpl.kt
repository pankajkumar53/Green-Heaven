package com.pandroid.greenhaven.data.repo

import android.net.Uri
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.pandroid.greenhaven.data.repo.UploadImage.uploadImage
import com.pandroid.greenhaven.domain.model.PlantImageUrlModel
import com.pandroid.greenhaven.domain.model.PlantModel
import com.pandroid.greenhaven.domain.repo.PlantRepo
import com.pandroid.greenhaven.utils.NodeRef
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PlantRepoImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase,
    private val firebaseStorage: FirebaseStorage
) : PlantRepo {

    override suspend fun addPlant(
        imageUri: Uri,
        title: String,
        rating: String,
        description: String,
        price: String,
        category: String,
        images: List<Uri>
    ): Result<Unit> {

        return try {
            val plantId = firebaseDatabase.getReference(NodeRef.PLANTS_REF).push().key
                ?: throw IllegalStateException("Unable to generate product ID")

            // Upload cover image
            val coverImgUrlResult =
                uploadImage(imageUri, title, "${NodeRef.PLANT_IMAGE_REF}$title")
            if (coverImgUrlResult.isFailure) {
                return Result.failure(coverImgUrlResult.exceptionOrNull()!!)
            }
            val coverImgUrl = coverImgUrlResult.getOrNull()

            // Upload additional images and create a list of ProductImageUrlModel
            val imageUrlList = ArrayList<PlantImageUrlModel>()
            images.forEachIndexed { index, uri ->
                val imageUrlResult =
                    uploadImage(uri, "$title-image-$index", "${NodeRef.PLANT_IMAGE_REF}$title")
                if (imageUrlResult.isSuccess) {
                    imageUrlList.add(
                        PlantImageUrlModel(
                            imageId = "$index",
                            imageUrl = imageUrlResult.getOrNull()!!
                        )
                    )
                } else {
                    return Result.failure(imageUrlResult.exceptionOrNull()!!)
                }
            }

            // Create the ProductModel object
            val plant = PlantModel(
                plantId = plantId,
                plantImage = coverImgUrl!!,
                title = title,
                rating = rating,
                description = description,
                price = price,
                category = category,
                plantImages = imageUrlList,

                )


            // Save the product to Firebase Realtime Database
            val reference = firebaseDatabase.getReference(NodeRef.PLANTS_REF).child(plantId)
            reference.setValue(plant).await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun loadPlant(): Result<List<PlantModel>> {
        return try {
            // Fetch the data from the PRODUCTS_REF in Firebase
            val plantRef = firebaseDatabase.getReference(NodeRef.PLANTS_REF)
            val snapshot = plantRef.get().await()

            // Convert snapshot to list of ProductModel
            val plantList = mutableListOf<PlantModel>()
            for (plantSnapshot in snapshot.children) {
                val plant = plantSnapshot.getValue(PlantModel::class.java)
                plant?.let {
                    plantList.add(it)
                }
            }

            // Return the list of products
            Result.success(plantList)
        } catch (e: Exception) {
            // In case of error, return failure result with the exception
            Result.failure(e)
        }
    }

    override suspend fun updatePlant(
        plantId: String,
        imageUri: Uri?,
        title: String,
        rating: String,
        description: String,
        price: String,
        category: String,
        images: List<Uri>?
    ): Result<Unit> {
        return try {
            // Fetch the current product details from Firebase
            val reference = firebaseDatabase.getReference(NodeRef.PLANTS_REF).child(plantId)
            val plantSnapshot = reference.get().await()

            if (!plantSnapshot.exists()) {
                throw IllegalStateException("Product with ID $plantId does not exist.")
            }

            // Convert snapshot to ProductModel
            val currentPlant = plantSnapshot.getValue(PlantModel::class.java)
                ?: throw IllegalStateException("Unable to parse product data.")

            // Handle cover image update (if new imageUri is provided)
            val coverImgUrl = if (imageUri != null) {
                val coverImgUrlResult =
                    uploadImage(imageUri, title, "${NodeRef.PLANT_IMAGE_REF}$title")
                if (coverImgUrlResult.isFailure) {
                    return Result.failure(coverImgUrlResult.exceptionOrNull()!!)
                }
                coverImgUrlResult.getOrNull()!!
            } else {
                currentPlant.plantImage
            }

            // Handle additional images update (if new images are provided)
            val updatedImageList = if (images != null && images.isNotEmpty()) {
                val imageUrlList = ArrayList<PlantImageUrlModel>()
                images.forEachIndexed { index, uri ->
                    val imageUrlResult = uploadImage(
                        uri, "$title-image-$index", "${NodeRef.PLANT_IMAGE_REF}$title"
                    )
                    if (imageUrlResult.isSuccess) {
                        imageUrlList.add(
                            PlantImageUrlModel(
                                imageId = "$index",
                                imageUrl = imageUrlResult.getOrNull()!!
                            )
                        )
                    } else {
                        return Result.failure(imageUrlResult.exceptionOrNull()!!)
                    }
                }
                imageUrlList
            } else {
                currentPlant.plantImages // Retain the existing additional images
            }

            // Update the ProductModel object with new values
            val updatedPlant = currentPlant.copy(
                plantId = plantId,
                plantImage = coverImgUrl,
                title = title,
                rating = rating,
                description = description,
                price = price,
                category = category,
                plantImages = updatedImageList
            )

            // Save the updated product to Firebase Realtime Database
            reference.setValue(updatedPlant).await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deletePlant(plantId: String): Result<Unit> {
        return try {
            // Reference to the product in the Firebase Realtime Database
            val plantRef = firebaseDatabase.getReference(NodeRef.PLANTS_REF).child(plantId)

            // Fetch the product details before deletion to get associated images
            val plantSnapshot = plantRef.get().await()

            if (!plantSnapshot.exists()) {
                throw IllegalStateException("Product with ID $plantId does not exist.")
            }

            // Convert snapshot to ProductModel to retrieve image URLs
            val plant = plantSnapshot.getValue(PlantModel::class.java)
                ?: throw IllegalStateException("Unable to parse product data.")

            // Delete the cover image from Firebase Storage
            val coverImgRef = firebaseStorage.getReferenceFromUrl(plant.plantImage)
            coverImgRef.delete().await()

            // Delete additional images from Firebase Storage
            plant.plantImages.forEach { imageModel ->
                val imageRef = firebaseStorage.getReferenceFromUrl(imageModel.imageUrl)
                imageRef.delete().await()
            }

            // Delete the product entry from Firebase Realtime Database
            plantRef.removeValue().await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
