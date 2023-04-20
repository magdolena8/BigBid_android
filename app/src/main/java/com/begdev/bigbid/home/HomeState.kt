package com.begdev.bigbid.home

import com.begdev.bigbid.model.Item

data class HomeState (
    val userId: String = "",
    val items: List<Item> = emptyList()
)
{

}


enum class UsePersonRole {
    USER, ADMIN
}