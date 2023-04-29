package com.begdev.bigbid.ui.item

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.begdev.bigbid.data.api.model.Bid
import com.begdev.bigbid.data.repository.BidsRepo
import com.begdev.bigbid.data.repository.ItemsRepo
import com.begdev.bigbid.data.repository.UsersRepo
import com.begdev.bigbid.nav_utils.AppNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class ItemViewModel @Inject constructor(
    private val AppNavigator: AppNavigator,
    private val itemRepo: ItemsRepo,
    private val usersRepo: UsersRepo,
    private val bidsRepo: BidsRepo,

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
                placeBid()
            }

            is ItemEvent.UserBidChanged -> {
                updateUserBid(itemEvent.bid)
            }

            is ItemEvent.AddToFavourite -> {
//                addToFavourite(authenticationEvent.password)
            }

            is ItemEvent.CurrentPriceChanged -> {
//                updatePrice(itemEvent.newPrice)

            }
        }
    }

    private fun updateUserBid(bid: Float) {
        _uiState.value = uiState.value.copy(
            userBid = bid
        )
    }

    private fun placeBid() {
        viewModelScope.launch(Dispatchers.IO) {
            val currentDate = Calendar.getInstance().time
            val formattedDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(currentDate)
            val bid = Bid(
                itemId = _uiState.value.item.id,
                personId = usersRepo.currentUser?.id,
                timeBid = formattedDate,
                price = _uiState.value.userBid!!
            )
            val response = bidsRepo.placeBid(bid, _uiState.value.item.id!!)
            Log.d(TAG, "placeBid: response --> $response")
        }
    }

}