//package com.leafcellteam.mafia.historySection.components
//
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.LazyListState
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.lazy.rememberLazyListState
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.derivedStateOf
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.leafcellteam.shared.history.HistoryData
//
//@Composable
//fun EnhancedGameHistoryList(
//    gameHistory: List<HistoryData>,
//    cardBackground: Color,
//    accentColor: Color,
//    textPrimary: Color,
//    textSecondary: Color,
//    successColor: Color,
//    dangerColor: Color,
//    isLoadingMore: Boolean = false,
//    hasNextPage: Boolean = false,
//    onLoadMore: () -> Unit = {}
//) {
//    val listState = rememberLazyListState()
//
//    val shouldLoadMore by remember {
//        derivedStateOf {
//            if (!hasNextPage || isLoadingMore || gameHistory.isEmpty()) {
//                false
//            } else {
//                val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
//                lastVisibleItem != null && lastVisibleItem.index >= gameHistory.size - 3
//            }
//        }
//    }
//
//    LaunchedEffect(shouldLoadMore) {
//        if (shouldLoadMore) {
//            onLoadMore()
//        }
//    }
//
//    LazyColumn(
//        state = listState,
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp),
//        verticalArrangement = Arrangement.spacedBy(12.dp),
//        contentPadding = PaddingValues(vertical = 8.dp)
//    ) {
//        items(gameHistory, key = { it.gameId }) { game ->
//            GameHistoryCard(
//                game = game,
//                cardBackground = cardBackground,
//                accentColor = accentColor,
//                textPrimary = textPrimary,
//                textSecondary = textSecondary,
//                successColor = successColor,
//                dangerColor = dangerColor
//            )
//        }
//
//        if (isLoadingMore) {
//            item {
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(16.dp),
//                    contentAlignment = Alignment.Center
//                ) {
//                    CircularProgressIndicator(
//                        color = accentColor,
//                        modifier = Modifier.padding(16.dp)
//                    )
//                }
//            }
//        }
//
//        if (!hasNextPage && gameHistory.isNotEmpty()) {
//            item {
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(16.dp),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text(
//                        text = "Все игры загружены",
//                        color = textSecondary,
//                        fontSize = 14.sp,
//                        textAlign = TextAlign.Center
//                    )
//                }
//            }
//        }
//    }
//}

package com.leafcellteam.mafia.historySection.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leafcellteam.mafia.R
import com.leafcellteam.shared.history.HistoryData
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun EnhancedGameHistoryList(
    gameHistory: List<HistoryData>,
    cardBackground: Color,
    accentColor: Color,
    textPrimary: Color,
    textSecondary: Color,
    successColor: Color,
    dangerColor: Color,
    isLoadingMore: Boolean = false,
    hasNextPage: Boolean = false,
    onLoadMore: () -> Unit = {}
) {
    val listState = rememberLazyListState()

    LaunchedEffect(listState, hasNextPage, isLoadingMore) {
        snapshotFlow {
            val layoutInfo = listState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

            lastVisibleIndex >= totalItems - 2 && totalItems > 0
        }
            .distinctUntilChanged()
            .collect { shouldLoad ->
                if (shouldLoad && hasNextPage && !isLoadingMore) {
                    onLoadMore()
                }
            }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(gameHistory, key = { it.gameId }) { game ->
            GameHistoryCard(
                game = game,
                cardBackground = cardBackground,
                accentColor = accentColor,
                textPrimary = textPrimary,
                textSecondary = textSecondary,
                successColor = successColor,
                dangerColor = dangerColor
            )
        }

        if (isLoadingMore) {
            item(key = "loading") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = accentColor,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }

        if (!hasNextPage && gameHistory.isNotEmpty()) {
            item(key = "end") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.allGamesLoaded),
                        color = textSecondary,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}