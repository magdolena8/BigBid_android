package com.begdev.bigbid.ui.home

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.begdev.bigbid.data.api.model.Item
import com.begdev.bigbid.data.repository.ItemsRepo
import com.begdev.bigbid.nav_utils.AppNavigator
import com.begdev.bigbid.nav_utils.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val appNavigator: AppNavigator,
    private val itemsRepo: ItemsRepo
) : ViewModel() {

    private val _itemsState = mutableStateListOf<Item>()
    val itemsState: List<Item> = _itemsState

    init {
        viewModelScope.launch {
            val items = itemsRepo.getItemsHome()
            _itemsState.addAll(items)
        }
    }

    fun handleEvent(homeEvent: HomeEvent) {
        when (homeEvent) {
            is HomeEvent.ItemClicked -> {
                navigateToItemScreen(homeEvent.itemId)
            }
        }
    }
    fun navigateToItemScreen(item:Item){
        appNavigator.tryNavigateTo(Destination.ItemScreen(item.id!!))
    }


}