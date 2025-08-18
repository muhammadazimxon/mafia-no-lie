package com.example.mafiaonlinejetpackcomposecapi.historySection

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.mafiaonlinejetpackcomposecapi.historySection.components.AnimatedBackgroundEffects
import com.example.mafiaonlinejetpackcomposecapi.historySection.components.EnhancedGameHistoryList
import com.example.mafiaonlinejetpackcomposecapi.historySection.components.EnhancedPlayerRatingList
import com.example.mafiaonlinejetpackcomposecapi.historySection.components.EnhancedStatisticsSection
import com.example.mafiaonlinejetpackcomposecapi.historySection.components.EnhancedTopAppBar
import com.example.mafiaonlinejetpackcomposecapi.historySection.components.LoadingIndicator
import com.example.mafiaonlinejetpackcomposecapi.historySection.components.ModernTabRow
import com.example.mafiaonlinejetpackcomposecapi.historyService.historyModel.HistoryApi
import com.example.mafiaonlinejetpackcomposecapi.historyService.historyModel.HistoryData
import com.example.mafiaonlinejetpackcomposecapi.historyService.historyModel.PlayerData
import com.example.mafiaonlinejetpackcomposecapi.historyService.historyModel.RatingPlayerData
import kotlinx.coroutines.delay

@OptIn( ExperimentalAnimationApi::class)
@Composable
fun GameHistoryScreen(
    currentPlayerId: Int,
    modifier: Modifier,
    onBack: () -> Unit
) {
    var gameHistory by remember { mutableStateOf<List<HistoryData>>(emptyList()) }
    var playerData by remember { mutableStateOf(PlayerData()) }
    var ratingPlayers by remember { mutableStateOf<List<RatingPlayerData>>(emptyList()) }
    var selectedTab by remember { mutableIntStateOf(0) }
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        try {
            gameHistory = HistoryApi.retrofitService.getHistoryData(playerId = currentPlayerId)
            playerData = HistoryApi.retrofitService.getPlayerData(playerId = currentPlayerId)
            ratingPlayers = HistoryApi.retrofitService.getRating()
                .map { RatingPlayerData(it.winRate, it.name) }
        } catch (e: Exception) {
            Log.d("History", "GameHistoryScreen: ${e.message}")
        } finally {
            delay(300)
            isLoading = false
        }
    }

    val darkBackground = Color(0xFF0A0B14)
    val cardBackground = Color(0xFF242434)
    val accentColor = Color(0xFF6366F1)
    val accentSecondary = Color(0xFF8B5CF6)
    val successColor = Color(0xFF10B981)
    val dangerColor = Color(0xFFEF4444)
    val textPrimary = Color(0xFFE5E7EB)
    val textSecondary = Color(0xFF9CA3AF)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(darkBackground)
            .then(modifier)
    ) {
        AnimatedBackgroundEffects(accentColor, accentSecondary)

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            EnhancedTopAppBar(
                title = "Game History",
                onBackClick = { onBack() },
                textColor = textPrimary,
                accentColor = accentColor
            )

            if (isLoading) {
                LoadingIndicator(accentColor)
            } else {
                EnhancedStatisticsSection(
                    stats = playerData,
                    accentColor = accentColor,
                    cardBackground = cardBackground,
                    textPrimary = textPrimary,
                    textSecondary = textSecondary,
                    successColor = successColor,
                    dangerColor = dangerColor
                )

                ModernTabRow(
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it },
                    accentColor = accentColor,
                    textPrimary = textPrimary,
                    textSecondary = textSecondary
                )

                AnimatedContent(
                    targetState = selectedTab,
                    transitionSpec = {
                        slideInHorizontally(
                            initialOffsetX = { if (targetState > initialState) 1000 else -1000 }
                        ) + fadeIn() with slideOutHorizontally(
                            targetOffsetX = { if (targetState > initialState) -1000 else 1000 }
                        ) + fadeOut()
                    },
                    label = "tab_content"
                ) { tabIndex ->
                    when (tabIndex) {
                        0 -> EnhancedGameHistoryList(
                            gameHistory = gameHistory,
                            cardBackground = cardBackground,
                            accentColor = accentColor,
                            textPrimary = textPrimary,
                            textSecondary = textSecondary,
                            successColor = successColor,
                            dangerColor = dangerColor
                        )
                        1 -> EnhancedPlayerRatingList(
                            ratingPlayers = ratingPlayers,
                            cardBackground = cardBackground,
                            accentColor = accentColor,
                            textPrimary = textPrimary,
                            textSecondary = textSecondary,
                            successColor = successColor
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GameHistoryScreenPreview() {
    GameHistoryScreen(currentPlayerId = 1, modifier = Modifier, onBack = {})
}