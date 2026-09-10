package com.leafcellteam.mafia.waitingSection.waitingRoomBars

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leafcellteam.mafia.R
import kotlinx.coroutines.delay

@Composable
fun WaitingRoomBottomBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSubmit: (String) -> Unit
) {
    var lastSendTime by remember { mutableLongStateOf(0L) }
    var canSend by remember { mutableStateOf(true) }
    var remainingTime by remember { mutableIntStateOf(0) }

    LaunchedEffect(lastSendTime) {
        if (lastSendTime > 0) {
            canSend = false
            for (i in 6 downTo 1) {
                remainingTime = i
                delay(1000L)
            }
            canSend = true
            remainingTime = 0
        }
    }

    val remainingChars = 200 - value.length
    val showCharacterCount = value.length > 150
    val currentLength = value.length
    val maxLength = 200

    BottomAppBar(
        containerColor = Color(0xFF1E1E2E)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            if (showCharacterCount) {
                Text(
                    text = when {
                        remainingChars <= 0 -> stringResource(R.string.characterLimitReached)
                        remainingChars <= 20 -> "${stringResource(R.string.charactersLeft)}: $remainingChars"
                        else -> "${stringResource(R.string.charactersLeft)}: $remainingChars"
                    },
                    color = when {
                        remainingChars <= 0 -> Color(0xFFFF6B6B)
                        remainingChars <= 20 -> Color(0xFFFFD93D)
                        else -> Color(0xFF7B68EE)
                    },
                    fontSize = 12.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    textAlign = TextAlign.End
                )
            }

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = value,
                onValueChange = { newValue ->
                    if (newValue.length <= 200) {
                        onValueChange(newValue)
                    }
                },
                leadingIcon = {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        imageVector = Icons.Default.Face,
                        contentDescription = "emoji",
                        tint = Color.White
                    )
                },
                placeholder = {
                    Text(
                        text = if (!canSend) {
                            "⏱ ${stringResource(R.string.wait)} $remainingTime ${stringResource(R.string.sec_lowerCase)}..."
                        } else {
                            stringResource(R.string.enterMessage)
                        }
                    )
                },
                supportingText = if (!canSend) {
                    {
                        Text(
                            text = stringResource(R.string.cooldownMessage),
                            fontSize = 11.sp,
                            color = Color(0xFFFFD93D)
                        )
                    }
                } else null,
                maxLines = 1,
                minLines = 1,
                label = if (currentLength > 0) {
                    {
                        Text(
                            text = "$currentLength/$maxLength",
                            color = when {
                                currentLength >= 200 -> Color(0xFFFF6B6B)
                                currentLength >= 180 -> Color(0xFFFFD93D)
                                else -> Color(0xFF7B68EE)
                            }
                        )
                    }
                } else null,
                trailingIcon = {
                    IconButton(
                        onClick = {
                            if (canSend && value.isNotBlank() && value.length <= 200) {
                                onSubmit(value)
                                lastSendTime = System.currentTimeMillis()
                            }
                        },
                        enabled = canSend && value.isNotBlank() && value.length <= 200
                    ) {
                        Icon(
                            modifier = Modifier.size(30.dp),
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "send",
                            tint = if (canSend && value.isNotBlank() && value.length <= 200) {
                                Color.White
                            } else {
                                Color.Gray
                            }
                        )
                    }
                },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    focusedPlaceholderColor = Color.White,
                    focusedIndicatorColor = when {
                        !canSend -> Color(0xFFFFD93D)
                        remainingChars in 1..20 -> Color(0xFFFFD93D)
                        remainingChars <= 0 -> Color(0xFFFF6B6B)
                        else -> Color.White
                    },
                    unfocusedPlaceholderColor = Color.White,
                    unfocusedIndicatorColor = when {
                        !canSend -> Color(0xFFFFD93D)
                        remainingChars in 1..20 -> Color(0xFFFFD93D)
                        remainingChars <= 0 -> Color(0xFFFF6B6B)
                        else -> Color.LightGray
                    },
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledPlaceholderColor = Color.Gray,
                    disabledContainerColor = Color.Transparent
                ),
                enabled = canSend
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun WaitingRoomBottomBarPreview() {
    WaitingRoomBottomBar(
        value = "",
        onValueChange = {},
        onSubmit = {}
    )
}