package com.pandroid.greenhaven.domain.repo

import android.net.Uri
import com.pandroid.greenhaven.data.resource.State
import com.pandroid.greenhaven.domain.model.PlantModel
import com.pandroid.greenhaven.domain.model.UserModel

interface HomeRepo {
    suspend fun loadUser(): State<UserModel>
    suspend fun addPlant(
        plantModel: PlantModel,
        plantImageUri: Uri,
        imageUris: List<Uri>
    ): State<Unit>

    suspend fun loadPlant(): State<List<PlantModel>>
}