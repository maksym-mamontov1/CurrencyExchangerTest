package com.currency.exchanger.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.currency.exchanger.data.datasource.local.entity.BalanceData

@Dao
interface BalanceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(currencies: List<BalanceData>)

    @Query("SELECT * FROM balance")
    fun getBalance(): List<BalanceData>

    @Query("UPDATE balance SET currencyBalance = currencyBalance + :newBalance WHERE currencyName = :currencyNameToUpdate")
    suspend fun plusToBalance(currencyNameToUpdate: String, newBalance: Double)

    @Query("UPDATE balance SET currencyBalance = currencyBalance - :newBalance WHERE currencyName = :currencyNameToUpdate")
    suspend fun minusToBalance(currencyNameToUpdate: String, newBalance: Double)

    @Query("SELECT COUNT(*) FROM balance")
    suspend fun getCurrenciesCount(): Int

    @Query("SELECT currencyName FROM balance")
    suspend fun getCurrenciesNames(): List<String>
}