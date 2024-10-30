package com.pandroid.greenhaven.domain.usecase

import com.pandroid.greenhaven.domain.model.PlantModel
import com.pandroid.greenhaven.domain.repo.FavRepo
import javax.inject.Inject

class FavUseCase @Inject constructor(private val favRepo: FavRepo) : FavRepo {
    override suspend fun addFav(plantModel: PlantModel): Result<Unit> {
        return favRepo.addFav(plantModel)
    }

    override suspend fun loadFav(): Result<List<PlantModel>> {
        return favRepo.loadFav()
    }

    override suspend fun deleteFav(plantId: String): Result<Unit> {
        return favRepo.deleteFav(plantId)
    }

}