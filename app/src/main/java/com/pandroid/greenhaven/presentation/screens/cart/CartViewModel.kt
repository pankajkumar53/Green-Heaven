package com.pandroid.greenhaven.presentation.screens.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pandroid.greenhaven.data.resource.State
import com.pandroid.greenhaven.domain.model.PlantModel
import com.pandroid.greenhaven.domain.usecase.CartUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class CartViewModel @Inject constructor(private val cartUseCase: CartUseCase) : ViewModel() {

    private val _cartState = MutableLiveData<State<Any>>()
    val cartState: LiveData<State<Any>> get() = _cartState

    private val _cartList = MutableLiveData<List<PlantModel>>()
    val cartList: LiveData<List<PlantModel>> get() = _cartList

    private val _selectedPlants = MutableLiveData<Set<String>>()
    val selectedPlants: LiveData<Set<String>> get() = _selectedPlants

    fun togglePlantSelection(plantId: String) {
        val currentSelection = _selectedPlants.value ?: emptySet()
        _selectedPlants.value = if (currentSelection.contains(plantId)) {
            currentSelection - plantId
        } else {
            currentSelection + plantId
        }
    }

    private val _checkoutState = MutableLiveData<State<Any>>()
    val checkoutState: LiveData<State<Any>> get() = _checkoutState

    fun proceedToCheckout() {
        val selectedPlantIds = _selectedPlants.value ?: emptySet()

        if (selectedPlantIds.isEmpty()) {
            _checkoutState.value = State.Error("No items selected for checkout.")
            return
        }

        _checkoutState.value = State.Loading()

        viewModelScope.launch {
            val result = cartUseCase.checkoutCart(selectedPlantIds)
            result.fold(
                onSuccess = {
                    _checkoutState.value = State.Success("Checkout successful!")
                    clearSelection()  // Clear selection after checkout
                },
                onFailure = { error ->
                    _checkoutState.value = State.Error("Checkout failed: ${error.message}")
                }
            )
        }
    }

    private fun clearSelection() {
        _selectedPlants.value = emptySet()
    }


    fun loadCart() {
        _cartState.value = State.Loading()

        viewModelScope.launch {
            val result = cartUseCase.loadCart()
            result.fold(
                onSuccess = { plants: List<PlantModel> ->
                    _cartList.value = plants
                    _cartState.value = State.Success(Unit)
                },
                onFailure = { error ->
                    _cartState.value = State.Error("${error.message}")
                }
            )
        }
    }

    fun addCart(plantModel: PlantModel) {
        _cartState.value = State.Loading()

        viewModelScope.launch {
            val result = cartUseCase.addCart(plantModel)
            result.fold(
                onSuccess = {
                    _cartState.value = State.Success(Unit)
                },
                onFailure = { error ->
                    _cartState.value = State.Error("Failed to add to favorites: ${error.message}")
                }
            )
        }
    }

    fun deleteCart(plantId: String) {
        _cartState.value = State.Loading()

        viewModelScope.launch {
            val result = cartUseCase.deleteCart(plantId)
            result.fold(
                onSuccess = {
                    _cartState.value = State.Success(Unit)
                },
                onFailure = { error ->
                    _cartState.value =
                        State.Error("Failed to remove from favorites: ${error.message}")
                }
            )
        }
    }


}