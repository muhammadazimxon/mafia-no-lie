package com.example.mafiaonlinejetpackcomposecapi.mainMenu_JoinRoomSection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Message(message: String) {
    Box(
        modifier = Modifier
            .heightIn(min = 20.dp)
            .fillMaxWidth()
            .background(Color.White)
            .clip(RoundedCornerShape(5.dp))
        ,
    ) {
        Text(
            text = "nickname:\n$message",
            modifier = Modifier.padding(vertical = 10.dp),
            fontSize = 18.sp,
            lineHeight = 30.sp
        )
    }
}

@Preview
@Composable
fun MessagePreview() {
    Message("Hello")
}