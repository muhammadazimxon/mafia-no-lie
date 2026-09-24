package com.leafcellteam.mafia.gameRoom.components.dialogs.base

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun IconRoleCircle(roleImage: Int?) {
    Box(
        modifier = Modifier
            .size(64.dp)
            .background(
                brush = androidx.compose.ui.graphics.Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF7B68EE).copy(alpha = 0.3f),
                        Color(0xFF7B68EE).copy(alpha = 0.1f),
                        Color.Transparent
                    ),
                    radius = 80f
                ),
                shape = CircleShape
            )
            .border(2.dp, Color(0xFF7B68EE).copy(alpha = 0.5f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        if (roleImage == null) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                tint = Color(0xFF7B68EE),
                modifier = Modifier.size(32.dp)
            )
        } else {
            Icon(
                painter = painterResource(id = roleImage),
                contentDescription = null,
                tint = Color(0xFF7B68EE),
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Preview
@Composable
fun IconRoleCirclePreview() {
    IconRoleCircle(roleImage = null)
}