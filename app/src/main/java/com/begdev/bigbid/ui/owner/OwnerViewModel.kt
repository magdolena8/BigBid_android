package com.begdev.bigbid.ui.owner

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.begdev.bigbid.data.api.model.Item
import com.begdev.bigbid.data.repository.ItemsRepo
import com.begdev.bigbid.data.repository.UsersRepo
import com.begdev.bigbid.nav_utils.Screen
import com.begdev.bigbid.ui.add_item.AddItemUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OwnerViewModel @Inject constructor(
    private val itemsRepo: ItemsRepo,
) : ViewModel() {

    private val _navigationEvent = MutableSharedFlow<String>()
    val navigationEvent: SharedFlow<String> = _navigationEvent

    private val _itemsState = mutableStateListOf<Item>()
    val itemsState: List<Item> = _itemsState

    val newItemUiState = MutableStateFlow(AddItemUiState())


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
            is OwnerEvent.DescriptionChanged ->{
                updateDescription(ownerEvent.description)
            }
            is OwnerEvent.DurationChanged -> {
                updateDuration(ownerEvent.duration)
            }
            is OwnerEvent.PriceChanged -> {
                updatePrice(ownerEvent.price)
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
}