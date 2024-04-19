package com.currency.exchanger.di

import com.currency.exchanger.data.datasource.network.AppApiService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .build()

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideAppApiService(
        okHttpClient: OkHttpClient,
        gson: Gson,
    ): AppApiService = Retrofit
        .Builder()
        .client(okHttpClient)
        .baseUrl("https://developers.paysera.com/tasks/api/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(AppApiService::class.java)

}