package com.begdev.bigbid.ui.catalog

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.begdev.bigbid.data.api.model.Item
import com.begdev.bigbid.data.repository.ItemsRepo
import com.begdev.bigbid.data.repository.UsersRepo
import com.begdev.bigbid.nav_utils.Screen
import com.begdev.bigbid.nav_utils.appendParams
import com.begdev.bigbid.utils.ConnectivityChecker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val itemsRepo: ItemsRepo,
    private val usersRepo: UsersRepo,
    private val connectivityChecker: ConnectivityChecker

) : ViewModel() {
    val isOnline: StateFlow<Boolean> = connectivityChecker.isOnline

    private val _navigationEvent = MutableSharedFlow<String>()
    val navigationEvent: SharedFlow<String> = _navigationEvent

    private val _itemsState = mutableStateListOf<Item>()
    val itemsState: List<Item> = _itemsState

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    init {
        if (isOnline.value) {
            viewModelScope.launch {
                val items =
                    itemsRepo.getItemsCatalog(UsersRepo.currentUser?.id!!, searchQuery.value)
                if (items != null) {
                    _itemsState.addAll(items)
                }
            }
        }
    }
    fun handleEvent(catalogEvent: CatalogEvent) {
        when (catalogEvent) {
            is CatalogEvent.ItemClicked -> {
                viewModelScope.launch {
                    navigateToItemScreen(catalogEvent.item)
                }
            }

            is CatalogEvent.SearchQueryChanged -> {
                _searchQuery.value = catalogEvent.queryString
            }

            is CatalogEvent.ToggleSearchState -> {
                _isSearching.value = !(_isSearching.value)
            }

            is CatalogEvent.SearchItems -> {
                viewModelScope.launch {
                    searchItems(_searchQuery.value)
                }
            }

            is CatalogEvent.RefreshItemsState -> {
                refreshData()
            }
        }
    }

    private suspend fun navigateToItemScreen(item: Item) {
        val route = Screen.Item.route.appendParams("ITEM_ID" to item.id)
        _navigationEvent.emit(route)
    }

    private suspend fun searchItems(query: String) {
        viewModelScope.launch {
            val items = itemsRepo.getItemsCatalog(UsersRepo.currentUser?.id!!, searchQuery.value)
            if (items != null) {
                _itemsState.clear()
                _itemsState.addAll(items)
            }
        }
    }

    fun refreshData() {
        viewModelScope.launch {
            _isRefreshing.value = true
            val items = itemsRepo.getItemsCatalog(UsersRepo.currentUser?.id!!, searchQuery.value)
            if (items != null) {
                _itemsState.clear()
                _itemsState.addAll(items)
            }
            _isRefreshing.value = false
        }
    }


}