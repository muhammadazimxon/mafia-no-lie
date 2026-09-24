package com.leafcellteam.mafia.gameRoom.components.dialogs.base

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.leafcellteam.mafia.R

@Composable
fun AbilityOrVoteDialog(
    onConfirm: (String) -> Unit,
    onCancel: () -> Unit,
    onDismiss: () -> Unit,
    isOnlyAbilityUse: Boolean,
    selectedPlayerName: String
) {
    val ability = stringResource(R.string.ability)
    var selectedAction by remember { mutableStateOf<String?>(if(isOnlyAbilityUse) ability else null) }

    BaseDialogCard(onDismiss = onDismiss) {
        Content(
            selectedAction = selectedAction,
            onActionSelected = { selectedAction = it },
            onConfirm = { selectedAction?.let { onConfirm(it) } },
            onCancel = onCancel,
            playerName = selectedPlayerName,
            isOnlyAbilityUse = isOnlyAbilityUse
        )
    }
}

@Composable
private fun Content(
    selectedAction: String?,
    onActionSelected: (String) -> Unit,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    playerName: String,
    isOnlyAbilityUse: Boolean
) {
    val ability = stringResource(R.string.ability)
    val vote = stringResource(R.string.vote)

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.chooseAction),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        Text(
            modifier = Modifier.width(90.dp),
            text = "to $playerName",
            fontSize = 16.sp,
            fontWeight = FontWeight.W500,
            color = Color.White,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = if(isOnlyAbilityUse.not()) Arrangement.SpaceEvenly else Arrangement.Center
        ) {
            ActionButton(
                text = ability,
                imageText = "⚡⚡⚡ $ability ⚡⚡⚡",
                isSelected = selectedAction == ability,
                onClick = { onActionSelected(ability) }
            )
            if(isOnlyAbilityUse.not()) {
                ActionButton(
                    text = vote,
                    imageText = "🏷🔖 $vote 🔖🏷",
                    isSelected = selectedAction == vote,
                    onClick = { onActionSelected(vote) }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = if(isOnlyAbilityUse.not()) stringResource(R.string.confirmButtonIsDisabledUntilAnActionIsSelected) else stringResource(R.string.youCanUseYourAbilityOnly),
            fontSize = 14.sp,
            color = Color(0xFFB0B0B0),
            textAlign = TextAlign.Center,
            lineHeight = 14.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onConfirm,
                enabled = selectedAction != null,
                modifier = Modifier
                    .width(90.dp)
                    .height(40.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF7B68EE),
                    disabledContainerColor = Color(0xFF7B68EE).copy(alpha = 0.3f)
                )
            ) {
                Text(
                    text = stringResource(R.string.confirm),
                    fontSize = 11.sp,
                    color = Color.White,
                    fontWeight = FontWeight.W500,
                    textAlign = TextAlign.Center
                )
            }

            Button(
                onClick = onCancel,
                modifier = Modifier
                    .width(90.dp)
                    .height(40.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF642A2A)
                )
            ) {
                Text(
                    text = stringResource(R.string.cancel),
                    fontSize = 11.sp,
                    color = Color.White,
                    fontWeight = FontWeight.W500,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun ActionButton(
    text: String,
    imageText: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) Color(0xFF7B68EE) else Color(0xFF444444)
    val borderWidth = if (isSelected) 2.dp else 1.dp
    val backgroundColor = if (isSelected) Color(0xFF7B68EE).copy(alpha = 0.1f) else Color(0xFF2A2A2A)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .height(135.dp)
                .width(115.dp)
                .clickable { onClick() },
            colors = CardDefaults.cardColors(
                containerColor = backgroundColor
            ),
            border = BorderStroke(borderWidth, borderColor)
        ) {
            Box(
                modifier = Modifier.clip(RoundedCornerShape(12.dp))
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = imageText,
                    fontSize = 11.sp,
                    color = Color(0xFFB0B0B0),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = text,
            fontSize = 14.sp,
            color = if (isSelected) Color(0xFF7B68EE) else Color.White,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun BaseDialogCard(
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF2E2E3E)
            ),
            border = BorderStroke(1.dp, Color(0xFF444444))
        ) {
            Box(
                modifier = Modifier.padding(24.dp)
            ) {
                content()
            }
        }
    }
}

@Preview
@Composable
fun MiniDialogPreview() {
    AbilityOrVoteDialog(
        {}, {},
        onDismiss = {},
        isOnlyAbilityUse = true,
        selectedPlayerName = "ALSDMosmfoermgfmeromghoiergoiermg"
    )
}