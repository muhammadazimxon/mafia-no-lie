package com.example.mafiaonlinejetpackcomposecapi.historySection.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mafiaonlinejetpackcomposecapi.R
import com.example.mafiaonlinejetpackcomposecapi.historyService.historyModel.PlayerData


@Composable
fun StatisticsSection(stats: PlayerData, accentColor: Color, cardBackground: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = cardBackground.copy(alpha = 0.9f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                text = "Overall Statistics",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    icon = R.drawable.stadia_controller_24px,
                    value = stats.allPlayedGames.toString(),
                    label = "All played games",
                    color = accentColor
                )

                StatItem(
                    icon = R.drawable.reward_cubok_24px,
                    value = stats.wonGames.toString(),
                    label = "Win rate: ${stats.winRate}%",
                    color = Color(0xFF32334D)
                )
            }
        }
    }
}


@Preview
@Composable
fun StatisticsSectionPreview() {
    StatisticsSection(
        cardBackground = Color.Red,
        accentColor = Color.Green,
        stats = PlayerData(
            allPlayedGames = 0,
            wonGames = 0,
            winRate = 0
        )
    )
}