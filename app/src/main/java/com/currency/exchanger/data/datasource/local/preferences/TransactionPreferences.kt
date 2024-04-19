package com.currency.exchanger.data.datasource.local.preferences

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TransactionPreferences @Inject constructor(@ApplicationContext context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun addTransaction() {
        val currentTransaction = prefs.getInt(KEY_TRANSACTION_COUNT, 0)
        prefs.edit().putInt(KEY_TRANSACTION_COUNT, currentTransaction + 1).apply()
    }

    fun getTransactionCount(): Int {
        return prefs.getInt(KEY_TRANSACTION_COUNT, 0)
    }

    companion object {
        private const val PREF_NAME = "transaction_prefs"
        private const val KEY_TRANSACTION_COUNT = "transaction_count"
    }
}