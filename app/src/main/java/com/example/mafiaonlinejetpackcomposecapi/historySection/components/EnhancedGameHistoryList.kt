package com.example.mafiaonlinejetpackcomposecapi.historySection.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mafiaonlinejetpackcomposecapi.historyService.historyModel.HistoryData

@Composable
fun EnhancedGameHistoryList(
    gameHistory: List<HistoryData>,
    cardBackground: Color,
    accentColor: Color,
    textPrimary: Color,
    textSecondary: Color,
    successColor: Color,
    dangerColor: Color
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(gameHistory, key = { it.gameId }) { game ->
            GameHistoryCard(
                game = game,
                cardBackground = cardBackground,
                accentColor = accentColor,
                textPrimary = textPrimary,
                textSecondary = textSecondary,
                successColor = successColor,
                dangerColor = dangerColor
            )
        }
    }
}