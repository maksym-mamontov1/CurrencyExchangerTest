package com.currency.exchanger.mvvm.viewmodel

import androidx.room.util.copy
import com.currency.exchanger.mvvm.BaseViewModel
import com.currency.exchanger.mvvm.entities.Currency
import com.currency.exchanger.mvvm.repository.CurrencyRepository
import com.currency.exchanger.ui.state.MainScreenState
import com.currency.exchanger.utils.extension.validateCurrency
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val currencyRepository: CurrencyRepository
) : BaseViewModel<MainScreenState>() {

    override fun getInitialState(): MainScreenState = MainScreenState()

    init {
        getBalance()
        getDefaultCurrencyToExchange()
    }

    fun onSellCurrencyChange(currencyValue: String) {
        changeState { copy(currencyCellValue = currencyValue) }
        launch {
            changeState {
                copy(
                    currencyReceiveValue = calculateCurrency(currencyValue.toDouble()).toString()
                )
            }
        }
    }

    private suspend fun calculateCurrency(cellCurrencyValue: Double): Double {
        val currencyCellName = state.currencyCellName ?: return 0.00
        val currencyReceiveName = state.currencyReceiveName ?: return 0.00
        val currencyRate = currencyRepository
            .getCurrencyRate(currencyCellName, currencyReceiveName)
            ?.rates

        return cellCurrencyValue * (currencyRate ?: 0.0)
    }

    private suspend fun calculateCellCurrency(receiveCurrencyValue: Double): Double {
        val currencyCellName = state.currencyCellName ?: return 0.00
        val currencyReceiveName = state.currencyReceiveName ?: return 0.00
        val currencyRate = currencyRepository
            .getCurrencyRate(currencyCellName, currencyReceiveName)
            ?.rates

        return receiveCurrencyValue / (currencyRate ?: 0.0)
    }

    fun onReceiveCurrencyChange(currencyValue: String) {
        changeState { copy(currencyReceiveValue = currencyValue) }
        launch {
            changeState {
                copy(
                    currencyCellValue = calculateCellCurrency(
                        currencyValue.toDouble()
                    ).toString()
                )
            }
        }
    }

    fun onSubmitClick() {
        launch {
            val isCommissionFee = currencyRepository.getTransactionCount() > COUNT_FREE_TRANSACTION

            currencyRepository.makeCurrencyExchange(
                Currency(
                    state.currencyCellName ?: "",
                    state.currencyCellValue?.toDouble() ?: 0.0
                ),
                Currency(
                    state.currencyReceiveName ?: "",
                    state.currencyReceiveValue?.toDouble() ?: 0.0
                ),
                isCommissionFee
            )

            val text = "You have converted ${state.currencyCellValue} ${state.currencyCellName} " +
                    "to ${state.currencyReceiveValue?.validateCurrency()} ${state.currencyReceiveName}" +
                    if (isCommissionFee) " Commission Fee - 0.7%" else ""

            changeState { copy(dialogText = text) }

            getBalance()
        }
    }

    private fun getDefaultCurrencyToExchange() {
        changeState {
            copy(
                currencyCellName = "EUR",
                currencyCellValue = "0.00",
                currencyReceiveName = "USD"
            )
        }
    }

    fun onSelectedCurrency(currencyName: String) {
        changeState {
            copy(
                currencyReceiveName = currencyName,
            )
        }
        launch {
            changeState {
                copy(
                    currencyCellValue = calculateCurrency(
                        state.currencyCellValue?.toDouble() ?: 0.0
                    ).toString()
                )
            }
        }
    }

    fun getCurrencyNames() {
        launch {
            changeState {
                copy(
                    currencyNameList = currencyRepository.getCurrencyNames()
                )
            }
        }
    }

    private fun getBalance() {
        launch {
            currencyRepository.setUpFirstCurrencyBalance()

            val currentBalance = currencyRepository.getBalance().filter {
                it.count != 0.0
            }
            changeState { copy(balanceList = currentBalance) }
        }
    }

    fun onDialogDismiss() {
        changeState { copy(dialogText = null) }
    }

    companion object {
        const val COUNT_FREE_TRANSACTION = 5
    }
}