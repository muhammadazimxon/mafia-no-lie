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

@Composable
fun ExitOrObserveDialog(
    onExitClick: () -> Unit,
    onDismiss: () -> Unit,
    endGameMessage: String,
    isGameEnd: Boolean
) {
    BaseDialogCard(onDismiss = onDismiss) {
        Content(
            onExitClick = onExitClick,
            onDismiss = onDismiss,
            endGameMessage = endGameMessage,
            isGameEnd = isGameEnd
        )
    }
}

@Composable
private fun Content(
    onExitClick: () -> Unit,
    onDismiss: () -> Unit,
    endGameMessage: String,
    isGameEnd: Boolean
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        IconEmojiCircle(emoji = "👀", bgColor = Color(0xFF8888FF).copy(alpha = 0.15f))

        Text(
            text = stringResource(R.string.chooseAction),
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text =  if(isGameEnd) endGameMessage
            else stringResource(R.string.doYouWantToStayAndWatchTheGameOrLeaveTheRoom),
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
                text = stringResource(R.string.leave),
                bgColor = Color(0xFFB00020),
                onClick = onExitClick
            )

            DialogButton(
                text = stringResource(R.string.watch),
                bgColor = Color(0xFF448AFF),
                onClick = onDismiss
            )
        }
    }
}

@Preview
@Composable
fun ExitOrObserveDialogPreview() {
    ExitOrObserveDialog(
        onExitClick = {},
        onDismiss = {},
        endGameMessage = "",
        isGameEnd = false
    )
}