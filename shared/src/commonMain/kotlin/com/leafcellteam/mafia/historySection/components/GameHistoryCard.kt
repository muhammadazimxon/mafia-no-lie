package com.leafcellteam.mafia.historySection.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leafcellteam.mafia.resources.Res
import com.leafcellteam.mafia.resources.wonBy
import com.leafcellteam.mafia.resources.WON
import com.leafcellteam.mafia.resources.LOST
import com.leafcellteam.mafia.resources.min_lowerCase
import com.leafcellteam.mafia.historyService.historyModel.HistoryData
import com.leafcellteam.mafia.resources.timer

@Composable
fun GameHistoryCard(
    game: HistoryData,
    cardBackground: Color,
    accentColor: Color,
    textPrimary: Color,
    textSecondary: Color,
    successColor: Color,
    dangerColor: Color
) {
    val resultColor = if (game.isCurrentPlayerWon) successColor else dangerColor

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        colors = CardDefaults.cardColors(containerColor = cardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = game.gameName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary
                    )

                    Text(
                        text = "${stringResource(Res.string.wonBy)} ${game.wonSide}",
                        fontSize = 14.sp,
                        color = textSecondary
                    )
                }

                Box(
                    modifier = Modifier
                        .background(
                            resultColor.copy(alpha = 0.15f),
                            RoundedCornerShape(8.dp)
                        )
                        .border(1.dp, resultColor.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = if (game.isCurrentPlayerWon) stringResource(Res.string.WON) else stringResource(Res.string.LOST),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = resultColor
                    )
                }
            }

            AdaptivePlayersSection(
                players = game.playerNames,
                textColor = Color.White
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(
                            accentColor.copy(alpha = 0.1f),
                            RoundedCornerShape(20.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.timer),
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "${game.duration} ${stringResource(Res.string.min_lowerCase)}",
                        fontSize = 12.sp,
                        color = textSecondary
                    )
                }

                InfoChip(
                    icon = Icons.Default.DateRange,
                    text = game.date,
                    color = accentColor,
                    textColor = textSecondary
                )
            }
        }
    }
}