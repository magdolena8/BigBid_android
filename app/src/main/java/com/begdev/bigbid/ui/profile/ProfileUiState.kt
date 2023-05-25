package com.begdev.bigbid.ui.profile

import android.graphics.Bitmap
import android.net.Uri
import com.begdev.bigbid.data.api.model.Person
import com.begdev.bigbid.data.repository.UsersRepo


data class ProfileUiState(
    val person: Person? = UsersRepo.currentUser,
//    val isFavourite: Boolean = false,
//    val userBid: Float? = item.currentBid + 1,
    val username: String = "",
    var profileMode: EditMode = EditMode.NO_EDIT,
    val imageUri: Uri? = null,
    val bitmap: Bitmap? = null,

    )

enum class EditMode {
    EDIT, NO_EDIT
}