//package com.leafcellteam.mafiaonlinejetpackcomposecapi.profileScreen
//
////import androidx.compose.animation.core.animateFloatAsState
//import androidx.compose.animation.core.animateIntAsState
//import androidx.compose.animation.core.tween
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxHeight
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.offset
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.wrapContentSize
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material.icons.filled.Star
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.Icon
//import androidx.compose.material3.OutlinedButton
//import androidx.compose.material3.Surface
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.blur
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import org.jetbrains.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.Dp
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.compose.ui.window.Dialog
//import androidx.lifecycle.viewmodel.compose.viewModel
//import com.leafcellteam.mafiaonlinejetpackcomposecapi.creatingSection.components.MinimalDialog
//import com.leafcellteam.mafiaonlinejetpackcomposecapi.profileScreen.components.EditProfileDialog
//import com.leafcellteam.mafiaonlinejetpackcomposecapi.register.registerViewModel.RegisterViewModel
//import com.leafcellteam.mafiaonlinejetpackcomposecapi.retrofitService.retrofitModel.MafiaApi
//import com.leafcellteam.mafiaonlinejetpackcomposecapi.retrofitService.retrofitModel.ProfileData
//import com.leafcellteam.mafiaonlinejetpackcomposecapi.tokenPreferences
//import com.leafcellteam.mafiaonlinejetpackcomposecapi.waitingSection.components.ConnectingToServerScreen
//import kotlin.math.max
//
//@Composable
//fun ProfileScreen(
//    registerViewModel: RegisterViewModel,
//    onBackClick: () -> Unit
//) {
//    val darkBackground = Color(0xFF1E1E2E)
//    val cardBackground = Color(0xFF282838)
//    val accentColor = Color(0xFF7B68EE)
//    val accentGold = Color(0xFFFFD700)
//    val accentGreen = Color(0xFF4CAF50)
//    var playerInfo by remember { mutableStateOf<ProfileData>(ProfileData()) }
//    var isDialogOn by remember { mutableStateOf(false) }
//    var isEditDialogOn by remember { mutableStateOf(false) }
//    val avatarEmoji by remember { mutableStateOf<String>(tokenPreferences.getAvatarEmoji()) }
//    var loader by remember { mutableStateOf(true) }
//
//    if(loader) {
//        ConnectingToServerScreen(text = "Loading...")
//    }
//
//    LaunchedEffect(true) {
//        try {
//            val response = MafiaApi.retrofitService.profileInfo(registerViewModel.currentPlayerId)
//            if (response.isSuccessful && response?.body() != null) {
//                playerInfo = response!!.body()!!
//                loader = false
//            }
//        } catch (e: Exception) {
//            loge("ProfileScreen", "ProfileScreen: ${e.message}", )
//        }
//    }
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(darkBackground)
//    ) {
//        Box(
//            modifier = Modifier
//                .size(280.dp)
//                .align(Alignment.TopStart)
//                .offset(x = (-80).dp, y = (-80).dp)
//                .background(
//                    brush = Brush.radialGradient(
//                        colors = listOf(accentColor.copy(alpha = 0.25f), Color.Transparent),
//                        radius = 280f
//                    ),
//                    shape = CircleShape
//                )
//                .blur(45.dp)
//        )
//
//        Box(
//            modifier = Modifier
//                .size(320.dp)
//                .align(Alignment.BottomEnd)
//                .offset(x = 100.dp, y = 120.dp)
//                .background(
//                    brush = Brush.radialGradient(
//                        colors = listOf(accentGold.copy(alpha = 0.15f), Color.Transparent),
//                        radius = 320f
//                    ),
//                    shape = CircleShape
//                )
//                .blur(55.dp)
//        )
//
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .verticalScroll(rememberScrollState())
//                .padding(24.dp),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.spacedBy(20.dp)
//        ) {
//            Spacer(modifier = Modifier.height(16.dp))
//
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(120.dp)
//            ) {
//                Surface(
//                    modifier = Modifier
//                        .padding(start = 12.dp, top = 8.dp)
//                        .size(48.dp)
//                        .align(Alignment.TopStart),
//                    shape = CircleShape,
//                    tonalElevation = 6.dp,
//                    shadowElevation = 4.dp,
//                    color = Color.White.copy(alpha = 0.06f),
//                    onClick = { onBackClick() }
//                ) {
//                    Box(contentAlignment = Alignment.Center) {
//                        Icon(
//                            imageVector = Icons.Default.ArrowBack,
//                            contentDescription = "Back",
//                            tint = Color.White,
//                            modifier = Modifier.size(20.dp)
//                        )
//                    }
//                }
//
//                Box(
//                    modifier = Modifier
//                        .align(Alignment.Center)
//                        .wrapContentSize(),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Box(
//                        modifier = Modifier
//                            .size(140.dp)
//                            .background(
//                                brush = Brush.linearGradient(
//                                    colors = listOf(
//                                        accentColor.copy(alpha = 0.3f),
//                                        accentGold.copy(alpha = 0.2f)
//                                    )
//                                ),
//                                shape = CircleShape
//                            )
//                            .padding(6.dp),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Box(
//                            modifier = Modifier
//                                .fillMaxSize()
//                                .clip(CircleShape)
//                                .background(
//                                    brush = Brush.linearGradient(
//                                        colors = listOf(accentColor, accentColor.copy(alpha = 0.3f))
//                                    )
//                                ),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            StaticAvatar(emoji = avatarEmoji)
//                        }
//                    }
//                }
//            }
//
//            Text(
//                text = playerInfo.name,
//                fontSize = 32.sp,
//                fontWeight = FontWeight.Bold,
//                color = Color.White,
//                textAlign = TextAlign.Center
//            )
//
//            Box(
//                modifier = Modifier
//                    .clip(RoundedCornerShape(20.dp))
//                    .background(
//                        brush = Brush.horizontalGradient(
//                            colors = listOf(
//                                accentGold.copy(alpha = 0.3f),
//                                accentColor.copy(alpha = 0.3f)
//                            )
//                        )
//                    )
//                    .border(
//                        width = 2.dp,
//                        brush = Brush.horizontalGradient(
//                            colors = listOf(accentGold, accentColor)
//                        ),
//                        shape = RoundedCornerShape(20.dp)
//                    )
//                    .padding(horizontal = 24.dp, vertical = 8.dp)
//            ) {
//                LevelRow(
//                    playerInfo = playerInfo,
//                    accentGold = accentGold
//                )
//            }
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.spacedBy(12.dp)
//            ) {
//                StatCard(
//                    title = "Games",
//                    value = "${playerInfo.allPlayedGames}",
//                    modifier = Modifier.weight(1f),
//                    cardBackground = cardBackground,
//                    accentColor = accentColor
//                )
//                StatCard(
//                    title = "Wins",
//                    value = "${playerInfo.wonGames}",
//                    modifier = Modifier.weight(1f),
//                    cardBackground = cardBackground,
//                    accentColor = accentGreen
//                )
//                StatCard(
//                    title = "W/R",
//                    value = "${playerInfo.winRate}%",
//                    modifier = Modifier.weight(1f),
//                    cardBackground = cardBackground,
//                    accentColor = accentGold
//                )
//            }
//
//            Card(
//                modifier = Modifier.fillMaxWidth(),
//                shape = RoundedCornerShape(20.dp),
//                colors = CardDefaults.cardColors(containerColor = cardBackground.copy(alpha = 0.9f)),
//                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
//            ) {
//                Column(
//                    modifier = Modifier.padding(20.dp),
//                    verticalArrangement = Arrangement.spacedBy(14.dp)
//                ) {
//                    Text(
//                        text = "🎮 Profile Achievements",
//                        fontSize = 20.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = Color.White
//                    )
//
//                    ActivityItem(
//                        emoji = "🏆",
//                        title = "1000 victories",
//                        accentColor = Color(0xFF4CAF50),
//                        isCompleted = playerInfo.wonGames >= 1000
//                    )
//
//                    ActivityItem(
//                        emoji = "🕵️‍♀️",
//                        title = "100 games with civilian roles",
//                        accentColor = Color(0xFF2196F3),
//                        isCompleted = playerInfo.playedQuantityAsCivilian >= 100
//                    )
//
//                    ActivityItem(
//                        emoji = "🧛‍♀️",
//                        title = "100 games with mafia roles",
//                        accentColor = accentGold,
//                        isCompleted = playerInfo.playedQuantityAsMafia >= 100
//                    )
//                }
//            }
//
//            Card(
//                modifier = Modifier.fillMaxWidth(),
//                shape = RoundedCornerShape(20.dp),
//                colors = CardDefaults.cardColors(containerColor = cardBackground.copy(alpha = 0.9f)),
//                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
//            ) {
//                Column(
//                    modifier = Modifier.padding(20.dp),
//                    verticalArrangement = Arrangement.spacedBy(12.dp)
//                ) {
//                    Text(
//                        text = "📊 Role Statistics",
//                        fontSize = 20.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = Color.White
//                    )
//
//                    RoleStatBar(
//                        role = "😇 Civilian roles",
//                        wins = playerInfo.playedQuantityAsCivilian,
//                        playerTotalGames = playerInfo.allPlayedGames,
//                        accentColor = accentGreen
//                    )
//
//                    RoleStatBar(
//                        role = "🔪 Mafia roles",
//                        wins = playerInfo.playedQuantityAsMafia,
//                        playerTotalGames = playerInfo.allPlayedGames,
//                        accentColor = Color(0xFFE91E63)
//                    )
//                }
//            }
//
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.spacedBy(12.dp)
//            ) {
//                OutlinedButton(
//                    onClick = {
//                        isEditDialogOn = true
//                    },
//                    modifier = Modifier
//                        .weight(1f)
//                        .height(56.dp),
//                    shape = RoundedCornerShape(16.dp),
//                    colors = ButtonDefaults.outlinedButtonColors(
//                        contentColor = accentColor
//                    ),
//                    border = ButtonDefaults.outlinedButtonBorder.copy(
//                        width = 2.dp,
//                        brush = Brush.horizontalGradient(
//                            colors = listOf(accentColor, accentGold)
//                        )
//                    )
//                ) {
//                    Text(
//                        text = "Edit Profile",
//                        fontSize = 16.sp,
//                        fontWeight = FontWeight.Bold
//                    )
//                }
//
//                Button(
//                    onClick = { isDialogOn = true },
//                    modifier = Modifier
//                        .weight(1f)
//                        .height(56.dp),
//                    shape = RoundedCornerShape(16.dp),
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = accentColor
//                    ),
//                    elevation = ButtonDefaults.buttonElevation(
//                        defaultElevation = 6.dp,
//                        pressedElevation = 10.dp
//                    )
//                ) {
//                    Text(
//                        text = "Share",
//                        fontSize = 16.sp,
//                        fontWeight = FontWeight.Bold
//                    )
//                }
//
//                if(isEditDialogOn) {
//                    Dialog(onDismissRequest = { isEditDialogOn = false }) {
//                        EditProfileDialog(
//                            initialData = playerInfo,
//                            modifier = Modifier,
//                            onDismiss = { isEditDialogOn = false },
//                            onSave = { profileData, avatarEmoji ->
//                                tokenPreferences.saveAvatarEmoji(avatarEmoji)
//                                try {
//                                    val response = MafiaApi.retrofitService.editProfile(
//                                        registerViewModel.currentPlayerId,
//                                        profileData.name.trim()
//                                    )
//                                    if (response.isSuccessful.not())
//                                        return@EditProfileDialog false
//                                } catch (e: Exception) {
//                                    loge("EditProfile", "ProfileScreen: ${e.message}")
//                                }
//                                return@EditProfileDialog true
//                            }
//                        )
//                    }
//                }
//
//                if(isDialogOn) {
//                    MinimalDialog(
//                        description = "Share is coming soon!"
//                    ) {
//                        isDialogOn = false
//                    }
//                }
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//        }
//    }
//}
//
//@Composable
//fun LevelRow(
//    playerInfo: ProfileData,
//    accentGold: Color
//) {
//    val games = try { playerInfo.allPlayedGames } catch (e: Exception) { 0 }
//    val wins = try { playerInfo.wonGames } catch (e: Exception) { 0 }
//
//    val computedLevel = remember(games, wins) {
//        val lvlFromGames = (games ?: 0) / 5
//        val lvlFromWins = (wins ?: 0) / 10
//        max(1, 1 + lvlFromGames + lvlFromWins)
//    }
//
//    val animatedLevel by animateIntAsState(targetValue = computedLevel)
//
//    Row(
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.spacedBy(8.dp)
//    ) {
//        Icon(
//            imageVector = Icons.Default.Star,
//            contentDescription = null,
//            tint = accentGold,
//            modifier = Modifier.size(20.dp)
//        )
//        Text(
//            text = "Level $animatedLevel",
//            fontSize = 18.sp,
//            fontWeight = FontWeight.Bold,
//            color = Color.White
//        )
//    }
//}
//
//@Composable
//fun StaticAvatar(
//    size: Dp = 140.dp,
//    emoji: String = "🎭",
//    ringWidth: Dp = 6.dp
//) {
//    val outerSize = size
//    Box(
//        modifier = Modifier.size(outerSize),
//        contentAlignment = Alignment.Center
//    ) {
//        Box(
//            modifier = Modifier
//                .matchParentSize()
//                .offset(x = (-8).dp, y = (-8).dp)
//                .background(
//                    brush = Brush.radialGradient(
//                        colors = listOf(Color(0xFF7B68EE).copy(alpha = 0.18f), Color.Transparent),
//                        radius = outerSize.value
//                    ),
//                    shape = CircleShape
//                )
//                .blur(18.dp)
//        )
//
//        Box(
//            modifier = Modifier
//                .size(outerSize)
//                .clip(CircleShape)
//                .background(
//                    brush = Brush.linearGradient(
//                        colors = listOf(Color(0xFF7B68EE), Color(0xFFFFD700))
//                    )
//                )
//                .padding(ringWidth)
//                .clip(CircleShape)
//                .background(Color(0xFF22222A))
//                .border(
//                    width = 1.dp,
//                    brush = Brush.linearGradient(
//                        colors = listOf(
//                            Color.White.copy(alpha = 0.06f),
//                            Color.Black.copy(alpha = 0.3f)
//                        )
//                    ),
//                    shape = CircleShape
//                ),
//            contentAlignment = Alignment.Center
//        ) {
//            Text(
//                text = emoji,
//                fontSize = (size.value / 2.6).sp
//            )
//        }
//    }
//}
//
//@Composable
//fun StatCard(
//    title: String,
//    value: String,
//    modifier: Modifier = Modifier,
//    cardBackground: Color,
//    accentColor: Color
//) {
//    Card(
//        modifier = modifier.height(100.dp),
//        shape = RoundedCornerShape(16.dp),
//        colors = CardDefaults.cardColors(containerColor = cardBackground.copy(alpha = 0.9f)),
//        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(12.dp),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
//        ) {
//            Text(
//                text = value,
//                fontSize = 28.sp,
//                fontWeight = FontWeight.Bold,
//                color = accentColor
//            )
//            Spacer(modifier = Modifier.height(4.dp))
//            Text(
//                text = title,
//                fontSize = 14.sp,
//                color = Color.White.copy(alpha = 0.7f)
//            )
//        }
//    }
//}
//
//@Composable
//fun ActivityItem(
//    emoji: String,
//    title: String,
//    accentColor: Color,
//    isCompleted: Boolean
//) {
//    Row(
//        modifier = Modifier.fillMaxWidth(),
//        horizontalArrangement = Arrangement.spacedBy(12.dp),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Box(
//            modifier = Modifier
//                .size(50.dp)
//                .clip(CircleShape)
//                .background(accentColor.copy(alpha = 0.1f), CircleShape),
//            contentAlignment = Alignment.Center
//        ) {
//            Text(text = emoji, fontSize = 24.sp)
//        }
//
//        Text(
//            text = title,
//            fontSize = 16.sp,
//            fontWeight = FontWeight.Bold,
//            color = if(isCompleted) Color.White else Color.Gray
//        )
//    }
//}
//
//@Composable
//fun RoleStatBar(
//    role: String,
//    wins: Int,
//    total: Int? = null,
//    playerTotalGames: Int = 0,
//    accentColor: Color
//) {
//    val computedTotal = when {
//        total != null && total > 0 -> total
//        playerTotalGames > 0 -> playerTotalGames
//        else -> maxOf(wins, 1)
//    }
//
//    val rawProgress = wins.toFloat() / computedTotal.toFloat()
//    val progress = rawProgress.coerceIn(0f, 1f)
//    val animatedProgress by animateFloatAsState(
//        targetValue = progress,
//        animationSpec = tween(durationMillis = 600)
//    )
//
//    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            Text(
//                text = role,
//                fontSize = 15.sp,
//                color = Color.White,
//                fontWeight = FontWeight.Medium
//            )
//
//            val rightText = if (total != null && total > 0) {
//                "$wins/$total"
//            } else if (playerTotalGames > 0) {
//                "$wins/${playerTotalGames}"
//            } else {
//                "$wins"
//            }
//            Text(
//                text = rightText,
//                fontSize = 13.sp,
//                color = Color.White.copy(alpha = 0.8f)
//            )
//        }
//
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(10.dp)
//                .clip(RoundedCornerShape(6.dp))
//                .background(Color.White.copy(alpha = 0.06f))
//        ) {
//            Box(
//                modifier = Modifier
//                    .fillMaxHeight()
//                    .fillMaxWidth(animatedProgress)
//                    .clip(RoundedCornerShape(6.dp))
//                    .background(
//                        brush = Brush.horizontalGradient(
//                            colors = listOf(accentColor, accentColor.copy(alpha = 0.7f))
//                        )
//                    )
//            )
//        }
//
//        if (total == null) {
//            Text(
//                text = "Progress is calculated relative to all of the player's games.",
//                fontSize = 12.sp,
//                color = Color.White.copy(alpha = 0.45f)
//            )
//        }
//    }
//}
//
//
//@Preview(showBackground = true, device = "spec:width=392.7dp,height=850.9dp,dpi=440")
//@Composable
//fun ProfileScreenPreview() {
//    ProfileScreen(viewModel(), {})
//}


