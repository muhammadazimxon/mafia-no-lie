package com.leafcellteam.mafia.gameRoom.components.playerCard.parts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun PlayerCardBackground(brush: Brush, isDead: Boolean, content: @Composable BoxScope.() -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(brush, RoundedCornerShape(8.dp))
    ) {
        if (isDead) {
            repeat(3) { i ->
                Box(
                    modifier = Modifier
                        .width((2 + i).dp)
                        .height((20 + i * 5).dp)
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color(0xAAFF0000),
                                    Color(0x44FF0000),
                                    Color.Transparent
                                )
                            ),
                            RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)
                        )
                        .offset(x = (10 + i * 15).dp, y = 0.dp)
                )
            }

            repeat(4) { i ->
                Box(
                    modifier = Modifier
                        .size((3 + i * 2).dp)
                        .background(Color(0x99CC0000), CircleShape)
                        .offset(x = (15 + i * 12).dp, y = (25 + i * 8).dp)
                )
            }
        }

        content()
    }
}

@Preview
@Composable
fun PlayerCardBackgroundPreview() {
    PlayerCardBackground(
        Brush.verticalGradient(listOf(Color.White, Color.White)),
        isDead = false,
        content = {}
    )
}
