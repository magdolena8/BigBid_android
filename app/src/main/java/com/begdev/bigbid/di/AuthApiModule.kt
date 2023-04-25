package com.begdev.bigbid.di

import com.begdev.bigbid.data.api.AuthApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthApiModule {
    @Provides
    @Singleton
    fun provideApi(builder: Retrofit.Builder): AuthApi {
        return builder
            .build()
            .create(AuthApi::class.java)
    }

//    @Provides
//    @Singleton
//    fun provideRetrofit(): Retrofit.Builder{
//        return Retrofit.Builder()
//            .baseUrl(ApiConstants.BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//    }

}