package com.begdev.bigbid.ui.item

import com.begdev.bigbid.data.api.model.Item

data class ItemUiState(
    val item: Item = Item(12, "adqwd", "adqwd", "adqwd", 3.2f),
    val isFavourite: Boolean = false
)
