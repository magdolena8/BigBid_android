package com.begdev.bigbid.data.repository

import com.begdev.bigbid.data.api.ItemApi
import com.begdev.bigbid.data.api.model.Item
import javax.inject.Inject

class ItemsRepo @Inject constructor(
    private val characterApi: ItemApi
) {
    suspend fun getItems(): List<Item> {
//        val result = characterApi.getItems()
//        Log.d(TAG, "getItems: $result")
        return characterApi.getItems()
//        return characterApi.getItems()
    }
}