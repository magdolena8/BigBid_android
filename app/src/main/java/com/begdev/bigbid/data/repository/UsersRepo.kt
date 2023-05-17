package com.begdev.bigbid.data.repository

import android.content.ContentValues.TAG
import android.content.SharedPreferences
import android.util.Log
import com.begdev.bigbid.data.DBHandlerLocal
import com.begdev.bigbid.data.api.AuthApi
import com.begdev.bigbid.data.api.model.LoginCredentials
import com.begdev.bigbid.data.api.model.Person
import com.begdev.bigbid.data.api.model.RegisterCredentials
import retrofit2.Response
import javax.inject.Inject

class UsersRepo @Inject constructor(
    private val characterApi: AuthApi,
    private val sharedPrefs: SharedPreferences,
    private val localDBHandler: DBHandlerLocal
) {
    companion object {
        var currentUser: Person? = Person()
    }

    suspend fun loginUser(credentials: LoginCredentials): Response<Person> {
        val response = characterApi.loginUser(credentials)
        if (response.isSuccessful) {
            currentUser = response.body()
            saveCredentials(credentials.login.toString(), credentials.passwordHash.toString())
            localDBHandler.saveUserDataLocal(
                username = response.body()?.username.toString(),
                email = response.body()?.email.toString(),
                passwordHash = credentials.passwordHash.toString(),
                userId = response.body()?.id!!,
                accountType = response.body()?.accountType.toString()
            )
            Log.d(TAG, "loginUser: " + currentUser)
        }
        return response
    }

    suspend fun registerUser(credentials: RegisterCredentials): Response<Person> {
        val response = characterApi.registerUser(credentials)
        if (response.isSuccessful) {
            currentUser = response.body()
            saveCredentials(credentials.email.toString(), credentials.passwordHash.toString())
            Log.d(TAG, "registerUser: " + currentUser)
        }
        return response
    }

    fun saveCredentials(login: String, passwordHash: String) {
        val editor = sharedPrefs.edit()
        editor.putString("login", login)
        editor.putString("passwordHash", passwordHash)
        editor.apply()
    }

    fun getSavedCredentials(): LoginCredentials {
        return LoginCredentials(
            login = sharedPrefs.getString("login", ""),
            passwordHash = sharedPrefs.getString("passwordHash", "")
        )
    }
}