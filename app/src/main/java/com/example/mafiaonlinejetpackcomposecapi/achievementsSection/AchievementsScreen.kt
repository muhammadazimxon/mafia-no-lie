package com.example.mafiaonlinejetpackcomposecapi.achievementsSection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.mafiaonlinejetpackcomposecapi.achievementsSection.components.Achievement
import com.example.mafiaonlinejetpackcomposecapi.achievementsSection.components.EnhancedTopAchievementsScreenBar
import com.example.mafiaonlinejetpackcomposecapi.achievementsSection.viewModel.AchievementsViewModel
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.gameRoomSignalRClient.GameRoomServiceHub
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.gameViewModel.GameRoomViewModel
import com.example.mafiaonlinejetpackcomposecapi.register.registerViewModel.RegisterViewModel
import com.example.mafiaonlinejetpackcomposecapi.waitingSection.signalRServiceHub.SignalRServiceHub
import com.example.mafiaonlinejetpackcomposecapi.waitingSection.waitingRoomViewModel.WaitingRoomViewModel

@Composable
fun AchievementsScreen(
    modifier: Modifier,
    viewModel: AchievementsViewModel,
    onBack: () -> Unit
) {
    LaunchedEffect(true) {
        viewModel.refreshPlayerData()
        viewModel.refreshCurrentPlayerHistoryData()
        viewModel.refreshAchievementsData()
    }

    val achievementsData = viewModel.achievementsData.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1D1D2A))
            .then(modifier)
    ) {
        EnhancedTopAchievementsScreenBar(
            title = "Achievements",
            onBackClick = onBack,
            textColor = Color(0xFF7B68EE),
            accentColor = Color.White
        )

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
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
            }
        }
    }
}


@Preview
@Composable
fun AchievementsScreenPreview() {
    AchievementsScreen(Modifier, viewModel = AchievementsViewModel(), {})
}