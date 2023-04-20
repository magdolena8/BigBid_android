package com.begdev.bigbid.data.api

import com.begdev.bigbid.data.api.model.Item
import retrofit2.http.GET

interface ItemApi {
    @GET(ApiConstants.ITEMS_END_POINT)
    suspend fun getItems(): List<Item>
}