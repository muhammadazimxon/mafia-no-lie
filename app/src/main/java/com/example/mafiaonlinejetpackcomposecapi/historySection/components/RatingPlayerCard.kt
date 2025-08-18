package com.example.mafiaonlinejetpackcomposecapi.historySection.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mafiaonlinejetpackcomposecapi.historyService.historyModel.RatingPlayerData

@Composable
fun RatingPlayerCard(
    player: RatingPlayerData,
    position: Int,
    cardBackground: Color,
    accentColor: Color,
    textPrimary: Color,
    textSecondary: Color,
    successColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = cardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            when (position) {
                                1 -> Color(0xFFFFD700)
                                2 -> Color(0xFFC0C0C0)
                                3 -> Color(0xFFCD7F32)
                                else -> accentColor
                            }.copy(alpha = 0.2f),
                            CircleShape
                        )
                        .border(
                            1.dp,
                            when (position) {
                                1 -> Color(0xFFFFD700)
                                2 -> Color(0xFFC0C0C0)
                                3 -> Color(0xFFCD7F32)
                                else -> accentColor
                            }.copy(alpha = 0.5f),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = position.toString(),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = player.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = textPrimary
                    )
                }
            }

            Text(
                text = "${player.winRate.toInt()}%",
                fontSize = 16.sp,
                color = successColor,
                fontWeight = FontWeight.Medium
            )
        }
    }
}