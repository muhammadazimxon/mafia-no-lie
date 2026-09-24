package com.leafcellteam.mafia.gameRoom.components.playerCard.parts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leafcellteam.mafia.gameRoom.models.Player

@Composable
fun PlayerCardContent(
    player: Player,
    currentPlayer: Player,
    isDead: Boolean,
    isKnownForPlayer: Boolean,
    isCardVotesVisible: Boolean,
    votes: Int
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val showVotes = isCardVotesVisible

        if (showVotes) {
            Text(
                text = "$votes",
                color = if (isDead) Color(0xFF996666) else Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start)
            )
        }

        PlayerAvatar(player, isDead, isKnownForPlayer = isKnownForPlayer, currentPlayer)

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = player.playerName,
            fontSize = 14.sp,
            color = if (isDead) Color(0xFF997777) else Color.White,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.W400,
            maxLines = 1
        )
    }
}

@Preview
@Composable
fun PlayerCardContentPreview() {
    PlayerCardContent(
        player = Player(),
        isDead = false,
        currentPlayer = Player(),
        isKnownForPlayer = false,
        isCardVotesVisible = false,
        votes = 0,
    )
}
