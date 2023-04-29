package com.begdev.bigbid.data.api

import com.begdev.bigbid.data.api.model.Bid
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface BidsApi {

    @POST(ApiConstants.BIDS_END_POINT+"/{itemId}")
    @Headers("Content-Type: application/json")
    suspend fun placeBid(@Body bid: Bid, @Path("itemId") itemId:Int): Response<Bid>

}