package com.begdev.bigbid.ui.item

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.begdev.bigbid.data.api.model.Bid
import com.begdev.bigbid.data.repository.BidsRepo
import com.begdev.bigbid.data.repository.ItemsRepo
import com.begdev.bigbid.data.repository.UsersRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
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
) : ViewModel() {

    val uiState = MutableStateFlow(ItemUiState())
//    val uiState: StateFlow<ItemUiState> = _uiState.asStateFlow()
//    val uiState1 = MutableStateFlow(ItemUiState())

    init {
        viewModelScope.launch {
            val itemId: String = checkNotNull(savedStateHandle["itemId"])
            val item = itemRepo.getItem(itemId.toInt())
            uiState.value = ItemUiState(mutableStateOf(item!!))
//            uiState.value.copy(
//                item.isLiked = true
//            )
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

            else -> {}
        }
    }

    private fun changeLikeState() {
        viewModelScope.launch(Dispatchers.IO) {
            val response: Boolean? = if(uiState.value.item.value.isLiked != true){
                itemRepo.likeItem(uiState.value.item.value.id!!, UsersRepo.currentUser?.id!!)
            } else{
                itemRepo.unlikeItem(uiState.value.item.value.id!!, UsersRepo.currentUser?.id!!)
            }
            if (response != null) {
//                uiState.value.item.value = uiState
                uiState.value.item.value.isLiked = !uiState.value.item.value.isLiked
            }
        }
    }

    private fun unlikeItem() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = itemRepo.unlikeItem(uiState.value.item.value.id!!, UsersRepo.currentUser?.id!!)
            if (response != null) {
//                uiState.value.item.value.copy(
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