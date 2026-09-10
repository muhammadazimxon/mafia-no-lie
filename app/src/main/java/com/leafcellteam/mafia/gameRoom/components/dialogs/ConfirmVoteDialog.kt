package com.leafcellteam.mafia.gameRoom.components.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leafcellteam.mafia.R
import com.leafcellteam.mafia.gameRoom.components.dialogs.base.BaseDialogCard
import com.leafcellteam.mafia.gameRoom.components.dialogs.base.DialogButton
import com.leafcellteam.mafia.gameRoom.components.dialogs.base.IconEmojiCircle
import com.leafcellteam.shared.gameRoom.mvi.UnitEvents
import com.leafcellteam.shared.gameRoom.mvi.OnDismissConfirmVoteDialog
import com.leafcellteam.shared.gameRoom.mvi.OnVote

@Composable
fun ConfirmVoteDialog(unitEventHandler: (UnitEvents) -> Unit, playerName: String, chosenId: Int) {
    BaseDialogCard(onDismiss = { unitEventHandler(OnDismissConfirmVoteDialog) }) {
        Content(
            playerName = playerName,
            onCancel = { unitEventHandler(OnDismissConfirmVoteDialog) },
            onConfirm = { unitEventHandler(OnVote(chosenId = chosenId)) }
        )
    }
}

@Composable
private fun Content(
    playerName: String,
    onCancel: () -> Unit,
    onConfirm: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        IconEmojiCircle(emoji = "🗳", bgColor = Color(0xFFFF4444).copy(alpha = 0.15f))

        Text(
            text = "${stringResource(R.string.confirm)} ${stringResource(R.string.vote)}",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "${stringResource(R.string.areYouSureYouWantTo)} ${stringResource(R.string.vote)} ${stringResource(R.string.For)} $playerName?",
            color = Color(0xFFB0B0B0),
            fontSize = 16.sp,
            lineHeight = 22.sp,
            textAlign = TextAlign.Center
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DialogButton(
                text = stringResource(R.string.cancel),
                bgColor = Color(0xFF32334D),
                onClick = onCancel
            )

            DialogButton(
                text = stringResource(R.string.vote),
                bgColor = Color(0xFF4CAF50),
                onClick = onConfirm
            )
        }
    }
}

@Preview
@Composable
fun ConfirmVoteDialogPreview() {
    ConfirmVoteDialog({}, "Player Name", -1)
}