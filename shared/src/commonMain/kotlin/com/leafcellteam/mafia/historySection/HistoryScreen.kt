package com.leafcellteam.mafia.historySection
import com.leafcellteam.mafia.logd

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.leafcellteam.mafia.resources.Res
import com.leafcellteam.mafia.resources.gameHistory
import com.leafcellteam.mafia.historySection.components.AnimatedBackgroundEffects
import com.leafcellteam.mafia.historySection.components.EnhancedGameHistoryList
import com.leafcellteam.mafia.historySection.components.EnhancedPlayerRatingList
import com.leafcellteam.mafia.historySection.components.EnhancedStatisticsSection
import com.leafcellteam.mafia.historySection.components.EnhancedTopAppBar
import com.leafcellteam.mafia.historySection.components.LoadingIndicator
import com.leafcellteam.mafia.historySection.components.ModernTabRow
import com.leafcellteam.mafia.historyService.historyModel.HistoryApi
import com.leafcellteam.mafia.historyService.historyModel.HistoryData
import com.leafcellteam.mafia.historyService.historyModel.PlayerData
import com.leafcellteam.mafia.historyService.historyModel.RatingPlayerData
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope

@OptIn(ExperimentalAnimationApi::class)
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
    var isLoading by remember { mutableStateOf(true) }
    var isLoadingMore by remember { mutableStateOf(false) }
    var currentPage by remember { mutableIntStateOf(1) }
    var hasNextPage by remember { mutableStateOf(true) }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(true) {
        try {
            val paginatedHistory = HistoryApi.retrofitService.getHistoryDataPaginated(
                playerId = currentPlayerId,
                pageNumber = 1,
                pageSize = 5
            )
            gameHistory = paginatedHistory.items
            hasNextPage = paginatedHistory.hasNextPage
            currentPage = 1

            playerData = HistoryApi.retrofitService.getPlayerData(playerId = currentPlayerId)
            ratingPlayers = HistoryApi.retrofitService.getRating()
                .map { RatingPlayerData(it.winRate, it.name) }
        } catch (e: Exception) {
            logd("History", "GameHistoryScreen: ${e.message}")
        } finally {
            delay(300)
            isLoading = false
        }
    }

    val loadNextPage: () -> Unit = {
        if (!isLoadingMore && hasNextPage) {
            isLoadingMore = true
            coroutineScope.launch {
                try {
                    delay(500)
                    val nextPage = currentPage + 1
                    val paginatedHistory = HistoryApi.retrofitService.getHistoryDataPaginated(
                        playerId = currentPlayerId,
                        pageNumber = nextPage,
                        pageSize = 5
                    )
                    gameHistory = gameHistory + paginatedHistory.items
                    hasNextPage = paginatedHistory.hasNextPage
                    currentPage = nextPage
                } catch (e: Exception) {
                    logd("History", "Load next page error: ${e.message}")
                } finally {
                    isLoadingMore = false
                }
            }
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
                title = stringResource(Res.string.gameHistory),
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
                            dangerColor = dangerColor,
                            isLoadingMore = isLoadingMore,
                            hasNextPage = hasNextPage,
                            onLoadMore = loadNextPage
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

@Preview
@Composable
fun GameHistoryScreenPreview() {
    GameHistoryScreen(currentPlayerId = 1, modifier = Modifier, onBack = {})
}