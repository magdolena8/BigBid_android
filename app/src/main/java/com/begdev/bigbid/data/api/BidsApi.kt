package com.begdev.bigbid.data.api

import com.begdev.bigbid.data.api.model.Bid
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface BidsApi {

    @POST(ApiConstants.BIDS_END_POINT+"/{itemId}")
    @Headers("Content-Type: application/json")
    suspend fun placeBid(@Body bid: Bid, @Path("itemId") itemId:Int): Response<Bid>

//    @GET(ApiConstants.BIDS_END_POINT+"/{itemId}")
////    @Headers("Content-Type: application/json")
//    suspend fun placeBid(@Body bid: Bid, @Path("itemId") itemId:Int): Response<Bid>


    @GET(ApiConstants.BIDS_USER_END_POINT + "/{userId}")
    suspend fun getBidsUser(@Path("userId") userId: Int): List<Bid>

    @GET(ApiConstants.BIDS_ITEM_END_POINT + "/{itemId}")
    suspend fun getBidsItem(@Path("itemId") itemId: Int): List<Bid>
    @GET(ApiConstants.BIDS_WINNER_END_POINT + "/{itemId}")
    suspend fun getWinnerEmail(@Path("itemId") itemId: Int): String?



}