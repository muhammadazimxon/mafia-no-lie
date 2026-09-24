package com.leafcellteam.mafia.achievementsSection
import com.leafcellteam.mafia.loge

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.leafcellteam.mafia.resources.Res
import com.leafcellteam.mafia.achievementsSection.components.Achievement
import com.leafcellteam.mafia.achievementsSection.components.EnhancedTopAchievementsScreenBar
import com.leafcellteam.mafia.achievementsSection.viewModel.AchievementsViewModel
import com.leafcellteam.mafia.resources.achievements
import com.leafcellteam.mafia.resources.noAchievementsYet

@Composable
fun AchievementsScreen(
    modifier: Modifier,
    viewModel: AchievementsViewModel,
    onBack: () -> Unit
) {
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(true) {
        try {
            viewModel.refreshPlayerData()
            viewModel.refreshCurrentPlayerHistoryData()
            viewModel.refreshAchievementsData()
        } catch (e: Exception) {
            loge("Achievment", "AchievementsScreen: ${e.message}")
        } finally {
            isLoading = false
        }
    }

    val achievementsData = viewModel.achievementsData.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1D1D2A))
            .then(modifier)
    ) {
        EnhancedTopAchievementsScreenBar(
            title = stringResource(Res.string.achievements),
            onBackClick = onBack,
            textColor = Color(0xFF7B68EE),
            accentColor = Color.White
        )

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            if(isLoading) {
                Box(
                    modifier = Modifier.align(Alignment.Center).fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                if (achievementsData.isNotEmpty()) {
                    LazyColumn {
                        items(achievementsData) { data ->
                            Achievement(
                                title = data.title,
                                description = data.description,
                                isAchieved = data.isAchieved
                            )
                        }
                    }
                } else {
                     Text(stringResource(Res.string.noAchievementsYet), color = Color.Gray)
                }
            }
        }
    }
}


@Preview
@Composable
fun AchievementsScreenPreview() {
    AchievementsScreen(Modifier, viewModel = viewModel(), {})
}