package com.begdev.bigbid.ui.profile

import com.begdev.bigbid.data.api.model.Person
import com.begdev.bigbid.data.repository.UsersRepo


data class ProfileUiState(
    val person: Person? = UsersRepo.currentUser,
//    val isFavourite: Boolean = false,
//    val userBid: Float? = item.currentBid + 1,

    )