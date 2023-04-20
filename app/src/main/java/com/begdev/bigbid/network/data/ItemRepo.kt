package com.begdev.bigbid.network.data

import com.begdev.bigbid.model.Item
import com.begdev.bigbid.network.ItemApi
import javax.inject.Inject

class ItemRepo @Inject constructor(
    private val itemApi: ItemApi
) {
    suspend fun getItems(): List<Item>{
        return itemApi.getHomeItems()
    }
}