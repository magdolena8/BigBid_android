package com.begdev.bigbid.ui.liked

import com.begdev.bigbid.data.api.model.Item

sealed class LikedEvent {
    class LikedItemClick(val item: Item) :
        LikedEvent()
}