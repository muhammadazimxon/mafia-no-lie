package com.example.mafiaonlinejetpackcomposecapi.joinRoom.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.mafiaonlinejetpackcomposecapi.R

@Composable
fun EnhancedPasswordDialog(
    value: String,
    onValueChange: (String) -> Unit,
    showPassword: Boolean,
    onShowPasswordToggle: () -> Unit,
    onDismiss: () -> Unit,
    onJoinWithPassword: () -> Unit,
    passwordError: Boolean,
    accentColor: Color
) {
    val secondaryBackground = Color(0xFF32334D)

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Password Required",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "This room is password protected. Please enter the password to join.",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (showPassword)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = onShowPasswordToggle) {
                        Icon(
                            painter = painterResource(id = if (showPassword) R.drawable.visibility_off else R.drawable.visibile),
                            contentDescription = "Toggle password visibility",
                            tint = Color.White
                        )
                    }
                },
                isError = passwordError,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = accentColor,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                    focusedContainerColor = secondaryBackground,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = accentColor
                )
            )

            if ( passwordError ) {
                Text(
                    text = "Incorrect password",
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, start = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    border = ButtonDefaults.outlinedButtonBorder().copy(
                        brush = Brush.radialGradient(listOf(Color.White.copy(alpha = 0.5f)))
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Cancel",
                        fontSize = 16.sp
                    )
                }

                Button(
                    onClick = onJoinWithPassword,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = accentColor
                    )
                ) {
                    Text(
                        text = "Join",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}