package com.currency.exchanger.utils.extension


fun String.validateCurrency(): String {
    val splitString = split(".")
    if (splitString.size == 1) return "0.00"

    return splitString[0] + "." + splitString[1].take(2)
}