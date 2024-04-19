package com.currency.exchanger.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.currency.exchanger.data.datasource.local.dao.BalanceDao
import com.currency.exchanger.data.datasource.local.entity.BalanceData

@Database(entities = [BalanceData::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun balanceDao(): BalanceDao
}