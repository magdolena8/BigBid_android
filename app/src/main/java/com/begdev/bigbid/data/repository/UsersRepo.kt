package com.begdev.bigbid.data.repository

import android.content.ContentValues.TAG
import android.util.Log
import com.begdev.bigbid.data.api.AuthApi
import com.begdev.bigbid.data.api.model.LoginCredentials
import com.begdev.bigbid.data.api.model.Person
import com.begdev.bigbid.data.api.model.RegisterCredentials
import retrofit2.Response
import javax.inject.Inject

class UsersRepo @Inject constructor(
    private val characterApi: AuthApi,
) {
    private var currentUser: Person? = null

    suspend fun loginUser(credentials: LoginCredentials): Response<Person> {
        val response = characterApi.loginUser(credentials)
        if(response.isSuccessful){
            currentUser = response.body()
            Log.d(TAG, "loginUser: "+currentUser)
        }
        return response
    }

    suspend fun registerUser(credentials:RegisterCredentials): Response<Person> {
        val response = characterApi.registerUser(credentials)
        if(response.isSuccessful){
            currentUser = response.body()
            Log.d(TAG, "registerUser: "+currentUser)
        }
        return response
    }
}