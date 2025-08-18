//package com.example.mafiaonlinejetpackcomposecapi.register
//
//import android.util.Log
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.offset
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.layout.wrapContentHeight
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.OutlinedTextFieldDefaults
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.blur
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontStyle
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.text.style.TextDecoration
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import com.example.mafiaonlinejetpackcomposecapi.R
//import com.example.mafiaonlinejetpackcomposecapi.creatingSection.components.MinimalDialog
//import com.example.mafiaonlinejetpackcomposecapi.register.registerMvi.RegisterEvent
//import com.example.mafiaonlinejetpackcomposecapi.register.registerViewModel.RegisterViewModel
//import com.example.mafiaonlinejetpackcomposecapi.retrofitService.retrofitModel.MafiaApi
//import com.example.mafiaonlinejetpackcomposecapi.sharedPreferences.TokenPreferences
//
//@Composable
//fun LogIn(
//    onLogIn: () -> Unit,
//    onRegister: () -> Unit,
//    registerViewModel: RegisterViewModel,
//) {
//
//    LaunchedEffect(true) {
//        try {
//            val authResponse = MafiaApi.retrofitService.validateToken()
//            Log.d("Auth", "${authResponse.message} : ${authResponse.userName} : ${authResponse.userId} : ${authResponse.email} : ${authResponse.isAuthenticated}")
//            if( authResponse.isAuthenticated ) {
//                registerViewModel.authValidation(authResponse.userId.toInt(), authResponse.email, authResponse.userName)
//                registerViewModel.changeAchievementId(authResponse.userId.toInt())
//                onLogIn()
//            }
//        } catch (e: Exception) {
//            Log.d("Auth", "LogIn: ${e.message}")
//        }
//    }
//
//    val uiState by registerViewModel.registerState.collectAsState()
//
//    val darkBackground = Color(0xFF1E1E2E)
//    val cardBackground = Color(0xFF282838)
//    val accentColor = Color(0xFF7B68EE)
//    val textFieldColor = Color(0xFF3D3D5B)
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(darkBackground),
//        contentAlignment = Alignment.Center
//    ) {
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp)
//        ) {
//            Box(
//                modifier = Modifier
//                    .size(200.dp)
//                    .align(Alignment.TopEnd)
//                    .offset(x = 50.dp, y = (-50).dp)
//                    .background(
//                        brush = Brush.radialGradient(
//                            colors = listOf(
//                                accentColor.copy(alpha = 0.3f),
//                                Color.Transparent
//                            ),
//                            radius = 200f
//                        ),
//                        shape = CircleShape
//                    )
//                    .blur(30.dp)
//            )
//
//            Box(
//                modifier = Modifier
//                    .size(250.dp)
//                    .align(Alignment.BottomStart)
//                    .offset(x = (-70).dp, y = 70.dp)
//                    .background(
//                        brush = Brush.radialGradient(
//                            colors = listOf(accentColor.copy(alpha = 0.2f), Color.Transparent),
//                            radius = 250f
//                        ),
//                        shape = CircleShape
//                    )
//                    .blur(40.dp)
//            )
//        }
//
//        Card(
//            modifier = Modifier
//                .width(450.dp)
//                .wrapContentHeight()
//                .padding(16.dp),
//            shape = RoundedCornerShape(24.dp),
//            colors = CardDefaults.cardColors(containerColor = cardBackground),
//            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
//        ) {
//            Column(
//                modifier = Modifier.padding(30.dp),
//                verticalArrangement = Arrangement.spacedBy(20.dp),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Icon(
//                    painter = painterResource(R.drawable.informator),
//                    contentDescription = "",
//                    modifier = Modifier
//                        .size(140.dp)
//                        .background(accentColor.copy(alpha = 0.2f), CircleShape)
//                )
//
//                Text(
//                    text = "MAFIA ONLINE",
//                    fontSize = 38.sp,
//                    fontStyle = FontStyle.Italic,
//                    fontWeight = FontWeight.Bold,
//                    color = Color.White,
//                    textAlign = TextAlign.Center
//                )
//
//                Spacer(modifier = Modifier.height(10.dp))
//
//                OutlinedTextField(
//                    value = uiState.email,
//                    onValueChange = { newEmail -> registerViewModel.registerEventHandler(RegisterEvent.UpdateEmail(newEmail)) },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(60.dp),
//                    shape = RoundedCornerShape(16.dp),
//                    singleLine = true,
//                    placeholder = {
//                        Text(
//                            text = "Enter your email",
//                            color = Color.White.copy(alpha = 0.6f)
//                        )
//                    },
//                    colors = OutlinedTextFieldDefaults.colors(
//                        focusedBorderColor = accentColor,
//                        unfocusedBorderColor = accentColor.copy(alpha = 0.5f),
//                        focusedContainerColor = textFieldColor,
//                        unfocusedTextColor = Color.White,
//                        focusedTextColor = Color.White,
//                        cursorColor = accentColor
//                    )
//                )
//
//                OutlinedTextField(
//                    value = uiState.password,
//                    onValueChange = { newPassword -> registerViewModel.registerEventHandler(RegisterEvent.UpdatePassword(newPassword)) },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(60.dp),
//                    shape = RoundedCornerShape(16.dp),
//                    singleLine = true,
//                    placeholder = {
//                        Text(
//                            text = "Enter password",
//                            color = Color.White.copy(alpha = 0.6f)
//                        )
//                    },
//                    trailingIcon = {
//                        IconButton(onClick = { registerViewModel.registerEventHandler(RegisterEvent.TogglePasswordVisibility) }) {
//                            Icon(
//                                painter = painterResource(uiState.passwordIcon),
//                                contentDescription = null,
//                                tint = Color.White
//                            )
//                        }
//                    },
//                    visualTransformation = uiState.passwordVisualization,
//                    colors = OutlinedTextFieldDefaults.colors(
//                        focusedBorderColor = accentColor,
//                        unfocusedBorderColor = accentColor.copy(alpha = 0.5f),
//                        focusedContainerColor = textFieldColor,
//                        unfocusedTextColor = Color.White,
//                        focusedTextColor = Color.White,
//                        cursorColor = accentColor
//                    )
//                )
//
//                Spacer(modifier = Modifier.height(20.dp))
//
//                if( uiState.isTextMessageOn ) {
//                    MinimalDialog(uiState.textMessage) { registerViewModel.registerEventHandler(RegisterEvent.OnDismiss) }
//                }
//
//                Button(
//                    onClick = {
//                        registerViewModel.registerEventHandler(RegisterEvent.OnLogIn(onLogIn))
//                    },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(55.dp),
//                    shape = RoundedCornerShape(16.dp),
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = accentColor,
//                        contentColor = Color.White
//                    ),
//                    elevation = ButtonDefaults.buttonElevation(
//                        defaultElevation = 4.dp,
//                        pressedElevation = 8.dp
//                    )
//                ) {
//                    Text(
//                        text = "Log In",
//                        fontSize = 18.sp,
//                        fontWeight = FontWeight.Bold
//                    )
//                }
//
//                Text(
//                    text = "Register",
//                    modifier = Modifier.clickable { onRegister() },
//                    color = accentColor,
//                    fontSize = 18.sp,
//                    fontWeight = FontWeight.Bold,
//                    textDecoration = TextDecoration.Underline
//                )
//
//                Spacer(modifier = Modifier.height(10.dp))
//            }
//        }
//    }
//}
//
//@Preview
//@Composable
//fun LogInPreview() {
//    LogIn({},{}, viewModel())
//}

