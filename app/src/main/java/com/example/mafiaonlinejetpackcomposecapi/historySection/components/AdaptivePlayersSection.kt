package com.example.mafiaonlinejetpackcomposecapi.historySection.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AdaptivePlayersSection(
    players: List<String>,
    textColor: Color
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Players:",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = textColor
        )

        when {
            players.isEmpty() -> {
                Text(
                    text = "No players",
                    fontSize = 14.sp,
                    color = textColor.copy(alpha = 0.7f)
                )
            }

            players.size <= 3 -> {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    players.forEachIndexed { idx, playerName ->
                        Text(
                            text = playerName,
                            color = textColor,
                            fontSize = 14.sp,
                            textAlign = if(idx == 0) TextAlign.Start else if(idx == 1) TextAlign.Center else TextAlign.End,
                            modifier = Modifier.width(70.dp),
                            minLines = 1,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            players.size <= 6 -> {
                val rows = players.chunked(3)
                rows.forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        repeat(3) { index ->
                            if (index < row.size) {
                                Text(
                                    text = row[index],
                                    color = textColor,
                                    fontSize = 14.sp,
                                    textAlign = if(index == 0) TextAlign.Start else if(index == 1) TextAlign.Center else TextAlign.End,
                                    modifier = Modifier.width(70.dp),
                                    minLines = 1,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            } else {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }

            players.size <= 9 -> {
                val rows = players.chunked(3)
                rows.forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        repeat(3) { index ->
                            if (index < row.size) {
                                Text(
                                    text = row[index],
                                    color = textColor,
                                    fontSize = 14.sp,
                                    textAlign = if(index == 0) TextAlign.Start else if(index == 1) TextAlign.Center else TextAlign.End,
                                    modifier = Modifier.width(70.dp),
                                    minLines = 1,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            } else {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }

            else -> {
                val playersToShow = players.take(9)
                val remainingCount = players.size - 9

                val rows = playersToShow.chunked(3)
                rows.forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        repeat(3) { index ->
                            if (index < row.size) {
                                Text(
                                    text = row[index],
                                    color = textColor,
                                    fontSize = 14.sp,
                                    textAlign = if(index == 0) TextAlign.Start else if(index == 1) TextAlign.Center else TextAlign.End,
                                    modifier = Modifier.width(70.dp),
                                    minLines = 1,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            } else {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = textColor.copy(alpha = 0.1f),
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) {
                        Text(
                            text = "+$remainingCount more",
                            fontSize = 12.sp,
                            color = textColor.copy(alpha = 0.8f),
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}