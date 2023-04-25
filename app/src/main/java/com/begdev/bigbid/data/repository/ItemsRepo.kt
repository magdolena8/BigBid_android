package com.begdev.bigbid.data.repository

import com.begdev.bigbid.data.api.ItemApi
import com.begdev.bigbid.data.api.model.Item
import javax.inject.Inject

class ItemsRepo @Inject constructor(
    private val characterApi: ItemApi
) {
    suspend fun getItems(): List<Item> {
        return characterApi.getItems()
    }
}