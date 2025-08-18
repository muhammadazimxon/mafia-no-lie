package com.example.mafiaonlinejetpackcomposecapi.gameRoom.components.dialogs.base

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DialogButton(text: String, bgColor: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .width(140.dp)
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(containerColor = bgColor),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}


@Preview
@Composable
fun DialogButtonPreview() {
    DialogButton(text = "Cancel", bgColor = Color(0xFF32334D), onClick = {})
}