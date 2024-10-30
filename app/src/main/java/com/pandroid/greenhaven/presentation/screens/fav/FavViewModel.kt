package com.pandroid.greenhaven.presentation.screens.fav

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pandroid.greenhaven.data.resource.State
import com.pandroid.greenhaven.domain.model.PlantModel
import com.pandroid.greenhaven.domain.usecase.FavUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavViewModel @Inject constructor(
    private val favUseCase: FavUseCase
) : ViewModel()
{
    private val _favState = MutableLiveData<State<Any>>()
    val favState: LiveData<State<Any>> get() = _favState

    private val _favList = MutableLiveData<List<PlantModel>>()
    val favList: LiveData<List<PlantModel>> get() = _favList

    fun loadFavorites() {
        _favState.value = State.Loading()

        viewModelScope.launch {
            val result = favUseCase.loadFav()
            result.fold(
                onSuccess = { plants: List<PlantModel> ->
                    _favList.value = plants
                    _favState.value = State.Success(Unit)
                },
                onFailure = { error ->
                    _favState.value = State.Error("${error.message}")
                }
            )
        }
    }

    fun addFavorite(plantModel: PlantModel) {
        _favState.value = State.Loading()

        viewModelScope.launch {
            val result = favUseCase.addFav(plantModel)
            result.fold(
                onSuccess = {
                    _favState.value = State.Success(Unit)
                },
                onFailure = { error ->
                    _favState.value = State.Error("Failed to add to favorites: ${error.message}")
                }
            )
        }
    }

    fun deleteFavorite(plantId: String) {
        _favState.value = State.Loading()

        viewModelScope.launch {
            val result = favUseCase.deleteFav(plantId)
            result.fold(
                onSuccess = {
                    _favState.value = State.Success(Unit)
                },
                onFailure = { error ->
                    _favState.value =
                        State.Error("Failed to remove from favorites: ${error.message}")
                }
            )
        }
    }

}


