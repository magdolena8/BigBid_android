package com.begdev.bigbid.ui.owner

import android.app.Application
import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.begdev.bigbid.data.api.model.Item
import com.begdev.bigbid.data.repository.ItemsRepo
import com.begdev.bigbid.data.repository.UsersRepo
import com.begdev.bigbid.nav_utils.Screen
import com.begdev.bigbid.nav_utils.appendParams
import com.begdev.bigbid.ui.add_item.AddItemUiState
import com.begdev.bigbid.utils.ConnectivityChecker
import com.begdev.bigbid.utils.fileFromContentUri
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class OwnerViewModel @Inject constructor(
    private val itemsRepo: ItemsRepo,
    private val application: Application,
    private val connectivityChecker: ConnectivityChecker
) : ViewModel() {

    val isOnline: StateFlow<Boolean> = connectivityChecker.isOnline


    private val _navigationEvent = MutableSharedFlow<String>()
    val navigationEvent: SharedFlow<String> = _navigationEvent

    private val _itemsState = mutableStateListOf<Item>()
    val itemsState: List<Item> = _itemsState

    val newItemUiState = MutableStateFlow(AddItemUiState())

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing


    init {
        viewModelScope.launch {
            val items = itemsRepo.getItemsOwner(UsersRepo.currentUser?.id!!)
            if (items != null) {
                _itemsState.addAll(items)
            }
        }
    }

    fun handleEvent(ownerEvent: OwnerEvent) {
        when (ownerEvent) {
            is OwnerEvent.AddFABClicked -> {
                viewModelScope.launch {
                    navigateToAddItemScreen()
                }
            }

            is OwnerEvent.TitleChanged -> {
                updateTitle(ownerEvent.title)
            }

            is OwnerEvent.CategoryChanged -> {
                updateCategory(ownerEvent.category)
            }

            is OwnerEvent.DescriptionChanged -> {
                updateDescription(ownerEvent.description)
            }

            is OwnerEvent.DurationChanged -> {
                updateDuration(ownerEvent.duration)
            }

            is OwnerEvent.PriceChanged -> {
                updatePrice(ownerEvent.price)
            }

            is OwnerEvent.AddButtonClicked -> {
                createLot()
            }

            is OwnerEvent.SaleItemClick -> {
                viewModelScope.launch {
                    navigateToItemScreen(ownerEvent.item)
                }
            }
        }
    }

    private fun createLot() {
        viewModelScope.launch {
            val result = itemsRepo.createLot(
                userId = UsersRepo.currentUser?.id!!,
                imageFile = fileFromContentUri(application, newItemUiState.value.imageUri!!),
                title = newItemUiState.value.title,
                category = newItemUiState.value.category,
                description = newItemUiState.value.description,
                startPrice = newItemUiState.value.startPrice,
                aucDuration = newItemUiState.value.aucDuration!!,
                imageUri = newItemUiState.value.imageUri!!
            )
            if (result == true) {
                Log.d(TAG, "createLot: SECCESS")
            }
        }
    }

    private fun uploadImage() {
        viewModelScope.launch {
            val uri = newItemUiState.value.imageUri
            val result = itemsRepo.uploadImage(
                imageFile = fileFromContentUri(application, uri!!)
            )
            if (result == true) {
                Log.d(TAG, "createLot: SECCESS")
            }
        }
    }

    private fun updateTitle(title: String) {
        newItemUiState.value = newItemUiState.value.copy(
            title = title
        )
    }

    private fun updateCategory(category: String) {
        newItemUiState.value = newItemUiState.value.copy(
            category = category
        )
    }

    private fun updateDescription(description: String) {
        newItemUiState.value = newItemUiState.value.copy(
            description = description
        //todo: NULL в бд !!!!!!!!
        )
    }

    private fun updatePrice(price: Float) {
        newItemUiState.value = newItemUiState.value.copy(
            startPrice = price
        )
    }

    private fun updateDuration(duration: Int) {
        newItemUiState.value = newItemUiState.value.copy(
            aucDuration = duration
        )
    }

    private suspend fun navigateToAddItemScreen() {
        val route = Screen.AddItem.route
        _navigationEvent.emit(route)
    }

    private suspend fun navigateToItemScreen(item: Item) {
        val route = Screen.Item.route.appendParams("ITEM_ID" to item.id)
        _navigationEvent.emit(route)
    }

    fun updateImageUri(uri: Uri?) {
        newItemUiState.value = newItemUiState.value.copy(
            imageUri = uri
        )
    }

    fun updateBitmap(bitmap: Bitmap?) {
        newItemUiState.value = newItemUiState.value.copy(
            bitmap = bitmap
        )
    }

    fun refreshData() {
        viewModelScope.launch {
            _isRefreshing.value = true
            val items = itemsRepo.getItemsOwner(UsersRepo.currentUser?.id!!)
            if (items != null) {
                _itemsState.clear()
                _itemsState.addAll(items)
            }
            _isRefreshing.value = false
        }
    }

}