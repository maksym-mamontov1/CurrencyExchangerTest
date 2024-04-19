package com.currency.exchanger.di

import android.content.Context
import androidx.room.Room
import com.currency.exchanger.data.datasource.local.AppDatabase
import com.currency.exchanger.data.datasource.local.dao.BalanceDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class LocalModule {

    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "app_database"
    ).build()

    @Provides
    fun provideBalanceDao(appDatabase: AppDatabase): BalanceDao = appDatabase.balanceDao()
}