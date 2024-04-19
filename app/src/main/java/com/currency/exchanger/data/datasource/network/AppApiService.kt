package com.currency.exchanger.data.datasource.network

import com.currency.exchanger.data.entities.CurrencyExchangeData
import retrofit2.http.GET
import retrofit2.http.Query

interface AppApiService {

    @GET("currency-exchange-rates")
    suspend fun getCurrencyExchangeRates(@Query("base") base: String) : CurrencyExchangeData
}