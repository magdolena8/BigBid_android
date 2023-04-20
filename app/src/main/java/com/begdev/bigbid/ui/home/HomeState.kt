package com.begdev.bigbid.ui.home

data class HomeState (
    val userId: String = "",

)
{

}


enum class PersonRole {
    USER, ADMIN
}