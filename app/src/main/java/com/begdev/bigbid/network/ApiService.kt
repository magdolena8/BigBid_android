package com.begdev.bigbid.network

interface ApiService {


    companion object ApiConstants {
        const val BASE_URL = "http://localhost:3000/api/"
        const val BASE_END_POINT_ITEM = "items"
    }

//    @Headers("Accept: Application/json")
//    @GET("items")
//    suspend fun fetchItems(): Call?

}