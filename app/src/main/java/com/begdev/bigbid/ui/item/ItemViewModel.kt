package com.begdev.bigbid.ui.item

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.begdev.bigbid.data.api.model.Bid
import com.begdev.bigbid.data.api.model.Item
import com.begdev.bigbid.data.repository.BidsRepo
import com.begdev.bigbid.data.repository.ItemsRepo
import com.begdev.bigbid.data.repository.UsersRepo
import com.begdev.bigbid.utils.ConnectivityChecker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class ItemViewModel @Inject constructor(
    private val itemRepo: ItemsRepo,
    private val usersRepo: UsersRepo,
    private val bidsRepo: BidsRepo,
    savedStateHandle: SavedStateHandle,
    private val connectivityChecker: ConnectivityChecker,
    private val context: Context

) : ViewModel() {
    val isOnline: StateFlow<Boolean> = connectivityChecker.isOnline
    val uiState = MutableStateFlow(ItemUiState())
    private val _bidsState = mutableStateListOf<Bid>()
    val bidsState: List<Bid> = _bidsState
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing
    private val _winnerEmail = MutableStateFlow("")
    val winnerEmail: StateFlow<String> = _winnerEmail


    init {
        viewModelScope.launch {
            val itemId: String = checkNotNull(savedStateHandle["itemId"])
            val item = itemRepo.getItem(itemId.toInt())
            Log.d(TAG, "item : $item")
            uiState.value = ItemUiState(mutableStateOf(item!!))
//            uiState.value.copy(
//                item.isLiked = true
//            )
            val bids = bidsRepo.getItemBids(itemId.toInt())
            if (bids != null) {
                _bidsState.addAll(bids)
            }
            try {
                getWinnerEmail(item)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun refreshData() {
        viewModelScope.launch {
            _isRefreshing.value = true
            val item = itemRepo.getItem(uiState.value.item.value.id!!)
            uiState.value = ItemUiState(mutableStateOf(item!!))
            val bids = bidsRepo.getItemBids(uiState.value.item.value.id!!)
            if (bids != null) {
                _bidsState.clear()
                _bidsState.addAll(bids)
            }
            _isRefreshing.value = false
            try {
//                getWinnerEmail(item)
            } catch (e: Exception) {
                e.printStackTrace()
            }
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

            is ItemEvent.ItemLikePressed -> {
                changeLikeState()
            }

            is ItemEvent.ItemUnliked -> {
                unlikeItem()
            }


            is ItemEvent.CurrentPriceChanged -> {
//                updatePrice(itemEvent.newPrice)
            }

            is ItemEvent.BuyerPressed -> {
            }
        }
    }

    private fun getWinnerEmail(item: Item) {
        viewModelScope.launch(Dispatchers.IO) {
            if (isOnline.value) {
                try {
                    _winnerEmail.value = bidsRepo.getWinnerEmail(item.id!!) ?: ""
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
//                Toast.makeText(context, "Mail Service unavailable", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun changeLikeState() {
        viewModelScope.launch(Dispatchers.IO) {
            val response: Boolean? = if (uiState.value.item.value.isLiked != true) {
                itemRepo.likeItem(uiState.value.item.value, UsersRepo.currentUser?.id!!)
            } else {
                itemRepo.unlikeItem(uiState.value.item.value, UsersRepo.currentUser?.id!!)
            }
            if (response != null) {
//                uiState.value.item.value = uiState
                uiState.value.item.value.isLiked = uiState.value.item.value.isLiked
            }
        }
    }

    private fun unlikeItem() {
        viewModelScope.launch(Dispatchers.IO) {
            val response =
                itemRepo.unlikeItem(uiState.value.item.value!!, UsersRepo.currentUser?.id!!)
            if (response != null) {
//                uiState.value.item.value.copy(prisma db push
//
//                )
            }
        }
    }

    private fun updateUserBid(bid: Float) {
        uiState.value = uiState.value.copy(
            userBid = bid
        )
    }

    private fun placeBid() {
        viewModelScope.launch(Dispatchers.IO) {
            val currentDate = Calendar.getInstance().time
            val formattedDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(currentDate)
            val bid = Bid(
                itemId = uiState.value.item.value.id,
                personId = UsersRepo.currentUser?.id,
                timeBid = formattedDate,
                price = uiState.value.userBid!!
            )
            val response = bidsRepo.placeBid(bid, uiState.value.item.value.id!!)
            Log.d(TAG, "placeBid: response --> $response")
        }
    }
}