package com.example.mafiaonlinejetpackcomposecapi.mainMenu_JoinRoomSection

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.mafiaonlinejetpackcomposecapi.R

@Composable
fun PasswordEnter(
    value: String,
    onValueChange: (String) -> Unit,
    showState: Boolean,
    onShowStateChange: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFF1E1E2E))
                .border(
                    width = 2.dp,
                    color = Color(141, 126, 162, 255),
                    shape = RoundedCornerShape(15.dp)
                )
            ,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),

        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = value,
                    onValueChange = onValueChange,
                    leadingIcon = {
                        Icon(
                            modifier = Modifier.size(30.dp),
                            painter = painterResource(R.drawable.locker2),
                            contentDescription = "lock",
                            tint = Color.White
                        )
                    },
                    minLines = 1,
                    maxLines = 1,
                    placeholder = {
                        Text(
                            text = "Password",
                            color = Color.White
                        )
                    },
                    visualTransformation = if ( showState )
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = onShowStateChange) {
                            Icon(
                                modifier = Modifier.size(30.dp),
                                painter = painterResource(
                                    if ( showState )
                                        R.drawable.visibile
                                    else
                                        R.drawable.visibility_off
                                ),
                                contentDescription = "visibility",
                                tint = Color.White
                            )
                        }
                    },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        focusedPlaceholderColor = Color.Black,
                        focusedIndicatorColor = Color.Black,
                        unfocusedPlaceholderColor = Color.Black,
                        unfocusedIndicatorColor = Color.DarkGray,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    )
                )
            }
            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .width(150.dp)
                    .height(50.dp)
                    .padding(bottom = 10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF7B68EE),
                    contentColor = Color.White
                )
            ) { Text(text = "Ok") }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun PasswordEnterPreview() {
    PasswordEnter(
        value = "",
        onValueChange = { },
        onShowStateChange = {},
        showState = false,
        onDismiss = {}
    )
}
