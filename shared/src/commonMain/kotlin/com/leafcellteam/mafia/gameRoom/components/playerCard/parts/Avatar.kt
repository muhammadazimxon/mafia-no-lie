package com.leafcellteam.mafia.gameRoom.components.playerCard.parts

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import org.jetbrains.compose.resources.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.leafcellteam.mafia.resources.Res
import com.leafcellteam.mafia.gameRoom.models.Player
import com.leafcellteam.mafia.resources.baseline_help_outline_24

@Composable
fun PlayerAvatar(player: Player, isDead: Boolean, isKnownForPlayer: Boolean, currentPlayer: Player) {
    Box {
        Box(
            modifier = Modifier
                .size(40.dp)
                .align(Alignment.TopStart)
                .background(
                    color = if (isDead) Color(0xFF444444) else Color.White,
                    shape = CircleShape
                )
                .border(
                    width = 1.dp,
                    color = if (isDead) Color(0xFF660000) else Color.DarkGray,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(if(isKnownForPlayer || isDead || currentPlayer.playerId == player.playerId) player.playerRole!!.image else Res.drawable.baseline_help_outline_24),
                contentDescription = player.playerRole.toString(),
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                colorFilter = if((isKnownForPlayer || isDead || currentPlayer.playerId == player.playerId).not()) ColorFilter.tint(Color.Black) else null
            )

            if (isDead) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(Color(0xAA000000), CircleShape)
                )
            }
        }

        if (isDead) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.TopStart),
                tint = Color(255, 0, 0, 100)
            )
        }

        Box(
            modifier = Modifier
                .size(16.dp)
                .clip(CircleShape)
                .background(
                    color = if (isDead) Color(0xFF553333) else player.avatarColor
                )
                .align(Alignment.BottomEnd),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = player.playerName.firstOrNull()?.toString() ?: "?",
                color = if (isDead) Color(0xFF997777) else Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Preview
@Composable
fun PlayerAvatarPreview() {
    PlayerAvatar(Player(), isDead = false, false, Player())
}
