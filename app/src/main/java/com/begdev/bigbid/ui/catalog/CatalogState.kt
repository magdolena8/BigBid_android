package com.begdev.bigbid.ui.catalog

data class HomeState (
    val userId: String = "",

)
{

}


enum class PersonRole {
    USER, ADMIN
}