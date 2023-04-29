package com.begdev.bigbid.data.repository

import com.begdev.bigbid.data.api.BidsApi
import com.begdev.bigbid.data.api.model.Bid
import javax.inject.Inject

class BidsRepo @Inject constructor(
    private val bidsApi: BidsApi
) {

    suspend fun placeBid(bid: Bid, itemId: Int): Bid? {
        return try {
            bidsApi.placeBid(bid, itemId).takeIf { it.isSuccessful }?.body()
        } catch (e: Exception) {
            null
        }
    }
}