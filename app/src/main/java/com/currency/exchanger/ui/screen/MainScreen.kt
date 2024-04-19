package com.currency.exchanger.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.currency.exchanger.R
import com.currency.exchanger.mvvm.entities.Currency
import com.currency.exchanger.mvvm.viewmodel.MainViewModel
import com.currency.exchanger.ui.component.Dialog
import com.currency.exchanger.ui.state.MainScreenState
import com.currency.exchanger.utils.extension.validateCurrency

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    state: MainScreenState,
) {
    Column(
        modifier = Modifier.padding(horizontal = 15.dp)
    ) {

        state.dialogText?.let {
            Dialog(text = it) {
                viewModel.onDialogDismiss()
            }
        }

        BalancerComponent(
            state.balanceList,
            Modifier.padding(vertical = 15.dp)
        )

        CurrencyExchangeComponent(
            sellCurrency = state.currencyCellValue ?: "",
            sellCurrencyName = state.currencyCellName ?: "",
            onSellCurrencyChange = viewModel::onSellCurrencyChange,
            receiveCurrency = state.currencyReceiveValue ?: "",
            receiveCurrencyName = state.currencyReceiveName ?: "",
            onReceiveCurrencyChange = viewModel::onReceiveCurrencyChange,
            currencyListName = state.currencyNameList,
            onSelectedCurrency = viewModel::onSelectedCurrency,
            onCurrencyChoosing = viewModel::getCurrencyNames,
            modifier = Modifier.padding(bottom = 15.dp)
        )

        Spacer(modifier = Modifier.weight(1f, true))

        Button(
            onClick = viewModel::onSubmitClick,
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
        ) {
            Text(text = "SUBMIT")
        }
    }
}

@Composable
private fun BalancerComponent(
    balancerList: List<Currency>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(text = "My balance")
        LazyRow(modifier = Modifier.padding(top = 20.dp)) {
            items(balancerList) { CurrencyItem(currency = it) }
        }
    }
}

@Composable
private fun CurrencyItem(currency: Currency) {
    Row(modifier = Modifier.padding(end = 30.dp)) {
        Text(text = currency.count.toString().validateCurrency())
        Text(text = currency.name)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CurrencyExchangeComponent(
    sellCurrency: String,
    sellCurrencyName: String,
    onSellCurrencyChange: (String) -> Unit,
    receiveCurrency: String,
    receiveCurrencyName: String,
    onReceiveCurrencyChange: (String) -> Unit,
    currencyListName: List<String>,
    onSelectedCurrency: (String) -> Unit,
    onCurrencyChoosing: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        Text(text = "Currency Exchange")

        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(top = 10.dp)
        ) {
            CurrencyExchangeField(
                isSell = true,
                currency = sellCurrency,
                currencyName = sellCurrencyName,
                onCurrencyChange = onSellCurrencyChange,
                onSelectedCurrency = onSelectedCurrency,
                currencyListName = currencyListName,
                onCurrencyChoosing = onCurrencyChoosing,
                modifier = Modifier.fillMaxWidth()
            )
            Divider(
                Modifier.padding(start = 60.dp)
            )
            CurrencyExchangeField(
                isSell = false,
                currency = receiveCurrency,
                currencyName = receiveCurrencyName,
                onCurrencyChange = onReceiveCurrencyChange,
                currencyListName = currencyListName,
                onSelectedCurrency = onSelectedCurrency,
                onCurrencyChoosing = onCurrencyChoosing,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun CurrencyExchangeField(
    modifier: Modifier = Modifier,
    isSell: Boolean = true,
    onCurrencyChange: (String) -> Unit = {},
    currency: String = "",
    currencyName: String = "",
    currencyListName: List<String>,
    onCurrencyChoosing: () -> Unit,
    onSelectedCurrency: (String) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        Icon(
            painter = painterResource(
                id = if (isSell) R.drawable.ic_currency_sell
                else R.drawable.ic_currency_receive,

                ),
            contentDescription = "",
            tint = Color.Unspecified,
        )
        Text(
            text = if (isSell) "Sell" else "Receive",
            modifier = Modifier.padding(start = 8.dp)
        )
        Spacer(modifier = Modifier.weight(1f))

        BasicTextField(
            value = currency.validateCurrency(),
            onValueChange = { newValue ->
                val newText = newValue
                val newTextWithoutDot = newText.replace(".", "")
                val decimalPart = newText.substringAfter(".", "")

                if (newTextWithoutDot.all { it.isDigit() } &&
                    newText.count { it == '.' } == 1 &&
                    decimalPart.length <= 2
                ) {
                    onCurrencyChange(newValue)
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            singleLine = true,
        )

        var expanded: Boolean by remember { mutableStateOf(false) }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = currencyName)
            IconButton(onClick = {
                expanded = !expanded
                onCurrencyChoosing()
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_down),
                    contentDescription = null
                )
            }
            if (!isSell) {
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    currencyListName.forEach { currencyName ->
                        DropdownMenuItem(text = {
                            Text(
                                text = currencyName
                            )
                        }, onClick = {
                            onSelectedCurrency(currencyName)
                            expanded = false
                        })
                    }
                }
            }
        }
    }
}


