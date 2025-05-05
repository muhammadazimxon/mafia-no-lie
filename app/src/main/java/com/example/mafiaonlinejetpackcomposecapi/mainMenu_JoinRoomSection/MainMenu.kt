package com.example.mafiaonlinejetpackcomposecapi.mainMenu_JoinRoomSection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun MainMenu(
    onCreateGameScreen: () -> Unit,
    onJoinGameScreen: () -> Unit,
    onSettingsScreen: () -> Unit,
    onStoreScreen: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E1E2E))
        ,
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Mafia Online",
                modifier = Modifier.padding(bottom = 100.dp),
                color = Color.White,
                fontSize = 40.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif
            )
            OutlinedButton(
                onClick = onCreateGameScreen,
                modifier = Modifier
                    .width(180.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF7B68EE)
                )
            ) {
                Text(
                    text = "Create Game",
                    color = Color.White,
                    fontSize = 20.sp
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            OutlinedButton(
                onClick = onJoinGameScreen,
                modifier = Modifier
                    .width(180.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF7B68EE)
                )
            ) {
                Text(
                    text = "Join Game",
                    color = Color.White,
                    fontSize = 20.sp
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            OutlinedButton(
                onClick = onSettingsScreen,
                modifier = Modifier
                    .width(180.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF7B68EE)
                )
            ) {
                Text(
                    text = "Settings",
                    color = Color.White,
                    fontSize = 20.sp
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            OutlinedButton(
                onClick = onStoreScreen,
                modifier = Modifier
                    .width(180.dp)
                    .height(45.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF7B68EE)
                )
            ) {
                Text(
                    text = "Store",
                    color = Color.White,
                    fontSize = 20.sp
                )
            }
        }
    }
}

@Preview(device = "spec:width=392.7dp,height=850.9dp,dpi=440")
@Composable
fun MainMenuPreview() {
    MainMenu(
        onCreateGameScreen = { },
        onJoinGameScreen = { },
        onSettingsScreen = { },
        onStoreScreen = { }
    )
}