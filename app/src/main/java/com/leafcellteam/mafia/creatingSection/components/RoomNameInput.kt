package com.leafcellteam.mafia.creatingSection.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leafcellteam.mafia.R

data class RoomNameParam(
    val value: String,
    val onValueChange: (String) -> Unit,
    val isError: Boolean,
)
@Composable
fun RoomName(roomNameParam: RoomNameParam) {
    Text(
        text = stringResource(R.string.roomName) + ":",
        fontSize = 15.sp,
        fontWeight = FontWeight.W600,
        color = Color.White
    )
    OutlinedTextField(
        value = roomNameParam.value,
        placeholder = { Text(text = stringResource(R.string.enterName)) },
        onValueChange = roomNameParam.onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp)),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF7B68EE),
            cursorColor = Color.White,
            focusedTextColor = Color.White,
            focusedPlaceholderColor = Color.White,
            unfocusedPlaceholderColor = Color.White,
            unfocusedBorderColor = Color(0xFF7B68EE),
            unfocusedTextColor = Color.White,
            errorBorderColor = Color.Red,
            errorCursorColor = Color.White,
            errorTextColor = Color.White,
            errorPlaceholderColor = Color.White
        ),
        shape = RoundedCornerShape(8.dp),
        isError = roomNameParam.isError,
        singleLine = true,
        maxLines = 1,
        supportingText = {
            Text(stringResource(R.string.roomNameHandler))
        }
    )
}

@Composable
@Preview(showBackground = true)
fun RoomNamePreview() {
    RoomName(
        RoomNameParam(
            value = "1",
            onValueChange = {  },
            isError = true,
        )
    )
}