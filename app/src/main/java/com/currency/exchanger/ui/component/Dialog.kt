package com.currency.exchanger.ui.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun Dialog(
    text: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = "Currency exchange") },
        confirmButton = {
            Button(
                onClick = {
                    onDismiss()
                }
            ) {
                Text(text = "Ok")
            }
        },
        text = { Text(text = text) }
    )
}