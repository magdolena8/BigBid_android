package com.begdev.bigbid.data.repository

import com.begdev.bigbid.data.api.ItemApi
import com.begdev.bigbid.data.api.model.Item
import javax.inject.Inject

class ItemsRepo @Inject constructor(
    private val itemsApi: ItemApi
) {
    suspend fun getItemsMarket(): List<Item> {
        return itemsApi.getItemsHome()
    }

    suspend fun getItem(itemId: Int): Item? {
        return try {
            itemsApi.getItem(itemId).takeIf { it.isSuccessful }?.body()
        } catch (e: Exception) {
            null
        }
    }
}