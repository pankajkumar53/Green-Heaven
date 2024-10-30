package com.pandroid.greenhaven.domain.repo

import com.pandroid.greenhaven.domain.model.PlantModel

interface FavRepo {
    suspend fun addFav(plantModel: PlantModel): Result<Unit>
    suspend fun loadFav(): Result<List<PlantModel>>
    suspend fun deleteFav(plantId: String): Result<Unit>
}


