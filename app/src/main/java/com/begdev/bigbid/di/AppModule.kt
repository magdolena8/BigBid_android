package com.begdev.bigbid.di

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import com.begdev.bigbid.data.DBHandlerLocal
import com.begdev.bigbid.utils.ConnectivityChecker
import com.begdev.bigbid.utils.NetworkUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideDBHandlerLocal(@ApplicationContext context: Context): DBHandlerLocal {
        return DBHandlerLocal(context)
    }
    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("auctionPrefs", Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun provideNetworkUtils(connectivityManager: ConnectivityManager): NetworkUtils {
        return NetworkUtils(connectivityManager)
    }

    @Provides
    fun provideConnectivityManager(@ApplicationContext context: Context): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
    @Provides
    fun provideConnectivityChecker(
        connectivityManager: ConnectivityManager,
        @ApplicationContext context: Context
    ): ConnectivityChecker = ConnectivityChecker(connectivityManager, context)


}