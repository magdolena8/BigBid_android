package com.begdev.bigbid.di

import com.begdev.bigbid.nav_utils.AppNavigator
import com.begdev.bigbid.nav_utils.AppNavigatorImpl
import com.begdev.bigbid.network.ApiService
import com.squareup.moshi.Moshi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AppModule {
    @Singleton
    @Binds
    fun bindAppNavigator(appNavigatorImpl: AppNavigatorImpl): AppNavigator

    @Provides
    @Singleton fun providesApiService(moshi: Moshi): ApiService =
        Retrofit.Builder()
            .run {
                baseUrl(ApiService.BASE_URL)
                    .addConverterFactory(MoshiConverterFactory.create(moshi))
                    .build()
            }.create(ApiService::class.java)


}