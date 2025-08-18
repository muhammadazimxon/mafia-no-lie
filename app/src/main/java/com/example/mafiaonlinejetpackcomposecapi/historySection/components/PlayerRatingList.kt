package com.example.mafiaonlinejetpackcomposecapi.historySection.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mafiaonlinejetpackcomposecapi.historyService.historyModel.PlayerData
import com.example.mafiaonlinejetpackcomposecapi.historyService.historyModel.RatingPlayerData


@Composable
fun PlayerRatingList(ratingPlayers: List<RatingPlayerData>, cardBackground: Color, accentColor: Color) {

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        itemsIndexed(ratingPlayers) { index, (winRate, name) ->
            PlayerRatingItem(
                player = name,
                winRate = winRate,
                rank = index + 1,
                cardBackground = cardBackground,
                accentColor = accentColor
            )
        }
    }
}


@Preview
@Composable
fun PlayerRatingListPreview() {
    PlayerRatingList(
        cardBackground = Color.Red,
        accentColor = Color.Green,
        ratingPlayers = emptyList()
    )
}