package com.leafcellteam.mafia.joinRoom.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.leafcellteam.mafia.resources.Res
import com.leafcellteam.mafia.resources.password
import com.leafcellteam.mafia.resources.required_lowerCase
import com.leafcellteam.mafia.resources.roomIsProtected_aware
import com.leafcellteam.mafia.resources.hide
import com.leafcellteam.mafia.resources.password_lowerCase
import com.leafcellteam.mafia.resources.show
import com.leafcellteam.mafia.resources.incorrect
import com.leafcellteam.mafia.resources.tryAgain
import com.leafcellteam.mafia.resources.cancel
import com.leafcellteam.mafia.resources.join
import com.leafcellteam.mafia.resources.visibile
import com.leafcellteam.mafia.resources.visibility_off

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
    val bg = Color(0xFF17171C)
    val secondaryBackground = Color(0xFF23232B)
    val softWhite = Color.White.copy(alpha = 0.92f)
    val accentGold = Color(0xFFFFD700)

    val buttonGradient = Brush.horizontalGradient(listOf(accentColor, accentGold))
    val cancelBorderGradient = Brush.horizontalGradient(listOf(Color.White.copy(alpha = 0.28f), Color.Transparent))

    val alphaAnim by animateFloatAsState(targetValue = 1f)

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(Color.Transparent),
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 8.dp,
            shadowElevation = 12.dp,
            color = bg.copy(alpha = alphaAnim)
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 18.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Lock",
                        tint = accentColor,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "${stringResource(Res.string.password)} ${stringResource(Res.string.required_lowerCase)}",
                    color = softWhite,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(Res.string.roomIsProtected_aware),
                    color = Color.White.copy(alpha = 0.72f),
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = value,
                    onValueChange = onValueChange,
                    label = { Text(stringResource(Res.string.password)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = onShowPasswordToggle) {
                            Icon(
                                painter = if (showPassword) painterResource(Res.drawable.visibile) else painterResource(Res.drawable.visibility_off),
                                contentDescription = if (showPassword)  "${stringResource(Res.string.hide)} ${stringResource(Res.string.password_lowerCase)}" else "${stringResource(Res.string.show)} ${stringResource(Res.string.password_lowerCase)}",
                                tint = Color.White
                            )
                        }
                    },
                    isError = passwordError,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = accentColor,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.14f),
                        focusedContainerColor = secondaryBackground,
                        unfocusedContainerColor = secondaryBackground,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White.copy(alpha = 0.92f),
                        cursorColor = accentColor,
                        errorTextColor = Color.White
                    )
                )

                if (passwordError) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${stringResource(Res.string.incorrect)} ${stringResource(Res.string.password_lowerCase)}. ${stringResource(Res.string.tryAgain)}.",
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 6.dp)
                    )
                } else {
                    Spacer(modifier = Modifier.height(12.dp))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = ButtonDefaults.outlinedButtonBorder().copy(
                            brush = cancelBorderGradient
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = stringResource(Res.string.cancel), fontSize = 15.sp)
                    }

                    Button(
                        onClick = onJoinWithPassword,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .background(brush = buttonGradient, shape = RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(Res.string.join),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                    }
                }
            }
        }
    }
}
