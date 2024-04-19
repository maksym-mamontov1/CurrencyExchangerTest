package com.currency.exchanger.data.mapper

import com.currency.exchanger.data.datasource.local.entity.BalanceData
import com.currency.exchanger.mvvm.entities.Currency
import javax.inject.Inject

class BalanceDataToDomainMapper @Inject constructor() {

    fun toDomain(balanceData: BalanceData) = balanceData.run {
        Currency(
            name = currencyName,
            count = balance
        )
    }
}