package com.example.mafiaonlinejetpackcomposecapi.register

import android.util.Log
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.mafiaonlinejetpackcomposecapi.retrofitService.retrofitModel.MafiaApi
import com.example.mafiaonlinejetpackcomposecapi.sharedPreferences.TokenPreferences

@Composable
fun LogIn(
    onLogIn: () -> Unit,
    onRegister: () -> Unit,
    registerViewModel: RegisterViewModel,
) {
    var isInitialLoading by remember { mutableStateOf(true) }

    LaunchedEffect(true) {
        try {
            val authResponse = MafiaApi.retrofitService.validateToken()
            Log.d("Auth", "${authResponse.message} : ${authResponse.userName} : ${authResponse.userId} : ${authResponse.email} : ${authResponse.isAuthenticated}")

            if( authResponse.isAuthenticated ) {
                registerViewModel.authValidation(authResponse.userId.toInt(), authResponse.email, authResponse.userName)
                registerViewModel.changeAchievementId(authResponse.userId.toInt())
                onLogIn()
            } else {
                isInitialLoading = false
            }
        } catch (e: Exception) {
            Log.d("Auth", "LogIn: ${e.message}")
            isInitialLoading = false
        }
    }

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
                modifier = Modifier.padding(30.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    painter = painterResource(R.drawable.informator),
                    contentDescription = "",
                    modifier = Modifier
                        .size(140.dp)
                        .background(accentColor.copy(alpha = 0.2f), CircleShape)
                )

                Text(
                    text = "MAFIA ONLINE",
                    fontSize = 38.sp,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(10.dp))

                if (isInitialLoading) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(40.dp),
                            color = accentColor,
                            strokeWidth = 3.dp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Checking authentication...",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 14.sp
                        )
                    }
                } else {
                    OutlinedTextField(
                        value = uiState.email,
                        onValueChange = { newEmail -> registerViewModel.registerEventHandler(RegisterEvent.UpdateEmail(newEmail)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true,
                        placeholder = {
                            Text(
                                text = "Enter your email",
                                color = Color.White.copy(alpha = 0.6f)
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = accentColor,
                            unfocusedBorderColor = accentColor.copy(alpha = 0.5f),
                            focusedContainerColor = textFieldColor,
                            unfocusedContainerColor = textFieldColor,
                            unfocusedTextColor = Color.White,
                            focusedTextColor = Color.White,
                            cursorColor = accentColor
                        )
                    )

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
                            unfocusedContainerColor = textFieldColor,
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
                        onClick = {
                            registerViewModel.registerEventHandler(RegisterEvent.OnLogIn(onLogIn))
                        },
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
                            text = "Log In",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = "Register",
                        modifier = Modifier.clickable { onRegister() },
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
}

@Preview
@Composable
fun LogInPreview() {
    LogIn({},{}, viewModel())
}