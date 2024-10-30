package com.pandroid.greenhaven.domain.repo

import android.net.Uri
import com.pandroid.greenhaven.domain.model.PlantModel

interface PlantRepo {

    suspend fun addPlant(
        imageUri: Uri,
        title: String,
        rating: String,
        description: String,
        price: String,
        category: String,
        images: List<Uri>
    ): Result<Unit>

    suspend fun loadPlant(): Result<List<PlantModel>>

    suspend fun updatePlant(
        plantId: String,
        imageUri: Uri?,
        title: String,
        rating: String,
        description: String,
        price: String,
        category: String,
        images: List<Uri>?
    ): Result<Unit>

    suspend fun deletePlant(plantId: String): Result<Unit>

}