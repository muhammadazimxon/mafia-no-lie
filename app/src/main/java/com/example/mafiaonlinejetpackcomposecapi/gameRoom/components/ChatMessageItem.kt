package com.example.mafiaonlinejetpackcomposecapi.gameRoom.components

import android.util.Log
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.gameRoomSignalRClient.Phase
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.gameRoomSignalRClient.Player
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.models.ChatMessageItemParams
import java.util.Date
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun ChatMessageItem(params: ChatMessageItemParams) {
    val uniqueKey = remember { params.timeStamp.time.milliseconds.toString() }
    val snapshotPlayer by remember(uniqueKey) { mutableStateOf(params.player) }
    val snapshotPhase by remember(uniqueKey) { mutableStateOf(params.phase) }

    Log.d("ChatMessageItem", "New message: ${params.message}")
    Log.d("ChatMessageItem", "Sender: ${snapshotPlayer?.playerName ?: "Unknown"}")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 6.dp),
        verticalAlignment = Alignment.Top
    ) {
        snapshotPlayer?.let { itPlayer ->
            Box {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    itPlayer.avatarColor.copy(alpha = 0.8f),
                                    itPlayer.avatarColor
                                ),
                                radius = 50f
                            )
                        )
                        .border(
                            width = 2.dp,
                            color = itPlayer.avatarColor.copy(alpha = 0.3f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = params.onSnapshotTextFirstLetter(snapshotPhase, itPlayer),
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 4.dp)
                ) {
                    Text(
                        text = params.onSnapshotTextName(snapshotPhase, itPlayer),
                        color = Color(0xFF7B68EE),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )

                    itPlayer.playerRole?.let { role ->
                        val snapshotBackground by remember(uniqueKey, role) {
                            mutableStateOf(params.onBackgroundSnapshot(snapshotPhase, role, itPlayer))
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(snapshotBackground)
                        )
                    }
                }


                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF32334D)),
                    shape = RoundedCornerShape(
                        topStart = 4.dp,
                        topEnd = 16.dp,
                        bottomStart = 16.dp,
                        bottomEnd = 16.dp
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0xFF32334D),
                                        Color(0xFF2A2B3D)
                                    )
                                ),
                                shape = RoundedCornerShape(
                                    topStart = 4.dp,
                                    topEnd = 16.dp,
                                    bottomStart = 16.dp,
                                    bottomEnd = 16.dp
                                )
                            )
                            .padding(12.dp)
                    ) {
                        Text(
                            text = params.message,
                            color = Color.White,
                            fontSize = 14.sp,
                            lineHeight = 20.sp
                        )
                    }
                }
            }
        } ?: run {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF404040).copy(alpha = 0.7f)
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = "📢  ${params.message} ⏳",
                        color = Color(0xFFB0B0B0),
                        fontSize = 13.sp,
                        fontStyle = FontStyle.Italic,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun ChatMessageItemPreview() {
    ChatMessageItem(
        params = ChatMessageItemParams(
            message = "",
            player = Player(),
            phase = Phase.NightVote,
            timeStamp = Date() ,
            onSnapshotTextFirstLetter = { a, b -> "" },
            onSnapshotTextName = { a, b -> "" },
            onBackgroundSnapshot = { a, b, _ -> Color.Red }
        )
    )
}