package com.begdev.bigbid.ui.add_item

import android.graphics.Bitmap
import android.net.Uri

data class AddItemUiState(
    val title: String = "",
    val category: String = "",
    val description: String = "",
    val startPrice: Float = 0f,
    val aucDuration: Int = 1,
    val imageUri: Uri? = null,
    val bitmap: Bitmap? = null,
    ) {

}