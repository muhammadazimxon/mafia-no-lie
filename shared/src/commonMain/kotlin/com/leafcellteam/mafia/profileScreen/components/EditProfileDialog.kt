package com.leafcellteam.mafia.profileScreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leafcellteam.mafia.resources.Res
import com.leafcellteam.mafia.resources.failedToSaveTryAgain
import com.leafcellteam.mafia.resources.unexpectedError
import com.leafcellteam.mafia.resources.editProfile
import com.leafcellteam.mafia.resources.changeYourDisplayNameAndAvatar
import com.leafcellteam.mafia.resources.close
import com.leafcellteam.mafia.resources.avatar
import com.leafcellteam.mafia.resources.tapAnEmojiToChoose
import com.leafcellteam.mafia.resources.displayName
import com.leafcellteam.mafia.resources.nameMustContain
import com.leafcellteam.mafia.resources.cancel
import com.leafcellteam.mafia.resources.save
import com.leafcellteam.mafia.retrofitService.retrofitModel.ProfileData
import kotlinx.coroutines.launch

@Composable
fun EditProfileDialog(
    initialData: ProfileData,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    /**
     * suspend callback: должен вернуть true если сохранение успешно,
     * false — если произошла ошибка (в UI покажем сообщение).
     */
    onSave: suspend (ProfileData, String) -> Boolean
) {
    val darkBackground = Color(0xFF0F1115).copy(alpha = 0.7f)
    val cardBg = Color(0xFF2C2E36)
    val accent = Color(0xFF7B68EE)
    val accentGold = Color(0xFFFFD700)

    var name by remember { mutableStateOf(initialData.name ?: "") }
    var avatarEmoji by remember { mutableStateOf("🎭") }
    var isSaving by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val isNameValid = name.trim().length >= 3
    val hasChanges = name.trim() != (initialData.name ?: "").trim() || avatarEmoji != ("🎭")

    val scope = rememberCoroutineScope()

    val emojis = listOf("🎭","😎","🕵️","🧛‍♀️","🧙‍♂️","🦹","🤠","👑","🐺","🐱")

    val failedToSaveTryAgain = stringResource(Res.string.failedToSaveTryAgain)
    val unexpectedError = stringResource(Res.string.unexpectedError)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .then(modifier),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(300.dp)
                .offset(y = (-40).dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(accent.copy(alpha = 0.12f), Color.Transparent),
                        center = Offset.Zero
                    ),
                    shape = RoundedCornerShape(200.dp)
                )
                .blur(24.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .clip(RoundedCornerShape(20.dp)),
            colors = CardDefaults.cardColors(containerColor = cardBg),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = stringResource(Res.string.editProfile),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = stringResource(Res.string.changeYourDisplayNameAndAvatar),
                            fontSize = 13.sp,
                            color = Color.White.copy(alpha = 0.6f)
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.ArrowBack,
                            contentDescription = stringResource(Res.string.close),
                            tint = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(
                        modifier = Modifier.size(72.dp),
                        shape = RoundedCornerShape(18.dp),
                        tonalElevation = 6.dp,
                        color = Brush.horizontalGradient(
                            colors = listOf(accent, accentGold)
                        ).let { Color.Transparent }
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(6.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(accent.copy(alpha = 0.95f), accent.copy(alpha = 0.3f))
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = avatarEmoji, fontSize = 28.sp)
                        }
                    }

                    Column {
                        Text(
                            text = stringResource(Res.string.avatar),
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = stringResource(Res.string.tapAnEmojiToChoose),
                            color = Color.White.copy(alpha = 0.6f),
                            fontSize = 12.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                LazyRow (
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(emojis) { e ->
                        val isSelected = e == avatarEmoji
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    if (isSelected) Brush.horizontalGradient(colors = listOf(accent, accentGold)).let { Color.Transparent }
                                    else Color.White.copy(alpha = 0.02f)
                                )
                                .border(
                                    width = if (isSelected) 2.dp else 0.dp,
                                    brush = if (isSelected) Brush.horizontalGradient(colors = listOf(accent, accentGold)) else Brush.linearGradient(listOf(Color.Transparent, Color.Transparent)),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .clickable { avatarEmoji = e },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = e, fontSize = 20.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(Res.string.displayName)) },
                    singleLine = true,
                    isError = !isNameValid && name.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        errorTextColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                    )
                )

                if (!isNameValid && name.isNotEmpty()) {
                    Text(
                        text = stringResource(Res.string.nameMustContain),
                        color = Color(0xFFEF5350),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = Color(0xFFEF5350),
                        fontSize = 13.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = onDismiss,
                        enabled = !isSaving
                    ) {
                        Text(text = stringResource(Res.string.cancel), color = Color.White.copy(alpha = 0.7f))
                    }

                    val saveEnabled = isNameValid && hasChanges && !isSaving
                    Button(
                        onClick = {
                            scope.launch {
                                errorMessage = null
                                isSaving = true
                                val newProfile = initialData.copy(
                                    name = name.trim(),
                                )

                                try {
                                    val ok = onSave(newProfile, avatarEmoji)
                                    if (!ok) {
                                        errorMessage = failedToSaveTryAgain
                                    } else {
                                        onDismiss()
                                    }
                                } catch (e: Exception) {
                                    errorMessage = e.message ?: unexpectedError
                                } finally {
                                    isSaving = false
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = accent),
                        enabled = saveEnabled,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (isSaving) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                strokeWidth = 2.dp,
                                color = Color.White
                            )
                        } else {
                            Text(text = stringResource(Res.string.save), color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewOfEditDialog() {
    EditProfileDialog(
        initialData = ProfileData(),
        onDismiss = {},
        onSave = { _, _ -> true }
    )
}