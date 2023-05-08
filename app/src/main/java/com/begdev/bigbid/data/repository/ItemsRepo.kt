package com.begdev.bigbid.data.repository

import android.content.ContentValues.TAG
import android.util.Log
import com.begdev.bigbid.data.DBHandlerLocal
import com.begdev.bigbid.data.api.ItemApi
import com.begdev.bigbid.data.api.model.Item
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(DelicateCoroutinesApi::class)
class ItemsRepo @Inject constructor(
    private val itemsApi: ItemApi,
    private val localDB: DBHandlerLocal
) {
    companion object {
        var likedItemsIds: MutableList<Int> = mutableListOf<Int>()
    }
    init {
        GlobalScope.launch {
//            likedItemsIds = getItemsLiked(UsersRepo.currentUser?.id!!)?.map { it.id!! } as MutableList<Int>?
            if(likedItemsIds.isEmpty()){}
            val result = getItemsLiked(UsersRepo.currentUser?.id!!)?.map { it.id!! } as MutableList<Int>?
            if (result != null) {
                result.forEach {
//                    likedItemsIds?.add(it)
                    localDB.addLikedItem(itemId = it)
                    likedItemsIds.add(it)
                }
//                localDB.getLikedItemsIds()
            }
        }

    }

    suspend fun getItemsCatalog(userId: Int): List<Item>? {
//        return itemsApi.getItemsCatalog(userId)

        return try {
            return itemsApi.getItemsCatalog(userId)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getItemsOwner(userId: Int): List<Item>? {
        return try {
            return itemsApi.getItemsOwner(userId)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getItem(itemId: Int): Item? {
        return try {
            itemsApi.getItem(itemId).takeIf { it.isSuccessful }?.body()
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getItemsLiked(userId: Int): List<Item>? {
        return try {
            return itemsApi.getItemsLiked(userId)
        } catch (e: Exception) {
            null
        }
    }

    //    suspend fun getLikedIds(userId: Int): List<Int>? {
//        return try {
//            return itemsApi.getLikedIds(userId)
//        } catch (e: Exception) {
//            null
//        }
//    }
    suspend fun likeItem(itemId: Int, userId: Int): Boolean? {
        return try {
            val result = itemsApi.likeItem(mapOf("itemId" to itemId.toString()), userId)
            if(result == true){
                likedItemsIds?.add(itemId)
                }
            return result

//            return itemsApi.likeItem(mapOf("itemId" to itemId.toString()), userId)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun unlikeItem(itemId: Int, userId: Int): Boolean? {
        return try {
            val result = itemsApi.unlikeItem(userId, itemId)
            if(result == true){
                likedItemsIds.remove(itemId)
            }
            return result

//            return itemsApi.likeItem(mapOf("itemId" to itemId.toString()), userId)
        } catch (e: Exception) {
            Log.d(TAG, "unlikeItem: "+ e.message)
            null
        }
    }
}