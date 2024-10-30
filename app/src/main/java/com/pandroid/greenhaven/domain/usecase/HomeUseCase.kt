package com.pandroid.greenhaven.domain.usecase

import android.net.Uri
import com.pandroid.greenhaven.data.resource.State
import com.pandroid.greenhaven.domain.model.PlantModel
import com.pandroid.greenhaven.domain.model.UserModel
import com.pandroid.greenhaven.domain.repo.HomeRepo
import javax.inject.Inject

class HomeUseCase @Inject constructor(private val homeRepo: HomeRepo) : HomeRepo {
    override suspend fun loadUser(): State<UserModel> {
        return homeRepo.loadUser()
    }

    override suspend fun addPlant(
        plantModel: PlantModel,
        plantImageUri: Uri,
        imageUris: List<Uri>
    ): State<Unit> {
        return homeRepo.addPlant(plantModel, plantImageUri, imageUris)
    }

    override suspend fun loadPlant(): State<List<PlantModel>> {
        return homeRepo.loadPlant()
    }

}