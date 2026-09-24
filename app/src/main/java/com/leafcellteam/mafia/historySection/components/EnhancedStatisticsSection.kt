package com.leafcellteam.mafia.historySection.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leafcellteam.mafia.R
import com.leafcellteam.mafia.historyService.historyModel.PlayerData

@Composable
fun EnhancedStatisticsSection(
    stats: PlayerData,
    accentColor: Color,
    cardBackground: Color,
    textPrimary: Color,
    textSecondary: Color,
    successColor: Color,
    dangerColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = cardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = stringResource(R.string.statisticsOverview),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = textPrimary,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatCard(
                    title = stringResource(R.string.games),
                    value = stats.allPlayedGames.toString(),
                    icon = R.drawable.stadia_controller_24px,
                    color = accentColor,
                    textPrimary = textPrimary,
                    textSecondary = textSecondary
                )

                StatCard(
                    title = stringResource(R.string.wins),
                    value = stats.wonGames.toString(),
                    icon = R.drawable.reward_cubok_24px,
                    color = successColor,
                    textPrimary = textPrimary,
                    textSecondary = textSecondary
                )

                StatCard(
                    title = stringResource(R.string.winRate),
                    value = "${stats.winRate}%",
                    icon = R.drawable.bar_chart_24px,
                    color = if (stats.allPlayedGames > 0 && stats.winRate > 50) successColor else dangerColor,
                    textPrimary = textPrimary,
                    textSecondary = textSecondary
                )
            }
        }
    }
}