package com.leafcellteam.mafia.profileScreen
import com.leafcellteam.mafia.loge

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.leafcellteam.mafia.resources.Res
import com.leafcellteam.mafia.resources.loading
import com.leafcellteam.mafia.resources.back
import com.leafcellteam.mafia.resources.games
import com.leafcellteam.mafia.resources.wins
import com.leafcellteam.mafia.resources.winRate
import com.leafcellteam.mafia.resources.profile
import com.leafcellteam.mafia.resources.achievements
import com.leafcellteam.mafia.resources.victories_lowerCase
import com.leafcellteam.mafia.resources.gamesWithCivilianRoles_lowerCase
import com.leafcellteam.mafia.resources.gamesWithMafiaRoles_lowerCase
import com.leafcellteam.mafia.resources.roleStatistics
import com.leafcellteam.mafia.resources.civilianRoles
import com.leafcellteam.mafia.resources.mafiaRoles
import com.leafcellteam.mafia.resources.editProfile
import com.leafcellteam.mafia.resources.share
import com.leafcellteam.mafia.resources.shareIsComingSoon
import com.leafcellteam.mafia.resources.level
import com.leafcellteam.mafia.resources.progressIsCalculatedRelativeToAllOfThePlayersGames
import com.leafcellteam.mafia.creatingSection.components.MinimalDialog
import com.leafcellteam.mafia.profileScreen.components.EditProfileDialog
import com.leafcellteam.mafia.register.registerViewModel.RegisterViewModel
import com.leafcellteam.mafia.retrofitService.retrofitModel.MafiaApi
import com.leafcellteam.mafia.retrofitService.retrofitModel.ProfileData
import com.leafcellteam.mafia.tokenPreferences
import com.leafcellteam.mafia.waitingSection.components.ConnectingToServerScreen
import kotlin.math.max

