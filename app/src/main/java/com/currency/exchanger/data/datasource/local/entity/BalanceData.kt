package com.currency.exchanger.data.datasource.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "balance")
data class BalanceData(
    @PrimaryKey @ColumnInfo(name = "currencyName") val currencyName: String,
    @ColumnInfo(name = "currencyBalance") val balance: Double
)