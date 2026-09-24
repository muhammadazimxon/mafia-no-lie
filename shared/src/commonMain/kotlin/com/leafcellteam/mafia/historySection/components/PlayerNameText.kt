package com.leafcellteam.mafia.historySection.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Composable
fun PlayerNameText(playerName: String) {
    Text(
        text = playerName,
        fontSize = 14.sp
    )
}

@Preview
@Composable
fun PlayerNameTextPreview() {
    PlayerNameText("Player 1")
}