package com.currency.exchanger.ui.state

import com.currency.exchanger.mvvm.entities.Currency

data class MainScreenState(
    val balanceList: List<Currency> = emptyList(),
    val currencyCellValue: String? = null,
    val currencyCellName: String? = null,
    val currencyReceiveValue: String? = null,
    val currencyReceiveName: String? = null,
    val currencyNameList: List<String> = emptyList(),
    val dialogText: String? = null,
)