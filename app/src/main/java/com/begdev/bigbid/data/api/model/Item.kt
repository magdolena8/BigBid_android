package com.begdev.bigbid.data.api.model

import com.google.gson.annotations.SerializedName

data class Item(
    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("descript")
    val description: String? = null,

    @field:SerializedName("photo")
    val photo: String? = null,

    @field:SerializedName("current_price")
    val currentBid: Float

) {

}
