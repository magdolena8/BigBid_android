package com.begdev.bigbid.ui.home

import com.begdev.bigbid.data.api.model.Item

sealed class HomeEvent{
//    object ToggleAuthenticationMode: HomeEvent()

    class ItemClicked(val itemId: Item):
        HomeEvent()


}
