package com.begdev.bigbid.network

import android.content.ClipData.Item
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MainRepository
@Inject
constructor(private val apiService: ApiService){
    fun getPost(): Flow<List<Item>> = flow {
        emit(apiService.getPosts())
    }.flowOn(Dispatchers.IO)
}