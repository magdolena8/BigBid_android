package com.begdev.bigbid.data.api.model

import com.google.gson.annotations.SerializedName

data class Bid (
    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("item_id")
    val itemId: Int? = null,

    @field:SerializedName("person_id")
    val personId: Int? = null,

    @field:SerializedName("time_bid")
    val timeBid: String? = null,

    @field:SerializedName("price")
    val price: Float = 0.0f
)