package com.currency.exchanger.data.mapper

import com.currency.exchanger.data.entities.CurrencyExchangeData
import com.currency.exchanger.mvvm.entities.CurrencyRate
import javax.inject.Inject

class CurrencyExchangeDataToDomainMapper @Inject constructor() {

    fun toDomain(currencyExchangeData: CurrencyExchangeData) = buildList {
        currencyExchangeData.rates.forEach {
            add(CurrencyRate(it.key, it.value))
        }
    }
}