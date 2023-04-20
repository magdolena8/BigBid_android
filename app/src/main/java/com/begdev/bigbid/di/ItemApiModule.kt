package com.begdev.bigbid.di

import com.begdev.bigbid.model.ApiConstants
import com.begdev.bigbid.network.ItemApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ItemApiModule {
    @Provides
    @Singleton
    fun provideApi(builder:Retrofit.Builder): ItemApi{
        return builder.build().create(ItemApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit.Builder{
        return Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
    }
}