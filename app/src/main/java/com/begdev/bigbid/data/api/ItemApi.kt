package com.begdev.bigbid.data.api

import com.begdev.bigbid.data.api.model.Item
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ItemApi {
    @GET(ApiConstants.ITEMS_END_POINT)
    suspend fun getItemsHome(): List<Item>

    @GET(ApiConstants.ITEMS_END_POINT+"/{itemId}")
    suspend fun getItem(@Path("itemId") itemId:Int) :Response<Item>


}