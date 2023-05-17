package com.begdev.bigbid.ui.item

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.begdev.bigbid.data.api.model.Item

data class ItemUiState(
    val item: MutableState<Item> = mutableStateOf(Item(13, "adqwd", "adqwd", "adqwd", 3.2f)),
    val userBid: Float? = item.value.currentPrice + 1,

    )
