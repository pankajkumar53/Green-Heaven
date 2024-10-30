package com.pandroid.greenhaven.domain.usecase

import com.pandroid.greenhaven.domain.model.PlantModel
import com.pandroid.greenhaven.domain.repo.CartRepo
import javax.inject.Inject

class CartUseCase @Inject constructor(private val cartRepo: CartRepo) : CartRepo {
    override suspend fun addCart(plantModel: PlantModel): Result<Unit> {
        return cartRepo.addCart(plantModel)
    }

    override suspend fun loadCart(): Result<List<PlantModel>> {
        return cartRepo.loadCart()
    }

    override suspend fun deleteCart(plantId: String): Result<Unit> {
        return cartRepo.deleteCart(plantId)
    }

    override suspend fun checkoutCart(selectedPlantIds: Set<String>): Result<Unit> {
        return cartRepo.checkoutCart(selectedPlantIds)
    }

}