package com.begdev.bigbid.data.api.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.begdev.bigbid.data.repository.ItemsRepo
import com.google.gson.annotations.SerializedName
import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit

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

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatEndTime(): String {
        return if (auctionEndTime != null) formatTime(auctionEndTime) else ""
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun formatTime(inputString: String): String {
        val instant = Instant.parse(inputString)
        val now = Instant.now()
        val duration = Duration.between(now, instant)
        val days = ChronoUnit.DAYS.between(now, instant)
        val months = days / 30
        val hours = duration.toHours()
        val minutes = duration.toMinutes()
        val seconds = duration.seconds

        return if (instant.isAfter(now)) {
            if (months > 0) {
                "$months months left"
            } else if (days > 0) {
                "$days days left"
            } else if (hours > 0) {
                "$hours hours left"
            } else if (minutes > 0) {
                "$minutes minutes left"
            } else {
                "$seconds just now"
            }
        } else {
            if (months < 0) {
                "${-months} months ago"
            } else if (days < 0) {
                "${-days} days ago"
            } else if (hours < 0) {
                "${-hours} hours ago"
            } else if (minutes < 0) {
                "${-minutes} minutes ago"
            } else {
                "${-seconds} just now"
            }
        }
    }
}
