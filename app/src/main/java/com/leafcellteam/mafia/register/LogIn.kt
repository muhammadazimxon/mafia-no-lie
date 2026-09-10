package com.leafcellteam.mafia.register

import android.content.Context
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material.DropdownMenu
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.leafcellteam.mafia.R
import com.leafcellteam.mafia.appLanguage.dataStore
import com.leafcellteam.shared.localization.Language
import com.leafcellteam.mafia.creatingSection.components.MinimalDialog
import com.leafcellteam.shared.register.mvi.RegisterEvent
import com.leafcellteam.mafia.register.registerViewModel.RegisterViewModel
import com.leafcellteam.mafia.retrofitService.retrofitModel.MafiaApi
import com.leafcellteam.mafia.tokenPreferences
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

@Composable
fun LogIn(
    onLogIn: () -> Unit,
    onRegister: () -> Unit,
    onLoginAsGuest: () -> Unit,
    onResetPassword: () -> Unit,
    registerViewModel: RegisterViewModel,
    onLanguageSelected: (Language) -> Unit,
    context : Context = LocalContext.current
) {
    val uiState by registerViewModel.registerState.collectAsState()

    var isInitialLoading by remember { mutableStateOf(true) }
    var isButtonEnabled by remember { mutableStateOf(true) }
    val lifecycleOwner = LocalLifecycleOwner.current

    var expanded by remember { mutableStateOf(false) }
    var currentLang by remember { mutableStateOf(Language.English) }

    val languages = listOf(Language.English, Language.Russian, Language.Uzbek)

    LaunchedEffect(true) {
        currentLang = when(context.dataStore.data.map { it[stringPreferencesKey("language")] }.first()) {
            "ru" -> Language.Russian
            "uz" -> Language.Uzbek
            else -> Language.English
        }
    }

    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            isInitialLoading = true

            val maxAttempts = 5
            val delayMillisBetweenAttempts = 275L

            var attempt = 1
            var authenticated = false

            while (attempt <= maxAttempts && !authenticated) {
                try {
                    Log.d("Auth", "validateToken attempt #$attempt")
                    val authResponse = MafiaApi.retrofitService.validateToken(tokenPreferences.getIfGuest())

                    Log.d(
                        "Auth",
                        "Attempt #$attempt: ${authResponse.message} : ${authResponse.userName} : ${authResponse.userId} : ${authResponse.email} : ${authResponse.isAuthenticated}"
                    )

                    if (authResponse.isAuthenticated) {
                        registerViewModel.isGuest = authResponse.isGuest
                        registerViewModel.authValidation(
                            isGuest = authResponse.isGuest,
                            userId = authResponse.userId.toInt(),
                            email = authResponse.email,
                            authResponse.userName
                        )
                        registerViewModel.changeAchievementId(authResponse.userId.toInt())

                        onLogIn()
                        authenticated = true
                        break
                    } else {
                        if (attempt < maxAttempts) {
                            Log.d("Auth", "Not authenticated — retrying after delay")
                            delay(delayMillisBetweenAttempts)
                        }
                    }
                } catch (e: Exception) {
                    Log.d("Auth", "LogIn attempt #$attempt failed: ${e.message}")
                    if (attempt < maxAttempts) {
                        delay(delayMillisBetweenAttempts)
                    }
                }
                attempt++
            }

            if (!authenticated) {
                isInitialLoading = false
                Log.d("Auth", "All $maxAttempts attempts finished — showing login UI")
            }
        }
    }

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
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        painter = painterResource(R.drawable.informator),
                        contentDescription = "",
                        modifier = Modifier
                            .size(140.dp)
                            .background(accentColor.copy(alpha = 0.2f), CircleShape)
                            .align(Alignment.Center)
                    )

                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                    ) {
                        IconButton(
                            onClick = { expanded = true },
                            modifier = Modifier
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
                                .clip(CircleShape)
                        ) {
                            Text(
                                text = when (currentLang) {
                                    Language.English -> "EN"
                                    Language.Russian -> "RU"
                                    Language.Uzbek -> "UZ"
                                },
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.background(cardBackground),
                            offset = DpOffset(x = 0.dp, y = 0.dp)
                        ) {
                            languages.forEach { lang ->
                                androidx.compose.material3.DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = when (lang) {
                                                Language.English -> "English"
                                                Language.Russian -> "Русский"
                                                Language.Uzbek -> "Oʻzbek"
                                            },
                                            color = Color.White
                                        )
                                    },
                                    onClick = {
                                        currentLang = lang
                                        onLanguageSelected(lang)
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                Text(
                    text = stringResource(R.string.MAFIA_ONLINE),
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
                            text = "${stringResource(R.string.checkingAuthentication)}...",
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
                                text = stringResource(R.string.enterYourEmail),
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
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email
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
                                text = stringResource(R.string.enterPassword),
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
                            if(isButtonEnabled) {
                                isButtonEnabled = false
                                registerViewModel.registerEventHandler(RegisterEvent.OnLogIn(onLogIn) {
                                    isButtonEnabled = it
                                })
                            }
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
                            text = stringResource(R.string.logIn),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.register),
                            modifier = Modifier.clickable { onRegister() },
                            color = accentColor,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.Underline
                        )

                        Text(
                            text = "${stringResource(R.string.logIn)} ${stringResource(R.string.asAGuest_lowerCase)}",
                            modifier = Modifier.clickable { onLoginAsGuest() },
                            color = accentColor,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.Underline
                        )

                        Text(
                            text = stringResource(R.string.forgotPassword),
                            modifier = Modifier.clickable { onResetPassword() },
                            color = accentColor,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.Underline
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun LogInPreview() {
    LogIn({},{}, {}, {}, viewModel(), {})
}