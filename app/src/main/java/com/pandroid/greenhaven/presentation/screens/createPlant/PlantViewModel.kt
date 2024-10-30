package com.pandroid.greenhaven.presentation.screens.createPlant

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pandroid.greenhaven.data.resource.State
import com.pandroid.greenhaven.domain.model.PlantModel
import com.pandroid.greenhaven.domain.usecase.PlantUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class PlantViewModel @Inject constructor(private val plantUseCase: PlantUseCase) :
    ViewModel() {

    private val _state = MutableStateFlow<State<Any>>(State.Idle())
    val state: StateFlow<State<Any>> = _state

    private val _plantList = MutableStateFlow<List<PlantModel>>(emptyList())
    val plantList: StateFlow<List<PlantModel>> = _plantList


    private var isPlantUpdated = false


    fun addPlant(
        imageUri: Uri,
        title: String,
        rating: String,
        description: String,
        price: String,
        category: String,
        images: List<Uri>

    ) {
        viewModelScope.launch {
            _state.value = State.Loading()
            try {
                val result = plantUseCase.addPlant(
                    imageUri = imageUri,
                    title = title,
                    rating = rating,
                    description = description,
                    price = price,
                    category = category,
                    images = images,
                )
                if (result.isSuccess) {
                    _state.value = State.Success(Unit)
                } else {
                    _state.value = State.Error(result.exceptionOrNull()?.message ?: "Unknown error")
                }
            } catch (e: Exception) {
                _state.value = State.Error(e.message ?: "An unexpected error occurred")
            }

        }
    }


    fun loadPlant() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = State.Loading()
            val result = plantUseCase.loadPlant()
            val plants = result.getOrNull() ?: emptyList()
            _plantList.value = plants
            _state.value = State.Success(plants)
        }
    }


    fun updatePlant(
        plantId: String,
        imageUri: Uri?,
        title: String,
        rating: String,
        description: String,
        price: String,
        category: String,
        images: List<Uri>?
    ) {
        viewModelScope.launch {
            _state.value = State.Loading()
            try {
                val result = plantUseCase.updatePlant(
                    plantId = plantId,
                    imageUri = imageUri,
                    title = title,
                    rating = rating,
                    description = description,
                    price = price,
                    category = category,
                    images = images,
                )
                if (result.isSuccess) {
                    isPlantUpdated = true
                    _state.value = State.Success(Unit)
                    loadPlant()
                } else {
                    _state.value = State.Error(result.exceptionOrNull()?.message ?: "Unknown error")
                }
            } catch (e: Exception) {
                _state.value = State.Error(e.message ?: "An unexpected error occurred")
            }
        }
    }


    fun delete(plantId: String) {
        viewModelScope.launch {
            _state.value = State.Loading()
            try {
                plantUseCase.deletePlant(plantId)
                loadPlant()
                _state.value = State.Success(Unit)
            } catch (e: Exception) {
                _state.value = State.Error(e.message ?: "Failed to delete plant")
            }
        }
    }


    fun resetPlantUpdatedFlag() {
        isPlantUpdated = false
    }

    fun isPlantRecentlyUpdated() = isPlantUpdated


}