package com.begdev.bigbid.ui.liked

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.begdev.bigbid.data.api.model.Item
import com.begdev.bigbid.data.repository.ItemsRepo
import com.begdev.bigbid.data.repository.UsersRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LikedViewModel @Inject constructor(
    private val itemsRepo: ItemsRepo,
) : ViewModel() {
    private val _navigationEvent = MutableSharedFlow<String>()
    val navigationEvent: SharedFlow<String> = _navigationEvent

    private val _itemsState = mutableStateListOf<Item>()
    val itemsState: List<Item> = _itemsState

    init {
        viewModelScope.launch {
            val items = itemsRepo.getItemsLiked(UsersRepo.currentUser?.id!!)
            if (items != null) {
                _itemsState.addAll(items)
            }
        }
    }

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    fun refreshData() {
        viewModelScope.launch {
            _isRefreshing.value = true
            val bids = itemsRepo.getItemsLiked(UsersRepo.currentUser?.id!!)
            if (bids != null) {
                _itemsState.clear()
                _itemsState.addAll(bids)
            }
            _isRefreshing.value = false
        }
    }


}