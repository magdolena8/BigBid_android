package com.begdev.bigbid.ui.catalog

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.begdev.bigbid.data.api.model.Item
import com.begdev.bigbid.data.repository.ItemsRepo
import com.begdev.bigbid.nav_utils.Screen
import com.begdev.bigbid.nav_utils.appendParams
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val itemsRepo: ItemsRepo
) : ViewModel() {

    private val _navigationEvent = MutableSharedFlow<String>()
    val navigationEvent: SharedFlow<String> = _navigationEvent

    private val _itemsState = mutableStateListOf<Item>()
    val itemsState: List<Item> = _itemsState

    init {
        viewModelScope.launch {
            val items = itemsRepo.getItemsHome()
            _itemsState.addAll(items)
        }
    }

    fun handleEvent(catalogEvent: CatalogEvent) {
        when (catalogEvent) {
            is CatalogEvent.ItemClicked -> {
                viewModelScope.launch {
                    navigateToItemScreen(catalogEvent.item)
                }
            }
        }
    }

    private suspend fun navigateToItemScreen(item: Item) {
        val qwe = Screen.Item.route.appendParams("ITEM_ID" to item.id)
        _navigationEvent.emit(qwe)
    }


}