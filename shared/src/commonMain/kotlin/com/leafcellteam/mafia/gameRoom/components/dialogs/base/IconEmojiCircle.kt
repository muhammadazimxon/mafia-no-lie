package com.leafcellteam.mafia.gameRoom.components.dialogs.base

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun IconEmojiCircle(emoji: String, bgColor: Color) {
    Box(
        modifier = Modifier
            .size(64.dp)
            .background(bgColor, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(text = emoji, fontSize = 32.sp)
    }
}

@Preview
@Composable
fun IconEmojiCirclePreview() {
    IconEmojiCircle(emoji = "🗳", bgColor = Color(0xFFFF4444).copy(alpha = 0.15f))
}