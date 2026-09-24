package com.leafcellteam.mafia.historySection.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun MorePlayersIndicator() {
    Row(
        modifier = Modifier.width(150.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "...",
            fontSize = 14.sp,
            color = Color(0xFFB0B0B0)
        )
    }
}

@Preview
@Composable
fun MorePlayersIndicatorPreview() {
    MorePlayersIndicator()
}