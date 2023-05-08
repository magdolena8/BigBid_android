package com.begdev.bigbid.ui.item

sealed class ItemEvent {
//    object ToggleAuthenticationMode: ItemEvent()

    class CurrentPriceChanged(val newPrice: Float):
        ItemEvent()

    class UserBidChanged(val bid: Float):
        ItemEvent()

//    class PasswordChanged(val password: String):
//        ItemEvent()

//    class PlaceBid (val newPrice:Float): ItemEvent()
    object PlaceBid: ItemEvent()
    object ItemLikePressed: ItemEvent()
    object ItemUnliked: ItemEvent()
}