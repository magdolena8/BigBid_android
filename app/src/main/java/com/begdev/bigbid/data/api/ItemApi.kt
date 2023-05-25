package com.begdev.bigbid.data.api

import com.begdev.bigbid.data.api.model.Item
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ItemApi {
    @GET(ApiConstants.CATALOG_END_POINT + "/{userId}")
    suspend fun getItemsCatalog(@Path("userId") userId: Int, @Query("filter") filter:String): List<Item>


    @GET(ApiConstants.OWNER_ITEMS_END_POINT + "/{userId}")
    suspend fun getItemsOwner(@Path("userId") userId: Int): List<Item>

    @GET(ApiConstants.ITEMS_END_POINT + "/{itemId}")
    suspend fun getItem(@Path("itemId") itemId: Int): Response<Item>

    @GET(ApiConstants.LIKED_END_POINT + "/{userId}")
    suspend fun getItemsLiked(@Path("userId") userId: Int): List<Item>

//    @POST(ApiConstants.LIKED_END_POINT + "/{userId}/")
//    suspend fun getLikedIds(@Body itemId: Any, @Path("userId") userId:Int): Item

    @POST(ApiConstants.LIKED_END_POINT + "/{userId}")
    suspend fun likeItem(@Body itemId: Map<String, String>, @Path("userId") userId: Int): Boolean


    @DELETE(ApiConstants.LIKED_END_POINT + "/{userId}")
    suspend fun unlikeItem(@Path("userId") userId: Int, @Query("itemId") itemId: Any): Boolean


    @Multipart
    @POST(ApiConstants.ITEMS_END_POINT + "/{userId}")
    suspend fun createLot(
        @Part image: MultipartBody.Part,
        @Part("title") title: RequestBody,
        @Part("category") category: RequestBody,
        @Part("description") description: RequestBody,
        @Part("startPrice") startPrice: RequestBody,
        @Part("aucDuration") aucDuration: RequestBody,
        @Part("imageUri") imageUri: RequestBody,
        @Path("userId") userId: Int,
    ): Response<Any>

    @Multipart
    @POST(ApiConstants.ITEMS_END_POINT + "{userId}")
    suspend fun uploadImage(
        @Path("userId") userId: Int,
        @Part image: MultipartBody.Part,
    ): Boolean


}