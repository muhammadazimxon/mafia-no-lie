package com.example.mafiaonlinejetpackcomposecapi.historySection.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mafiaonlinejetpackcomposecapi.historyService.historyModel.HistoryData
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun GameHistoryItem(game: HistoryData, cardBackground: Color, accentColor: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBackground.copy(alpha = 0.9f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Game: ${game.gameName}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(game.date),
                    fontSize = 14.sp,
                    color = Color(0xFFB0B0B0)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(Color(0xFFFFD700), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "👑",
                                fontSize = 12.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "Winner: ${game.wonSide}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Players:",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    val playersToShow = game.playerNames.take(9)
                    val hasMorePlayers = game.playerNames.size > 9

                    val columnsData = playersToShow.chunked(3)

                    Row(
                        modifier = Modifier.width(150.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        repeat(3) { columnIndex ->
                            Column(
                                modifier = Modifier.weight(1f),
                                horizontalAlignment = when (columnIndex) {
                                    0 -> Alignment.Start
                                    1 -> Alignment.CenterHorizontally
                                    2 -> Alignment.End
                                    else -> Alignment.Start
                                }
                            ) {
                                val playersInColumn =
                                    columnsData.getOrNull(columnIndex) ?: emptyList()

                                playersInColumn.forEach { playerName ->
                                    PlayerNameText(playerName = playerName)
                                }
                            }
                        }
                    }

                    if (hasMorePlayers) {
                        MorePlayersIndicator()
                    }
                }

                Card(
                    modifier = Modifier
                        .height(100.dp)
                        .width(150.dp)
                ) {
                    Text(text = if(game.isCurrentPlayerWon) "WON" else "LOST")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Duration: ${game.duration}",
                fontSize = 14.sp,
                color = Color(0xFFB0B0B0)
            )
        }
    }
}

@Preview
@Composable
fun GameHistoryItemPreview() {
    GameHistoryItem(
        game = HistoryData(
            gameName = "GameName",
            wonSide = "Civilian",
            duration = 80,
            date = "",
            isCurrentPlayerWon = true,
            playerNames = emptyList()
        ),
        cardBackground = Color.Red ,
        accentColor = Color.Green
    )
}