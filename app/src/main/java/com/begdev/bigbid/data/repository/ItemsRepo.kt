package com.begdev.bigbid.data.repository

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import com.begdev.bigbid.data.api.ItemApi
import com.begdev.bigbid.data.api.model.Item
import java.io.IOException
import javax.inject.Inject

class ItemsRepo @Inject constructor(
    private val itemsApi: ItemApi
) {
    suspend fun getItemsHome(): List<Item> {
        return itemsApi.getItemsHome()
    }

    suspend fun getItem(itemId: Int): Item? {
        try {
            val response = itemsApi.getItem(itemId)

            if (response.isSuccessful) {
                Log.d(ContentValues.TAG, "getItem: " + response.body())
            } else throw IOException()
            return response.body()
        } catch (exception: Exception) {
            Log.d(TAG, "getItem: Exception" + exception.message)
        }
//        return response.errorBody()
        return null
//        return itemsApi.getItem(itemId)
    }

}