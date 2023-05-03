package com.begdev.bigbid.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface AppModule {
//    @Singleton
//    @Binds
//    fun bindAppNavigator(appNavigatorImpl: AppNavigatorImpl): AppNavigator

//    @Provides
//    @Singleton
////    @Binds
//    fun provideRetrofit(): Retrofit.Builder{
//        return Retrofit.Builder()
//            .baseUrl(ApiConstants.BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//    }


}