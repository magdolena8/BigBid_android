package com.begdev.bigbid.ui.bids

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.begdev.bigbid.data.api.model.Bid
import com.begdev.bigbid.data.repository.BidsRepo
import com.begdev.bigbid.data.repository.UsersRepo
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
    ):ViewModel()
{
    private val _navigationEvent = MutableSharedFlow<String>()
    val navigationEvent: SharedFlow<String> = _navigationEvent

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


    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

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