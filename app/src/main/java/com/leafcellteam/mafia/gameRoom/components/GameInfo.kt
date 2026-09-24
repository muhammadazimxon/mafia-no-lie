package com.leafcellteam.mafia.gameRoom.components

import android.util.Log
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leafcellteam.mafia.R
import com.leafcellteam.mafia.gameRoom.models.Phase

@Composable
fun GameInfo(
    eventInfo: List<String>,
    currentPhase: Phase,
    quantities: List<Int>,
    counter: Int
) {
    Log.d("GameInfo", "Rendering game info component")
    Log.d("GameInfo", "Current phase: ${currentPhase.name}")
    Log.d("GameInfo", "Number of events: ${eventInfo.size}")

    val phase = currentPhase.name.split("(?=[A-Z])".toRegex())
    val infiniteTransition = rememberInfiniteTransition()

    val shouldBlink = counter < 10 && (currentPhase == Phase.DayVote || currentPhase == Phase.NightVote)
    val alpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(500),
            repeatMode = RepeatMode.Reverse
        )
    )

    val mafiaCount = quantities.getOrNull(0) ?: 0
    val civilianCount = quantities.getOrNull(1) ?: 0
    val totalAlive = quantities.getOrNull(2) ?: 0

    Column(modifier = Modifier.fillMaxSize()) {
        PhaseCard(
            mafiaCount = mafiaCount,
            civilianCount = civilianCount,
            totalAlive = totalAlive,
            counter = counter,
            shouldBlink = shouldBlink,
            alpha = alpha,
            phase = phase
        )

        Spacer(modifier = Modifier.height(8.dp))

        EventsCard(eventInfo = eventInfo)
    }
}

@Composable
private fun PhaseCard(
    mafiaCount: Int,
    civilianCount: Int,
    totalAlive: Int,
    counter: Int,
    shouldBlink: Boolean,
    alpha: Float,
    phase: List<String>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2E2E3E))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.gameInProcess),
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            InfoText("${stringResource(R.string.mafiaQuantity)}: $mafiaCount")
            InfoText("${stringResource(R.string.civilianQuantity)}: $civilianCount")
            InfoText("${stringResource(R.string.totalAlive)}: $totalAlive")

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "${stringResource(R.string.currentPhase)}: ${if(phase.joinToString("") == "None") "..." else phase.slice(1..phase.lastIndex).joinToString(" ")}",
                color = Color(0xFFFFD700).copy(alpha = if (shouldBlink) alpha else 1f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "${stringResource(R.string.untilNextPhase)}: ${if(counter == -1) "..." else counter} ${stringResource(R.string.sec_lowerCase)}",
                color = Color(0xFFFFD700).copy(alpha = if (shouldBlink) alpha else 1f),
                fontSize = 14.sp
            )

        }
    }
}

@Composable
private fun EventsCard(eventInfo: List<String>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2E2E3E))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "${stringResource(R.string.gameEvents)}: ${eventInfo.size}",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            if (eventInfo.isEmpty()) {
                Text(
                    text = stringResource(R.string.noEventsYet),
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            } else {
                LazyColumn {
                    itemsIndexed(eventInfo) { index, content ->
                        EventItem("#${eventInfo.size - index}. $content")
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoText(text: String) {
    Text(
        text = text,
        fontSize = 14.sp,
        color = Color.White
    )
}

private fun formatPhaseName(phase: Phase): String {
    return phase.name.split("(?=[A-Z])".toRegex())
        .drop(1)
        .joinToString(" ")
}

@Preview
@Composable
fun GameInfoPreview(){
    GameInfo(
        eventInfo = emptyList(),
        currentPhase = Phase.NightDiscussion,
        quantities = emptyList(),
        counter = 15
    )
}