package com.begdev.bigbid.network

import com.begdev.bigbid.model.Item
import retrofit2.http.GET

interface ItemApi {

    @GET(ApiService.BASE_END_POINT_ITEM)
    suspend fun getHomeItems(): List<Item>
}