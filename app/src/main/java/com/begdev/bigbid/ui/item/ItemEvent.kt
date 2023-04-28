package com.begdev.bigbid.ui.item

sealed class ItemEvent {
//    object ToggleAuthenticationMode: ItemEvent()

    class CurrentPriceChanged(val newPrice: Float):
        ItemEvent()

//    class UsernameChanged(val username: String):
//        ItemEvent()

//    class PasswordChanged(val password: String):
//        ItemEvent()

    class PlaceBid (val newPrice:Float): ItemEvent()
    object AddToFavourite: ItemEvent()
}