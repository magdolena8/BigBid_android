package com.begdev.bigbid.data.repository

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import com.begdev.bigbid.data.DBHandlerLocal
import com.begdev.bigbid.data.api.ItemApi
import com.begdev.bigbid.data.api.model.Item
import com.begdev.bigbid.utils.ConnectivityChecker
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject


@OptIn(DelicateCoroutinesApi::class)
class ItemsRepo @Inject constructor(
    private val itemsApi: ItemApi,
    private val localDB: DBHandlerLocal,
    private val connectivityChecker: ConnectivityChecker
) {
    val isOnline: StateFlow<Boolean> = connectivityChecker.isOnline

//    companion object {
//        var likedItemsIds: MutableList<Int> by lazy {
//            val list = mutableListOf<Int>()
//
//        }
//    }

    companion object {
        private lateinit var instance: ItemsRepo
        val likedItemsIds: MutableList<Int> by lazy {
            val list = mutableListOf<Int>()
            GlobalScope.launch {
                loadLikedItems(list, instance.localDB, instance.itemsApi, instance.isOnline.value)
            }
            list

        }
    }

    init {
        instance = this
    }

//    init {
//        GlobalScope.launch {
////            likedItemsIds = getItemsLiked(UsersRepo.currentUser?.id!!)?.map { it.id!! } as MutableList<Int>?
//            if (likedItemsIds.isEmpty()) {
//            }
//            val result =
//                getItemsLiked(UsersRepo.currentUser?.id!!)
//            if (result != null) {
//                result.forEach {
////                    likedItemsIds?.add(it)
//                    localDB.addLikedItem(
//                        itemId = it.id!!,
//                        title = it.title,
//                        description = it.description,
//                        category = it.category
//                    )
//                    likedItemsIds.add(it.id)
//                }
//            }
//        }
//    }


    suspend fun createLot(
        userId: Int,
        imageFile: File,
        title: String,
        category: String,
        description: String,
        startPrice: Float,
        aucDuration: Int,
        imageUri: Uri,
    ): Boolean? {
        return try {
            return itemsApi.createLot(
                userId = userId,
                image = MultipartBody.Part.createFormData(
                    "image",
                    imageFile.name,
                    imageFile.asRequestBody()
                ),
                title = title.toRequestBody("text/plain".toMediaTypeOrNull()),
                category = category.toRequestBody("text/plain".toMediaTypeOrNull()),
                description = description.toRequestBody("text/plain".toMediaTypeOrNull()),
                startPrice = startPrice.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                aucDuration = aucDuration.toString()
                    .toRequestBody("text/plain".toMediaTypeOrNull()),
                imageUri = imageUri.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            )
        } catch (e: Exception) {
            null
        }
    }

    suspend fun uploadImage(imageFile: File): Boolean? {
        Log.d(TAG, "uploadImage: " + imageFile.totalSpace)

        return try {
            return itemsApi.uploadImage(
//                image = fbody,
                image = MultipartBody.Part.createFormData(
                    "image",
                    imageFile.name,
                    imageFile.asRequestBody()
                ),
                userId = 1
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d(TAG, "uploadImageERROR: " + e.message)
            null
        }
    }

    suspend fun getItemsCatalog(userId: Int, filter: String): List<Item>? {
        return try {
            return itemsApi.getItemsCatalog(userId, filter)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getItemsOwner(userId: Int): List<Item>? {
        return try {
            if (isOnline.value) {
                val result = itemsApi.getItemsOwner(userId)
                localDB.saveOwnerItems(result)
                return result
            } else localDB.getOwnerItemsList()
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getItem(itemId: Int): Item? {
        return try {
            if (isOnline.value) {
                return itemsApi.getItem(itemId).takeIf { it.isSuccessful }?.body()
            } else return localDB.getOwnerItem(itemId)
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
    suspend fun likeItem(item: Item, userId: Int): Boolean? {
        return try {
            val result = itemsApi.likeItem(mapOf("itemId" to item.id.toString()), userId)
            if (result == true) {
                likedItemsIds?.add(item.id!!)
                localDB.addLikedItem(
                    itemId = item.id!!,
                    title = item.title,
                    description = item.description,
                    category = item.category
                )
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
            if (result == true) {
                likedItemsIds.remove(itemId)
                localDB.unlikeItem(itemId)
            }
            return result

//            return itemsApi.likeItem(mapOf("itemId" to itemId.toString()), userId)
        } catch (e: Exception) {
            Log.d(TAG, "unlikeItem: " + e.message)
            null
        }
    }
}

private suspend fun loadLikedItems(
    list: MutableList<Int>,
    localDB: DBHandlerLocal,
    itemsApi: ItemApi,
    isOnline: Boolean,
) {
    if (isOnline) {
        val result = itemsApi.getItemsLiked(UsersRepo.currentUser?.id!!)
        if (result != null) {
            result.forEach {
                localDB.addLikedItem(
                    itemId = it.id!!,
                    title = it.title,
                    description = it.description,
                    category = it.category
                )
                list.add(it.id)
            }
        }
    }
    else{
        localDB.getLocalLikedItems()
    }
}