@Composable
fun ProfileScreen(
    registerViewModel: RegisterViewModel,
    onBackClick: () -> Unit
) {
    val darkBackground = Color(0xFF1E1E2E)
    val cardBackground = Color(0xFF282838)
    val accentColor = Color(0xFF7B68EE)
    val accentGold = Color(0xFFFFD700)
    val accentGreen = Color(0xFF4CAF50)
    var playerInfo by remember { mutableStateOf<ProfileData>(ProfileData()) }
    var isDialogOn by remember { mutableStateOf(false) }
    var isEditDialogOn by remember { mutableStateOf(false) }
    val avatarEmoji by remember { mutableStateOf<String>(tokenPreferences.getAvatarEmoji()) }
    var loader by remember { mutableStateOf(true) }

    if(loader) {
        ConnectingToServerScreen(text = "${stringResource(Res.string.loading)}...")
    }

    LaunchedEffect(true) {
        try {
            val response = MafiaApi.retrofitService.profileInfo(registerViewModel.currentPlayerId)
            if (response.isSuccessful && response.body != null) {
                playerInfo = response.body
                loader = false
            }
        } catch (e: Exception) {
            loge("ProfileScreen", "ProfileScreen: ${e.message}", )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(darkBackground)
    ) {
        Box(
            modifier = Modifier
                .size(280.dp)
                .align(Alignment.TopStart)
                .offset(x = (-80).dp, y = (-80).dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(accentColor.copy(alpha = 0.25f), Color.Transparent),
                        radius = 280f
                    ),
                    shape = CircleShape
                )
                .blur(45.dp)
        )

        Box(
            modifier = Modifier
                .size(320.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 100.dp, y = 120.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(accentGold.copy(alpha = 0.15f), Color.Transparent),
                        radius = 320f
                    ),
                    shape = CircleShape
                )
                .blur(55.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            ) {
                Surface(
                    modifier = Modifier
                        .padding(start = 12.dp, top = 8.dp)
                        .size(48.dp)
                        .align(Alignment.TopStart),
                    shape = CircleShape,
                    tonalElevation = 6.dp,
                    shadowElevation = 4.dp,
                    color = Color.White.copy(alpha = 0.06f),
                    onClick = { onBackClick() }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(Res.string.back),
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .wrapContentSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(140.dp)
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        accentColor.copy(alpha = 0.3f),
                                        accentGold.copy(alpha = 0.2f)
                                    )
                                ),
                                shape = CircleShape
                            )
                            .padding(6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(accentColor, accentColor.copy(alpha = 0.3f))
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            StaticAvatar(emoji = avatarEmoji)
                        }
                    }
                }
            }

            Text(
                text = playerInfo.name,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                accentGold.copy(alpha = 0.3f),
                                accentColor.copy(alpha = 0.3f)
                            )
                        )
                    )
                    .border(
                        width = 2.dp,
                        brush = Brush.horizontalGradient(
                            colors = listOf(accentGold, accentColor)
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(horizontal = 24.dp, vertical = 8.dp)
            ) {
                LevelRow(
                    playerInfo = playerInfo,
                    accentGold = accentGold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = stringResource(Res.string.games),
                    value = "${playerInfo.allPlayedGames}",
                    modifier = Modifier.weight(1f),
                    cardBackground = cardBackground,
                    accentColor = accentColor
                )
                StatCard(
                    title = stringResource(Res.string.wins),
                    value = "${playerInfo.wonGames}",
                    modifier = Modifier.weight(1f),
                    cardBackground = cardBackground,
                    accentColor = accentGreen
                )
                StatCard(
                    title = stringResource(Res.string.winRate),  //  W/R
                    value = "${playerInfo.winRate}%",
                    modifier = Modifier.weight(1f),
                    cardBackground = cardBackground,
                    accentColor = accentGold
                )
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = cardBackground.copy(alpha = 0.9f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "🎮 ${stringResource(Res.string.profile)} ${stringResource(Res.string.achievements)}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    ActivityItem(
                        emoji = "🏆",
                        title = "1000 ${stringResource(Res.string.victories_lowerCase)}",
                        accentColor = Color(0xFF4CAF50),
                        isCompleted = playerInfo.wonGames >= 1000
                    )

                    ActivityItem(
                        emoji = "🕵️‍♀️",
                        title = "100 ${stringResource(Res.string.gamesWithCivilianRoles_lowerCase)}",
                        accentColor = Color(0xFF2196F3),
                        isCompleted = playerInfo.playedQuantityAsCivilian >= 100
                    )

                    ActivityItem(
                        emoji = "🧛‍♀️",
                        title = "100 ${stringResource(Res.string.gamesWithMafiaRoles_lowerCase)}",
                        accentColor = accentGold,
                        isCompleted = playerInfo.playedQuantityAsMafia >= 100
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = cardBackground.copy(alpha = 0.9f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "📊 ${stringResource(Res.string.roleStatistics)}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    RoleStatBar(
                        role = "😇 ${stringResource(Res.string.civilianRoles)}",
                        wins = playerInfo.playedQuantityAsCivilian,
                        playerTotalGames = playerInfo.allPlayedGames,
                        accentColor = accentGreen
                    )

                    RoleStatBar(
                        role = "🔪 ${stringResource(Res.string.mafiaRoles)}",
                        wins = playerInfo.playedQuantityAsMafia,
                        playerTotalGames = playerInfo.allPlayedGames,
                        accentColor = Color(0xFFE91E63)
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { isEditDialogOn = true },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = accentColor
                    ),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        width = 2.dp,
                        brush = Brush.horizontalGradient(
                            colors = listOf(accentColor, accentGold)
                        )
                    )
                ) {
                    Text(
                        text = stringResource(Res.string.editProfile),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(
                    onClick = { isDialogOn = true },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = accentColor
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 6.dp,
                        pressedElevation = 10.dp
                    )
                ) {
                    Text(
                        text = stringResource(Res.string.share),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                if(isEditDialogOn) {
                    Dialog(onDismissRequest = { isEditDialogOn = false }) {
                        EditProfileDialog(
                            initialData = playerInfo,
                            modifier = Modifier,
                            onDismiss = { isEditDialogOn = false },
                            onSave = { profileData, avatarEmoji ->
                                tokenPreferences.saveAvatarEmoji(avatarEmoji)
                                try {
                                    val response = MafiaApi.retrofitService.editProfile(
                                        registerViewModel.currentPlayerId,
                                        profileData.name.trim()
                                    )
                                    if (response.isSuccessful.not())
                                        return@EditProfileDialog false
                                } catch (e: Exception) {
                                    loge("EditProfile", "ProfileScreen: ${e.message}")
                                }
                                return@EditProfileDialog true
                            }
                        )
                    }
                }

                if(isDialogOn) {
                    MinimalDialog(description = "${stringResource(Res.string.shareIsComingSoon)}!") {
                        isDialogOn = false
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun LevelRow(
    playerInfo: ProfileData,
    accentGold: Color
) {
    val games = try { playerInfo.allPlayedGames } catch (e: Exception) { 0 }
    val wins = try { playerInfo.wonGames } catch (e: Exception) { 0 }

    val computedLevel = remember(games, wins) {
        val lvlFromGames = (games ?: 0) / 5
        val lvlFromWins = (wins ?: 0) / 10
        max(1, 1 + lvlFromGames + lvlFromWins)
    }

    val animatedLevel by animateIntAsState(targetValue = computedLevel)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            tint = accentGold,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = "${stringResource(Res.string.level)} $animatedLevel",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Composable
fun StaticAvatar(
    size: Dp = 140.dp,
    emoji: String = "🎭",
    ringWidth: Dp = 6.dp
) {
    val outerSize = size
    Box(
        modifier = Modifier.size(outerSize),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(x = (-8).dp, y = (-8).dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0xFF7B68EE).copy(alpha = 0.18f), Color.Transparent),
                        radius = outerSize.value
                    ),
                    shape = CircleShape
                )
                .blur(18.dp)
        )

        Box(
            modifier = Modifier
                .size(outerSize)
                .clip(CircleShape)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFF7B68EE), Color(0xFFFFD700))
                    )
                )
                .padding(ringWidth)
                .clip(CircleShape)
                .background(Color(0xFF22222A))
                .border(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.06f),
                            Color.Black.copy(alpha = 0.3f)
                        )
                    ),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = emoji,
                fontSize = (size.value / 2.6).sp
            )
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    cardBackground: Color,
    accentColor: Color
) {
    Card(
        modifier = modifier.height(100.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBackground.copy(alpha = 0.9f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = value,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = accentColor
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun ActivityItem(
    emoji: String,
    title: String,
    accentColor: Color,
    isCompleted: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(accentColor.copy(alpha = 0.1f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(text = emoji, fontSize = 24.sp)
        }

        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = if(isCompleted) Color.White else Color.Gray
        )
    }
}

@Composable
fun RoleStatBar(
    role: String,
    wins: Int,
    total: Int? = null,
    playerTotalGames: Int = 0,
    accentColor: Color
) {
    val computedTotal = when {
        total != null && total > 0 -> total
        playerTotalGames > 0 -> playerTotalGames
        else -> maxOf(wins, 1)
    }

    val rawProgress = wins.toFloat() / computedTotal.toFloat()
    val progress = rawProgress.coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 600)
    )

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = role,
                fontSize = 15.sp,
                color = Color.White,
                fontWeight = FontWeight.Medium
            )

            val rightText = if (total != null && total > 0) {
                "$wins/$total"
            } else if (playerTotalGames > 0) {
                "$wins/${playerTotalGames}†"
            } else {
                "$wins"
            }
            Text(
                text = rightText,
                fontSize = 13.sp,
                color = Color.White.copy(alpha = 0.8f)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(Color.White.copy(alpha = 0.06f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(animatedProgress)
                    .clip(RoundedCornerShape(6.dp))
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(accentColor, accentColor.copy(alpha = 0.7f))
                        )
                    )
            )
        }

        if (total == null) {
            Text(
                text = "${stringResource(Res.string.progressIsCalculatedRelativeToAllOfThePlayersGames)}.",
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.45f)
            )
        }
    }
}


@Preview
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(viewModel(), {})
}