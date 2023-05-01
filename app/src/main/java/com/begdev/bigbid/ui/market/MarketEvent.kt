package com.begdev.bigbid.ui.market

import com.begdev.bigbid.data.api.model.Item

sealed class MarketEvent{
//    object ToggleAuthenticationMode: HomeEvent()

    class ItemClicked(val itemId: Item):
        MarketEvent()


}
