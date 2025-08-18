package com.example.mafiaonlinejetpackcomposecapi.mainMenu

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GameTitle() {
    Text(
        text = "Mafia Online",
        modifier = Modifier.padding(bottom = 80.dp),
        color = Color.White,
        fontSize = 40.sp,
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.Serif
    )
}

@Preview
@Composable
fun GameTitlePreview(){
    GameTitle()
}