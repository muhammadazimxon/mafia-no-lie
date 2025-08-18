package com.example.mafiaonlinejetpackcomposecapi.mainMenu

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mafiaonlinejetpackcomposecapi.R

@Composable
fun MainMenu(
    onCreateGameScreen: () -> Unit,
    onJoinGameScreen: () -> Unit,
    onSettingsScreen: () -> Unit,
    onStoreScreen: () -> Unit,
    onHistoryScreen: () -> Unit,
    onAchievementsScreen: () -> Unit,
    modifier : Modifier
) {
    val darkBackground = Color(0xFF1E1E2E)
    val cardBackground = Color(0xFF282838)
    val accentColor = Color(0xFF7B68EE)

    BackHandler {}

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(darkBackground)
            .then(modifier)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(300.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 50.dp, y = (-50).dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(accentColor.copy(alpha = 0.3f), Color.Transparent),
                            radius = 300f
                        ),
                        shape = CircleShape
                    )
                    .blur(40.dp)
            )

            Box(
                modifier = Modifier
                    .size(350.dp)
                    .align(Alignment.BottomStart)
                    .offset(x = (-100).dp, y = 100.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(accentColor.copy(alpha = 0.2f), Color.Transparent),
                            radius = 350f
                        ),
                        shape = CircleShape
                    )
                    .blur(50.dp)
            )
        }

        IconButton(
            onClick = onAchievementsScreen,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .size(48.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(accentColor.copy(alpha = 0.3f), Color.Transparent),
                        radius = 200f
                    ),
                    shape = CircleShape
                )
                .clip(CircleShape)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.editor_choice_24px),
                contentDescription = "Achievements",
                tint = Color.White
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .background(accentColor.copy(alpha = 0.2f), CircleShape)
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(accentColor.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "M",
                            fontSize = 60.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(22.dp))

                Text(
                    text = "MAFIA ONLINE",
                    fontSize = 42.sp,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Be part of the strategy",
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
            }

            Card(
                modifier = Modifier
                    .width(320.dp)
                    .wrapContentHeight(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = cardBackground.copy(alpha = 0.9f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 24.dp, horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    EnhancedMenuButton(
                        text = "Create Game",
                        onClick = onCreateGameScreen,
                        accentColor = accentColor,
                        isPrimary = true
                    )

                    EnhancedMenuButton(
                        text = "Join Game",
                        onClick = onJoinGameScreen,
                        accentColor = accentColor,
                        isPrimary = true
                    )

                    EnhancedMenuButton(
                        text = "History",
                        onClick = onHistoryScreen,
                        accentColor = accentColor,
                        isPrimary = true
                    )

                    HorizontalDivider(
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth(0.8f),
                        color = Color.White.copy(alpha = 0.2f)
                    )

                    EnhancedMenuButton(
                        text = "Settings",
                        onClick = onSettingsScreen,
                        accentColor = accentColor,
                        isPrimary = false
                    )

                    EnhancedMenuButton(
                        text = "Store",
                        onClick = onStoreScreen,
                        accentColor = accentColor,
                        isPrimary = false
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "v1.0.0",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
fun EnhancedMenuButton(
    text: String,
    onClick: () -> Unit,
    accentColor: Color,
    isPrimary: Boolean
) {
    val buttonColors = if (isPrimary) {
        ButtonDefaults.buttonColors(
            containerColor = accentColor,
            contentColor = Color.White
        )
    } else {
        ButtonDefaults.buttonColors(
            containerColor = Color(0xFF32334D),
            contentColor = Color.White
        )
    }

    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        shape = RoundedCornerShape(16.dp),
        colors = buttonColors,
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = if (isPrimary) 6.dp else 2.dp,
            pressedElevation = if (isPrimary) 10.dp else 4.dp
        )
    ) {
        Text(
            text = text,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(device = "spec:width=392.7dp,height=850.9dp,dpi=440")
@Composable
fun MainMenuPreview() {
    MainMenu(
        onCreateGameScreen = {},
        onJoinGameScreen = {},
        onSettingsScreen = {},
        onStoreScreen = {},
        onHistoryScreen = {},
        onAchievementsScreen = {},
        modifier = Modifier.padding(vertical = 30.dp)
    )
}
