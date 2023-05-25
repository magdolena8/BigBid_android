package com.begdev.bigbid.ui.bids

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.begdev.bigbid.data.api.model.Bid
import com.begdev.bigbid.data.repository.BidsRepo
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
class BidsViewModel @Inject constructor(
    private val bidsRepo: BidsRepo,
    private val connectivityChecker: ConnectivityChecker

) : ViewModel() {
    private val _navigationEvent = MutableSharedFlow<String>()
    val navigationEvent: SharedFlow<String> = _navigationEvent
    val isOnline: StateFlow<Boolean> = connectivityChecker.isOnline
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing
    private val _bidsState = mutableStateListOf<Bid>()
    val bidsState: List<Bid> = _bidsState

    init {
        viewModelScope.launch {
            val bids = bidsRepo.getBidsUser(UsersRepo.currentUser?.id!!)
            if (bids != null) {
                _bidsState.addAll(bids)
            }
        }
    }

    fun handleEvent(bidsEvent: BidsEvent) {
        when (bidsEvent) {
            is BidsEvent.BidClicked -> {
                viewModelScope.launch {
                    navigateToItemScreen(bidsEvent.bid.itemId!!)
                }
            }
        }
    }

    private suspend fun navigateToItemScreen(itemId: Int) {
        val route = Screen.Item.route.appendParams("ITEM_ID" to itemId)
        _navigationEvent.emit(route)
    }

    fun refreshData() {
        viewModelScope.launch {
            _isRefreshing.value = true
            val bids = bidsRepo.getBidsUser(UsersRepo.currentUser?.id!!)
            if (bids != null) {
                _bidsState.clear()
                _bidsState.addAll(bids)
            }
            _isRefreshing.value = false
        }
    }
}