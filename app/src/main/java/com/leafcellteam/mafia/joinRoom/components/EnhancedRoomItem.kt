package com.leafcellteam.mafia.joinRoom.components

import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leafcellteam.mafia.R
import com.leafcellteam.shared.joinRoom.RoomData
import com.leafcellteam.mafia.retrofitService.retrofitModel.MafiaApi
import kotlinx.coroutines.launch

@Composable
fun EnhancedRoomItem(
    data: RoomData,
    host: String,
    onJoin: (String) -> Unit,
    onPasswordDialogOpen: (String) -> Unit
) {
    val secondaryBackground = Color(0xFF32334D)
    val accentColor = Color(0xFF7B68EE)
    val scope = rememberCoroutineScope()

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = secondaryBackground)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = data.roomName,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
//                    text = "${stringResource(R.string.host)}:",
                    text = "Host: $host",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 16.sp
                )
            }

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${data.playerQuantity}/${data.maxPlayers}",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
//            Spacer(modifier = Modifier.width(12.

            Spacer(modifier = Modifier.width(12.dp))

            Button(
                onClick = {
                    scope.launch {
                        try {
                            if (!MafiaApi.retrofitService.checkJoin(data.roomId)) {
                                if (data.password.isBlank())
                                    onJoin(data.roomId)
                                else
                                    onPasswordDialogOpen(data.password)
                            }
                        } catch (e: Exception) {
                            Log.e("JoinRoom", "EnhancedRoomItem: ${e.message}", )
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.width(95.dp)
            ) {
                Text(
                    text = stringResource(R.string.joinRoom),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
