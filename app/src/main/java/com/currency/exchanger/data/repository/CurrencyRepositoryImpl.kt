package com.currency.exchanger.data.repository

import com.currency.exchanger.data.datasource.local.dao.BalanceDao
import com.currency.exchanger.data.datasource.local.entity.BalanceData
import com.currency.exchanger.data.datasource.local.preferences.TransactionPreferences
import com.currency.exchanger.data.datasource.network.AppApiService
import com.currency.exchanger.data.mapper.BalanceDataToDomainMapper
import com.currency.exchanger.data.mapper.CurrencyExchangeDataToDomainMapper
import com.currency.exchanger.mvvm.entities.Currency
import com.currency.exchanger.mvvm.entities.CurrencyRate
import com.currency.exchanger.mvvm.repository.CurrencyRepository
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    private val appApiService: AppApiService,
    private val balanceDao: BalanceDao,
    private val currencyExchangeMapper: CurrencyExchangeDataToDomainMapper,
    private val balanceMapper: BalanceDataToDomainMapper,
    private val transactionPreferences: TransactionPreferences,
) : CurrencyRepository {

    override suspend fun getCurrencyExchangeRates(base: String): List<CurrencyRate> = appApiService
        .getCurrencyExchangeRates(base)
        .run(currencyExchangeMapper::toDomain)

    override suspend fun getCurrencyRate(
        base: String,
        exchangeCurrencyCode: String
    ): CurrencyRate? = appApiService
        .getCurrencyExchangeRates(base)
        .run(currencyExchangeMapper::toDomain)
        .find { it.name == exchangeCurrencyCode }

    override suspend fun setUpFirstCurrencyBalance() {
        val isTableEmpty = balanceDao.getCurrenciesCount() == 0

        if (isTableEmpty) {
            val currencyList = appApiService.getCurrencyExchangeRates("EUR").rates.keys
            buildList {
                addAll(currencyList.map {
                    val balance = if (it == "EUR") 1000.0 else 0.0
                    BalanceData(currencyName = it, balance = balance)
                })
            }.also {
                balanceDao.insertAll(it)
            }
        }
    }

    override suspend fun getCurrencyNames(): List<String> = balanceDao.getCurrenciesNames()

    override suspend fun makeCurrencyExchange(
        currencyFrom: Currency,
        currencyTo: Currency,
        isFee: Boolean,
    ): List<Currency> {
        val currencyFromCount: Double = if (isFee) {
            val commission = currencyFrom.count * COMMISSION_FEE
            currencyFrom.count + commission
        } else currencyFrom.count

        balanceDao.minusToBalance(currencyFrom.name, currencyFromCount)
        balanceDao.plusToBalance(currencyTo.name, currencyTo.count)

        transactionPreferences.addTransaction()

        return balanceDao.getBalance().map(balanceMapper::toDomain)
    }

    override fun getTransactionCount() = transactionPreferences.getTransactionCount()

    override suspend fun getBalance(): List<Currency> =
        balanceDao.getBalance().map(balanceMapper::toDomain)

    companion object {
        const val COMMISSION_FEE = 0.007
    }
}