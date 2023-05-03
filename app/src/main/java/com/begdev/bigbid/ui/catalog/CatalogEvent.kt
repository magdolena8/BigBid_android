package com.begdev.bigbid.ui.catalog

import com.begdev.bigbid.data.api.model.Item

sealed class CatalogEvent{
//    object ToggleAuthenticationMode: HomeEvent()

    class ItemClicked(val item: Item):
        CatalogEvent()
}
