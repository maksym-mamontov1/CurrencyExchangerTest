package com.currency.exchanger.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.currency.exchanger.mvvm.viewmodel.MainViewModel
import com.currency.exchanger.ui.component.Header
import com.currency.exchanger.ui.screen.MainScreen
import com.currency.exchanger.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                val viewModel: MainViewModel = hiltViewModel()
                val state by viewModel.stateFlow.collectAsState()

                Column(modifier = Modifier.fillMaxSize()) {
                    Header()
                    state?.let {
                        MainScreen(viewModel, it)
                    }
                }
            }
        }
    }
}