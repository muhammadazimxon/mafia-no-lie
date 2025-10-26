package com.example.mafiaonlinejetpackcomposecapi.register

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mafiaonlinejetpackcomposecapi.R
import com.example.mafiaonlinejetpackcomposecapi.creatingSection.components.MinimalDialog
import com.example.mafiaonlinejetpackcomposecapi.register.registerMvi.RegisterEvent
import com.example.mafiaonlinejetpackcomposecapi.register.registerViewModel.RegisterViewModel

@Composable
fun CreateCharacter(
    onBack : () -> Unit,
    onConfirm : () -> Unit,
    registerViewModel : RegisterViewModel
) {

    val uiState by registerViewModel.registerState.collectAsState()

    val darkBackground = Color(0xFF1E1E2E)
    val cardBackground = Color(0xFF282838)
    val accentColor = Color(0xFF7B68EE)
    val textFieldColor = Color(0xFF3D3D5B)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(darkBackground),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 50.dp, y = (-50).dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                accentColor.copy(alpha = 0.3f),
                                Color.Transparent
                            ),
                            radius = 200f
                        ),
                        shape = CircleShape
                    )
                    .blur(30.dp)
            )

            Box(
                modifier = Modifier
                    .size(250.dp)
                    .align(Alignment.BottomStart)
                    .offset(x = (-70).dp, y = 70.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(accentColor.copy(alpha = 0.2f), Color.Transparent),
                            radius = 250f
                        ),
                        shape = CircleShape
                    )
                    .blur(40.dp)
            )
        }

        Card(
            modifier = Modifier
                .width(450.dp)
                .wrapContentHeight()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = cardBackground),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(30.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(R.drawable.informator),
                    contentDescription = "",
                    modifier = Modifier
                        .size(140.dp)
                        .background(accentColor.copy(alpha = 0.2f), CircleShape)
                )

                Text(
                    text = "CREATE CHARACTER",
                    fontSize = 38.sp,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )


                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = uiState.playerName,
                    onValueChange = { newName ->
                        registerViewModel.registerEventHandler(
                            RegisterEvent.UpdatePlayerName(newName)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    placeholder = {
                        Text(
                            text = "Nickname",
                            color = Color.White.copy(alpha = 0.6f)
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = accentColor,
                        unfocusedBorderColor = accentColor.copy(alpha = 0.5f),
                        focusedContainerColor = textFieldColor,
                        unfocusedTextColor = Color.White,
                        focusedTextColor = Color.White,
                        cursorColor = accentColor
                    )
                )

                /*OutlinedTextField(
                    value = uiState.repeatPlayerName,
                    onValueChange = { newName ->
                        registerViewModel.registerEventHandler(
                            RegisterEvent.UpdateRepeatPlayerName(newName)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    placeholder = {
                        Text(
                            text = "Repeat nickname",
                            color = Color.White.copy(alpha = 0.6f)
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = accentColor,
                        unfocusedBorderColor = accentColor.copy(alpha = 0.5f),
                        focusedContainerColor = textFieldColor,
                        unfocusedTextColor = Color.White,
                        focusedTextColor = Color.White,
                        cursorColor = accentColor
                    )
                )*/

                OutlinedTextField(
                    value = uiState.password,
                    onValueChange = { newPassword -> registerViewModel.registerEventHandler(RegisterEvent.UpdatePassword(newPassword)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    placeholder = {
                        Text(
                            text = "Enter password",
                            color = Color.White.copy(alpha = 0.6f)
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { registerViewModel.registerEventHandler(RegisterEvent.TogglePasswordVisibility) }) {
                            Icon(
                                painter = painterResource(uiState.passwordIcon),
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    },
                    visualTransformation = uiState.passwordVisualization,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = accentColor,
                        unfocusedBorderColor = accentColor.copy(alpha = 0.5f),
                        focusedContainerColor = textFieldColor,
                        unfocusedTextColor = Color.White,
                        focusedTextColor = Color.White,
                        cursorColor = accentColor
                    )
                )


                OutlinedTextField(
                    value = uiState.repeatPassword,
                    onValueChange = { newPassword -> registerViewModel.registerEventHandler(RegisterEvent.OnUpdateRepeatPassword(newPassword)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    placeholder = {
                        Text(
                            text = "Enter repeat password",
                            color = Color.White.copy(alpha = 0.6f)
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { registerViewModel.registerEventHandler(RegisterEvent.OnRepeatPasswordIconClick) }) {
                            Icon(
                                painter = painterResource(uiState.repeatPasswordIcon),
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    },
                    visualTransformation = uiState.repeatPasswordVisualization, colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = accentColor,
                        unfocusedBorderColor = accentColor.copy(alpha = 0.5f),
                        focusedContainerColor = textFieldColor,
                        unfocusedTextColor = Color.White,
                        focusedTextColor = Color.White,
                        cursorColor = accentColor
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                if( uiState.isTextMessageOn ) {
                    MinimalDialog(uiState.textMessage) { registerViewModel.registerEventHandler(RegisterEvent.OnDismiss) }
                }

                Button(
                    onClick = { registerViewModel.registerEventHandler(
                        RegisterEvent.OnConfirmCreateNewCharacter { onConfirm() }
                    ) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = accentColor,
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 8.dp
                    )
                ) {
                    Text(
                        text = "Create",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = "Back",
                    modifier = Modifier.clickable { onBack() },
                    color = accentColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline
                )

                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Preview
@Composable
fun CreateCharacterPreview() {
    CreateCharacter({},{}, viewModel())
}