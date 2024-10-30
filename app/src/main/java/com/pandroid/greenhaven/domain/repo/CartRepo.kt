package com.pandroid.greenhaven.domain.repo

import com.pandroid.greenhaven.domain.model.PlantModel

interface CartRepo {
    suspend fun addCart(plantModel: PlantModel): Result<Unit>
    suspend fun loadCart(): Result<List<PlantModel>>
    suspend fun deleteCart(plantId: String): Result<Unit>
    suspend fun checkoutCart(selectedPlantIds: Set<String>): Result<Unit>
}