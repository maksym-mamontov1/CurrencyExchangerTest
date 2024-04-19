package com.currency.exchanger.mvvm.repository

import com.currency.exchanger.mvvm.entities.Currency
import com.currency.exchanger.mvvm.entities.CurrencyRate

interface CurrencyRepository {

    suspend fun getCurrencyExchangeRates(base: String): List<CurrencyRate>

    suspend fun getCurrencyRate(
        base: String,
        exchangeCurrencyCode: String,
    ): CurrencyRate?
    suspend fun makeCurrencyExchange(
        currencyFrom: Currency,
        currencyTo: Currency,
        isFee: Boolean = false,
    ): List<Currency>

    suspend fun getCurrencyNames(): List<String>

    suspend fun setUpFirstCurrencyBalance()

    suspend fun getBalance(): List<Currency>

    fun getTransactionCount(): Int
}