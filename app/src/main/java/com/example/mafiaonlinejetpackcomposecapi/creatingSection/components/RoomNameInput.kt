package com.example.mafiaonlinejetpackcomposecapi.creatingSection.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RoomName(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
) {
    Text(
        text = "Room name:",
        fontSize = 15.sp,
        fontWeight = FontWeight.W600,
        color = Color.White
    )
    OutlinedTextField(
        value = value,
        placeholder = { Text(text = "Enter name") },
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp)),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF7B68EE),
            focusedTextColor = Color.White,
            focusedPlaceholderColor = Color.White,
            unfocusedPlaceholderColor = Color.White,
            unfocusedBorderColor = Color(0xFF7B68EE)
        ), // Colors
        shape = RoundedCornerShape(8.dp),
        isError = isError,
        singleLine = true,
        maxLines = 1
    )
}

@Composable
@Preview(showBackground = true)
fun RoomNamePreview() {
    RoomName(
        value = "",
        onValueChange = {  },
        isError = false,
    )
}