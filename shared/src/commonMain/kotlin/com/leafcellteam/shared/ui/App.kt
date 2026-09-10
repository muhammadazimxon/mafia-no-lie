package com.leafcellteam.shared.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.leafcellteam.shared.platform

@Composable
fun App() {
    MaterialTheme {
        Text("Hello from Shared UI on ${platform()}!")
    }
}
