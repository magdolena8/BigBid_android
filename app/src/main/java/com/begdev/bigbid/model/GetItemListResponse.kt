package com.begdev.bigbid.model

data class GetItemListResponse(
    val itemlist: List<Item>,
    val success: Long

)
