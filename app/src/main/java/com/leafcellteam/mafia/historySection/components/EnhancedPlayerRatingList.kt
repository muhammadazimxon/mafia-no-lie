package com.leafcellteam.mafia.historySection.components

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
import com.leafcellteam.shared.history.RatingPlayerData

@Composable
fun EnhancedPlayerRatingList(
    ratingPlayers: List<RatingPlayerData>,
    cardBackground: Color,
    accentColor: Color,
    textPrimary: Color,
    textSecondary: Color,
    successColor: Color
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(ratingPlayers.take(10)) { player ->
            RatingPlayerCard(
                player = player,
                position = ratingPlayers.indexOf(player) + 1,
                cardBackground = cardBackground,
                accentColor = accentColor,
                textPrimary = textPrimary,
                textSecondary = textSecondary,
                successColor = successColor
            )
        }
    }
}