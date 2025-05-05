package com.example.mafiaonlinejetpackcomposecapi.waitingSection.waitingRoomBottomBar

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun WaitingRoomBottomBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSubmit: (String) -> Unit
) {
    BottomAppBar(
        containerColor = Color(0xFF1E1E2E)
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            leadingIcon = {
                Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector = Icons.Default.Face,
                    contentDescription = "lock",
                    tint = Color.White
                )
            },
            placeholder = {
                Text(
                    text = "Enter message"
                )
            },
            maxLines = 1,
            minLines = 1,
            trailingIcon = {
                IconButton(onClick = { onSubmit(value) }) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "visibility",
                        tint = Color.White
                    )
                }
            },
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.White,
                focusedPlaceholderColor = Color.White,
                focusedIndicatorColor = Color.White,
                unfocusedPlaceholderColor = Color.White,
                unfocusedIndicatorColor = Color.LightGray,
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            )
        )
    }
}

@Composable
@Preview(showBackground = true)
fun WaitingRoomBottomBarPreview() {
    WaitingRoomBottomBar(
        value = "",
        onValueChange = {},
        onSubmit = {}
    )
}