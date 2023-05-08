package com.begdev.bigbid.ui.owner

sealed class OwnerEvent {

    class AddFABClicked() :
        OwnerEvent()

    class TitleChanged(val title: String) :
        OwnerEvent()

    class DescriptionChanged(val description: String) :
        OwnerEvent()

    class CategoryChanged(val category: String) :
        OwnerEvent()

    class PriceChanged(val price: Float) :
        OwnerEvent()

    class DurationChanged(val duration: Int) :
        OwnerEvent()
}
