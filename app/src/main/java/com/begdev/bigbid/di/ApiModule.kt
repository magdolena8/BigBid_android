package com.begdev.bigbid.di

import com.begdev.bigbid.data.api.ApiConstants
import com.begdev.bigbid.data.api.AuthApi
import com.begdev.bigbid.data.api.BidsApi
import com.begdev.bigbid.data.api.ItemApi
import com.begdev.bigbid.data.api.UserApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideAuthApi(builder: Retrofit.Builder): AuthApi {
        return builder
            .build()
            .create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideItemApi(builder: Retrofit.Builder): ItemApi {
        return builder
            .build()
            .create(ItemApi::class.java)
    }

    @Provides
    @Singleton
    fun provideUserApi(builder: Retrofit.Builder): UserApi {
        return builder
            .build()
            .create(UserApi::class.java)
    }
    @Provides
    @Singleton
    fun provideBidsApi(builder: Retrofit.Builder): BidsApi {
        return builder
            .build()
            .create(BidsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit.Builder{
        return Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
//            .addConverterFactory(MoshiConverterFactory.create())

    }

}