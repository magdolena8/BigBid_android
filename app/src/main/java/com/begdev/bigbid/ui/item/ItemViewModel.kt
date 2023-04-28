package com.begdev.bigbid.ui.item

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.begdev.bigbid.data.repository.ItemsRepo
import com.begdev.bigbid.nav_utils.AppNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemViewModel @Inject constructor(
    private val AppNavigator: AppNavigator,
    private val itemRepo: ItemsRepo,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ItemUiState())
    val uiState: StateFlow<ItemUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val itemId: String = checkNotNull(savedStateHandle["itemId"])
            val item = itemRepo.getItem(itemId.toInt())
            _uiState.value = ItemUiState(item!!)
        }
    }


    fun handleEvent(itemEvent: ItemEvent) {
        when (itemEvent) {
            is ItemEvent.PlaceBid -> {
//                placeBid(itemEvent.newPrice)
            }

            is ItemEvent.AddToFavourite -> {
//                addToFavourite(authenticationEvent.password)
            }

            is ItemEvent.CurrentPriceChanged -> {
//                updatePrice(itemEvent.newPrice)

            }
        }
    }
}