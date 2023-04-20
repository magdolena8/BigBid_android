package com.begdev.bigbid.ui.home

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.begdev.bigbid.data.api.model.Item
import com.begdev.bigbid.data.repository.ItemsRepo
import com.begdev.bigbid.nav_utils.AppNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val appNavigator: AppNavigator,
    private val itemRepo: ItemsRepo
) : ViewModel() {

    private val _itemsState = mutableStateListOf<Item>()
    val itemsState: List<Item> = _itemsState

    init {
            viewModelScope.launch {
                val items = itemRepo.getItems()
                _itemsState.addAll(items)
//                _itemsState.addAll( listOf(Item(1, "ivan"),Item(2, "egor"),Item(3, "alex")))
            }
        }


}