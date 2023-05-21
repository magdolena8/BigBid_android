package com.begdev.bigbid.data.repository

import com.begdev.bigbid.data.DBHandlerLocal
import com.begdev.bigbid.data.api.BidsApi
import com.begdev.bigbid.data.api.model.Bid
import com.begdev.bigbid.utils.ConnectivityChecker
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class BidsRepo @Inject constructor(
    private val bidsApi: BidsApi,
    private val localDB: DBHandlerLocal,
    private val connectivityChecker: ConnectivityChecker

) {
    val isOnline: StateFlow<Boolean> = connectivityChecker.isOnline

    suspend fun getWinnerEmail(itemId: Int): String? {
        return try {
            if (isOnline.value) {
                val result = bidsApi.getWinnerEmail(itemId) ?: ""
                return result
            } else {
                return ""
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun placeBid(bid: Bid, itemId: Int): Bid? {
        return try {
            val result = bidsApi.placeBid(bid, itemId).takeIf { it.isSuccessful }?.body()
            if (result != null) {
                localDB.saveBidLocal(result)
            }
            return result
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getBidsUser(userId: Int): List<Bid>? {
        return try {
            if (isOnline.value) {
                val result = bidsApi.getBidsUser(userId)
                localDB.saveBidsListLocal(result, username = UsersRepo.currentUser?.username)
                return bidsApi.getBidsUser(userId)
            } else localDB.getUserBidsLocal()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getItemBids(itemId: Int): List<Bid>? {
        return try {
            if (isOnline.value) {
                val result = bidsApi.getBidsItem(itemId)
                localDB.saveBidsListLocal(result)
                return result
            } else localDB.getItemBidsLocalList(itemId)
        } catch (e: Exception) {
            null
        }
    }
}