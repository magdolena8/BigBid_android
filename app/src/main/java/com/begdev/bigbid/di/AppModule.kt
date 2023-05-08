package com.begdev.bigbid.di

import android.content.Context
import com.begdev.bigbid.data.DBHandlerLocal
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

}