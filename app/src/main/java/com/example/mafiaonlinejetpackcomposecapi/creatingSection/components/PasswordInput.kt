package com.example.mafiaonlinejetpackcomposecapi.creatingSection.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mafiaonlinejetpackcomposecapi.R
data class PasswordInputParam(
    val value: String,
    val showState: Boolean,
    val onValueChange: (String) -> Unit,
    val onShowStateChange: (Boolean) -> Unit
)
@Composable
fun PasswordInput(passwordInputParam: PasswordInputParam) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = passwordInputParam.value,
            onValueChange = passwordInputParam.onValueChange,
            leadingIcon = {
                Icon(
                    modifier = Modifier.size(30.dp),
                    painter = painterResource(R.drawable.locker2),
                    contentDescription = "lock",
                    tint = Color.White
                )
            },
            placeholder = {
                Text(
                    text = "Password"
                )
            },
            visualTransformation = if(passwordInputParam.showState) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordInputParam.onShowStateChange(!passwordInputParam.showState) }) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        painter = painterResource(if (passwordInputParam.showState) R.drawable.visibile else R.drawable.visibility_off),
                        contentDescription = "visibility",
                        tint = Color.White
                    )
                }
            },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.White,
                focusedPlaceholderColor = Color.White,
                focusedIndicatorColor = Color.White,
                unfocusedPlaceholderColor = Color.White,
                unfocusedIndicatorColor = Color.DarkGray,
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PasswordInputPreview() {
    PasswordInput(
        PasswordInputParam(
            value = "",
            onValueChange = { },
            onShowStateChange = {},
            showState = false
        )
    )
}