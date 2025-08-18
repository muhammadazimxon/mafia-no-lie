package com.example.mafiaonlinejetpackcomposecapi.settingsMenuSection

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.sin
import kotlin.math.cos

@Composable
fun SettingsMenu(onBackClick: () -> Unit) {
    var isLoaded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(500)
        isLoaded = true
    }

    val infiniteTransition = rememberInfiniteTransition()

    val backgroundAnimatedFloat by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(15000, easing = LinearEasing)
        ), label = ""
    )

    val loadingRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing)
        ), label = ""
    )

    val slideInAnimation by animateFloatAsState(
        targetValue = if (isLoaded) 0f else 100f,
        animationSpec = tween(
            durationMillis = 800,
            easing = FastOutSlowInEasing
        ), label = ""
    )

    val fadeInAnimation by animateFloatAsState(
        targetValue = if (isLoaded) 1f else 0f,
        animationSpec = tween(
            durationMillis = 1000,
            easing = FastOutSlowInEasing
        ), label = ""
    )

    val scaleAnimation by animateFloatAsState(
        targetValue = if (isLoaded) 1f else 0.8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ), label = ""
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1A1A2E),
                        Color(0xFF16213E),
                        Color(0xFF0F3460)
                    )
                )
            )
    ) {
        Column {
            Spacer(modifier = Modifier.height(30.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(
                            Color.White.copy(alpha = 0.1f * fadeInAnimation)
                        )
                        .clickable { onBackClick() }
                        .graphicsLayer(alpha = fadeInAnimation),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val centerX = size.width / 2
            val centerY = size.height / 2
            val time = backgroundAnimatedFloat

            for (i in 0..8) {
                val angle = (time + i * 40) * (Math.PI / 180f).toFloat()
                val radius = 100f + i * 30f
                val x = centerX + cos(angle) * radius
                val y = centerY + sin(angle) * radius * 0.5f

                drawCircle(
                    color = Color.Cyan.copy(alpha = 0.1f - i * 0.01f),
                    radius = 15f + i * 3f,
                    center = Offset(x, y)
                )
            }

            for (i in 0..5) {
                val x = size.width * (0.1f + 0.16f * i)
                val y = size.height * (0.2f + 0.12f * i)
                drawCircle(
                    color = Color.Cyan.copy(alpha = 0.05f),
                    radius = 25f + i * 8f,
                    center = Offset(x, y)
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .scale(scaleAnimation)
                .offset(y = slideInAnimation.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .rotate(loadingRotation),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val strokeWidth = 6.dp.toPx()

                    drawArc(
                        color = Color.Cyan.copy(alpha = 0.3f),
                        startAngle = 0f,
                        sweepAngle = 360f,
                        useCenter = false,
                        style = Stroke(width = strokeWidth)
                    )

                    drawArc(
                        color = Color.Cyan,
                        startAngle = 0f,
                        sweepAngle = 120f,
                        useCenter = false,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )

                    drawCircle(
                        color = Color.Cyan,
                        radius = 4.dp.toPx(),
                        center = center
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Box(
                modifier = Modifier.graphicsLayer(alpha = fadeInAnimation)
            ) {
                Text(
                    text = "Soon",
                    fontSize = 64.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Cyan.copy(alpha = 0.3f),
                    modifier = Modifier.offset(x = 4.dp, y = 4.dp)
                )
                Text(
                    text = "Soon",
                    fontSize = 64.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Cyan,
                    style = TextStyle(
                        shadow = Shadow(
                            color = Color.Cyan,
                            blurRadius = 20f
                        )
                    )
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Store will be available soon",
                fontSize = 18.sp,
                color = Color.White.copy(alpha = 0.7f * fadeInAnimation),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color.Cyan.copy(alpha = 0.1f * fadeInAnimation)
                ),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, Color.Cyan.copy(alpha = 0.3f * fadeInAnimation)),
                modifier = Modifier.graphicsLayer(alpha = fadeInAnimation)
            ) {
                Text(
                    text = "Coming Soon",
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                    color = Color.Cyan.copy(alpha = fadeInAnimation),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(alpha = fadeInAnimation * 0.5f)
        ) {
            val time = backgroundAnimatedFloat * 0.5f

            for (i in 0..12) {
                val angle = (time + i * 30) * (Math.PI / 180f).toFloat()
                val radiusX = size.width * 0.4f
                val radiusY = size.height * 0.3f
                val x = size.width / 2 + cos(angle) * radiusX
                val y = size.height / 2 + sin(angle * 0.7f) * radiusY

                drawCircle(
                    color = Color.White.copy(alpha = 0.1f),
                    radius = 2f + sin(angle * 3) * 1f,
                    center = Offset(x, y)
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun SettingsMenuPreview() {
    SettingsMenu({})
}