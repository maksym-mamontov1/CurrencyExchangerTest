package com.currency.exchanger.data.entities

data class CurrencyExchangeData(
    val base: String? = null,
    val date: String? = null,
    val rates: Map<String, Double>
)
