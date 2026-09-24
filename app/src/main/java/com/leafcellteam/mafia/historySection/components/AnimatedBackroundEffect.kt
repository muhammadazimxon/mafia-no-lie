package com.leafcellteam.mafia.historySection.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.leafcellteam.mafia.R

@Composable
fun AnimatedBackgroundEffects(accentColor: Color, accentSecondary: Color) {
    val infiniteTransition = rememberInfiniteTransition(label = stringResource(R.string.background_lowerCase))
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = stringResource(R.string.rotation_lowerCase)
    )

    Box(
        modifier = Modifier
            .size(400.dp)
            .offset(x = 50.dp, y = (-100).dp)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(accentColor.copy(alpha = 0.15f), Color.Transparent),
                    radius = 300f
                ),
                shape = CircleShape
            )
            .blur(60.dp)
            .scale(1.2f)
    )

    Box(
        modifier = Modifier
            .size(350.dp)
            .offset(x = -150.dp, y = 200.dp)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(accentSecondary.copy(alpha = 0.12f), Color.Transparent),
                    radius = 350f
                ),
                shape = CircleShape
            )
            .blur(80.dp)
    )
}