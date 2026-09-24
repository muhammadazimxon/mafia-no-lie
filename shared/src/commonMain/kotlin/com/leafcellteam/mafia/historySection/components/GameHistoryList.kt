package com.leafcellteam.mafia.historySection.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leafcellteam.mafia.historyService.historyModel.HistoryData


@Composable
fun GameHistoryList(gameHistory: List<HistoryData>, cardBackground: Color, accentColor: Color) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(gameHistory) { game ->
            GameHistoryItem(game = game, cardBackground = cardBackground, accentColor = accentColor)
        }
    }
}


@Preview
@Composable
fun GameHistoryListPreview() {
    GameHistoryList(
        cardBackground = Color.Red,
        accentColor = Color.Green,
        gameHistory = emptyList(),
    )
}