package com.currency.exchanger.di

import com.currency.exchanger.data.repository.CurrencyRepositoryImpl
import com.currency.exchanger.mvvm.repository.CurrencyRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindsCurrencyRepository(repository: CurrencyRepositoryImpl): CurrencyRepository
}