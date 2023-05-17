package com.begdev.bigbid.data.api.model

import com.begdev.bigbid.data.repository.ItemsRepo
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
    val currentPrice: Float = 0.0f,

    @field:SerializedName("owner_id")
    val ownerId: Int? = null,

    @field:SerializedName("category")
    val category: String? = null,

    @field:SerializedName("bidding_cond")
    val biddingCondition: String? = null,

    @field:SerializedName("bids_total")
    val bidsTotalCount: String? = null,

    @field:SerializedName("auc_end_time")
    val auctionEndTime: String? = null,

    ) {
    var isLiked: Boolean = false
        get() = ItemsRepo.likedItemsIds.contains(id)
        set(state: Boolean){
            field = state
        }
}
