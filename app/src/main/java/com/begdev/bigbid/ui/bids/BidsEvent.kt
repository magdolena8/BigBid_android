package com.begdev.bigbid.ui.bids

import com.begdev.bigbid.data.api.model.Bid

sealed class BidsEvent{
    class BidClicked(val bid: Bid):
        BidsEvent()
}