package com.begdev.bigbid.data.api

import com.begdev.bigbid.data.api.model.LoginCredentials
import com.begdev.bigbid.data.api.model.Person
import com.begdev.bigbid.data.api.model.RegisterCredentials
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST(ApiConstants.LOGIN_END_POINT)
    suspend fun loginUser(@Body credentials:LoginCredentials): Response<Person>

    @POST(ApiConstants.REGISTER_END_POINT)
    suspend fun registerUser(@Body credentials:RegisterCredentials): Response<Person>

} 