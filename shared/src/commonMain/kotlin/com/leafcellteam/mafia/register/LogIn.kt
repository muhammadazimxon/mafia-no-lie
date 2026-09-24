package com.leafcellteam.mafia.register

import com.leafcellteam.mafia.logd
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.leafcellteam.mafia.resources.Res
import com.leafcellteam.mafia.resources.somethingWentWrong
import com.leafcellteam.mafia.resources.MAFIA_ONLINE
import com.leafcellteam.mafia.resources.checkingAuthentication
import com.leafcellteam.mafia.resources.tryAgain
import com.leafcellteam.mafia.resources.enterYourEmail
import com.leafcellteam.mafia.resources.enterPassword
import com.leafcellteam.mafia.resources.logIn
import com.leafcellteam.mafia.resources.register
import com.leafcellteam.mafia.resources.asAGuest_lowerCase
import com.leafcellteam.mafia.resources.forgotPassword
import com.leafcellteam.mafia.resources.informator
import com.leafcellteam.mafia.appLanguage.AppSettings
import com.leafcellteam.mafia.appLanguage.languages.Language
import com.leafcellteam.mafia.creatingSection.components.MinimalDialog
import com.leafcellteam.mafia.register.registerMvi.RegisterEvent
import com.leafcellteam.mafia.register.registerViewModel.RegisterViewModel
import com.leafcellteam.mafia.retrofitService.retrofitModel.MafiaApi
import com.leafcellteam.mafia.tokenPreferences
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

sealed class AuthState
object Loading : AuthState()
data class Error(val message: String, val onRetry: () -> Unit) : AuthState()
object Content : AuthState()

@Composable
fun LogIn(
    onLogIn: () -> Unit,
    onRegister: () -> Unit,
    onLoginAsGuest: () -> Unit,
    onResetPassword: () -> Unit,
    registerViewModel: RegisterViewModel,
    onLanguageSelected: (Language) -> Unit,
) {
    val uiState by registerViewModel.registerState.collectAsState()

    var authState by remember { mutableStateOf<AuthState>(Loading) }
    var isButtonEnabled by remember { mutableStateOf(true) }
    val authScope = rememberCoroutineScope()

    var expanded by remember { mutableStateOf(false) }
    var currentLang by remember { mutableStateOf(Language.English) }

    val languages = listOf(Language.English, Language.Russian, Language.Uzbek)

    val somethingWentWrongError = stringResource(Res.string.somethingWentWrong)

    LaunchedEffect(true) {
        currentLang = AppSettings.getLanguage()
    }

    fun startAuthCheck() {
        authState = Loading
        authScope.launch {
            val maxAttempts = 5
            val delayMillisBetweenAttempts = 275L

            var attempt = 1
            var authenticated = false

            while (attempt <= maxAttempts && !authenticated) {
                try {
                    logd("Auth", "validateToken attempt #$attempt")
                    val authResponse = MafiaApi.retrofitService.validateToken(tokenPreferences.getIfGuest())

                    logd(
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
                            logd("Auth", "Not authenticated — retrying after delay")
                            delay(delayMillisBetweenAttempts)
                        }
                    }
                } catch (e: Exception) {
                    logd("Auth", "LogIn attempt #$attempt failed: ${e.message}")
                    if (attempt < maxAttempts) {
                        delay(delayMillisBetweenAttempts)
                    }
                }
                attempt++
            }

            if (!authenticated) {
                authState = Error(
                    message = somethingWentWrongError,
                    onRetry = { startAuthCheck() }
                )
                logd("Auth", "All $maxAttempts attempts finished — showing error")
            }
        }
    }

    LaunchedEffect(Unit) {
        startAuthCheck()
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
                        painter = painterResource(Res.drawable.informator),
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
                                DropdownMenuItem(
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
                    text = stringResource(Res.string.MAFIA_ONLINE),
                    fontSize = 38.sp,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(10.dp))

                when (authState) {
                    Loading -> {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Spacer(modifier = Modifier.height(30.dp))
                            Text(
                                text = "${stringResource(Res.string.checkingAuthentication)}...",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 16.sp
                            )
                        }
                    }
                    is Error -> {
                        val errorState = authState as Error
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = errorState.message,
                                color = Color.Red,
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center
                            )
                            Button(
                                onClick = errorState.onRetry,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(55.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = accentColor,
                                    contentColor = Color.White
                                )
                            ) {
                                Text(
                                    text = stringResource(Res.string.tryAgain),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                    Content -> {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
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
                                        text = stringResource(Res.string.enterYourEmail),
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
                                        text = stringResource(Res.string.enterPassword),
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

                            if (uiState.isTextMessageOn) {
                                MinimalDialog(uiState.textMessage) { registerViewModel.registerEventHandler(RegisterEvent.OnDismiss) }
                            }

                            Button(
                                onClick = {
                                    if (isButtonEnabled) {
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
                                    text = stringResource(Res.string.logIn),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(20.dp)
                            ) {
                                Text(
                                    text = stringResource(Res.string.register),
                                    modifier = Modifier.clickable { onRegister() },
                                    color = accentColor,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    textDecoration = TextDecoration.Underline
                                )

                                Text(
                                    text = "${stringResource(Res.string.logIn)} ${stringResource(Res.string.asAGuest_lowerCase)}",
                                    modifier = Modifier.clickable { onLoginAsGuest() },
                                    color = accentColor,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    textDecoration = TextDecoration.Underline
                                )

                                Text(
                                    text = stringResource(Res.string.forgotPassword),
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
    }
}

@Preview
@Composable
fun LogInPreview() {
    LogIn({},{}, {}, {}, viewModel(), {})
}