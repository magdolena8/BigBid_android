package com.begdev.bigbid.data.api

import com.begdev.bigbid.data.api.model.LoginCredentials
import com.begdev.bigbid.data.api.model.Person
import com.begdev.bigbid.data.api.model.RegisterCredentials
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface AuthApi {

    @POST(ApiConstants.LOGIN_END_POINT)
    suspend fun loginUser(@Body credentials: LoginCredentials): Response<Person>

    @POST(ApiConstants.REGISTER_END_POINT)
    suspend fun registerUser(@Body credentials: RegisterCredentials): Response<Person>

    @Multipart
    @POST(ApiConstants.EDIT_USER_END_POINT+ "/{username}")
    suspend fun editUser(
        @Part image: MultipartBody.Part,
        @Path("username") username: String
    ): Boolean
} 