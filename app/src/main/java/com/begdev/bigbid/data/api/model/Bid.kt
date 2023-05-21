package com.begdev.bigbid.data.api.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.annotations.SerializedName
import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit

data class Bid (
    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("item_id")
    val itemId: Int? = null,

    @field:SerializedName("person_id")
    val personId: Int? = null,

    @field:SerializedName("bidder_username")
    val bidderUsername: String? = null,

    @field:SerializedName("time_bid")
    val timeBid: String? = null,

    @field:SerializedName("price")
    val price: Float = 0.0f
){
    @RequiresApi(Build.VERSION_CODES.O)
    fun formatTimeBid(): String {
        return if (timeBid != null) formatTime(timeBid) else ""
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
                "just now"
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
                "just now"
            }
        }
    }
}
