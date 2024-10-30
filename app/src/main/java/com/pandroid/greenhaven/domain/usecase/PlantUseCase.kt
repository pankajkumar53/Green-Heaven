package com.pandroid.greenhaven.domain.usecase

import android.net.Uri
import com.pandroid.greenhaven.domain.model.PlantModel
import com.pandroid.greenhaven.domain.repo.PlantRepo
import javax.inject.Inject

class PlantUseCase @Inject constructor(private val plantRepo: PlantRepo) : PlantRepo {
    override suspend fun addPlant(
        imageUri: Uri,
        title: String,
        rating: String,
        description: String,
        price: String,
        category: String,
        images: List<Uri>
    ): Result<Unit> {
        return plantRepo.addPlant(imageUri, title, rating, description, price, category, images)
    }

    override suspend fun loadPlant(): Result<List<PlantModel>> {
        return plantRepo.loadPlant()
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
        return plantRepo.updatePlant(
            plantId,
            imageUri,
            title,
            rating,
            description,
            price,
            category,
            images
        )
    }

    override suspend fun deletePlant(plantId: String): Result<Unit> {
        return plantRepo.deletePlant(plantId)
    }

}