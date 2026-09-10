package com.leafcellteam.mafia.loginAsGuest

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.leafcellteam.mafia.register.registerViewModel.RegisterViewModel
import com.leafcellteam.mafia.retrofitService.retrofitModel.MafiaApi
import com.leafcellteam.mafia.tokenManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.leafcellteam.mafia.R
import com.leafcellteam.mafia.contentModerationService.ContentModerationService

val darkBackground = Color(0xFF1E1E2E)
val cardBackground = Color(0xFF282838)
val accentColor = Color(0xFF7B68EE)
val textFieldColor = Color(0xFF3D3D5B)

@Composable
fun LoginAsGuestScreen(
    onBack: () -> Unit,
    registerViewModel: RegisterViewModel,
    onContinue: () -> Unit,
) {
    var guestName by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(registerViewModel.context.getString(R.string.pleaseEnterName)) }
    var isCorrectToCreate by remember { mutableStateOf(false) }
    var isAlreadyCreated by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(darkBackground),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .width(300.dp)
                .height(300.dp),
            colors = CardDefaults.cardColors(containerColor = cardBackground),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.loginAsGuest),
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                OutlinedTextField(
                    value = guestName,
                    onValueChange = { name ->
                        guestName = name

                        errorMessage = when {
                            name.isBlank() -> registerViewModel.context.getString(R.string.nameCannotBeEmpty)
                            name.length < 3 -> registerViewModel.context.getString(R.string.nameMustContain)
                            name.length > 20 -> registerViewModel.context.getString(R.string.nameTooLong)
                            ContentModerationService.containsProhibitedContent(name) ->
                                registerViewModel.context.getString(R.string.nameContainsInappropriateContent)
                            !name.matches(Regex("^[a-zA-Zа-яА-ЯёЁ0-9_\\-]+$")) ->
                                registerViewModel.context.getString(R.string.nameContainsInvalidCharacters)
                            else -> null
                        }

                        if (errorMessage == null) {
                            isCorrectToCreate = false
                            CoroutineScope(Dispatchers.Main).launch {
                                val guestIsExists = MafiaApi.retrofitService.checkForGuestName(name)
                                if (guestIsExists.isSuccessful.not()) {
                                    errorMessage = registerViewModel.context.getString(R.string.somethingWentWrong)
                                    isCorrectToCreate = false
                                } else if(guestIsExists.body() ?: true) {
                                    errorMessage = registerViewModel.context.getString(R.string.thisNameIsAlreadyUsed)
                                    isCorrectToCreate = false
                                } else if(guestIsExists.isSuccessful && guestIsExists.body()?.not() ?: false) {
                                    isCorrectToCreate = true
                                }
                            }
                        } else {
                            isCorrectToCreate = false
                        }
                    },
                    placeholder = { Text(text = registerViewModel.context.getString(R.string.enterUniqueName), color = Color.LightGray) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = textFieldColor,
                        unfocusedContainerColor = textFieldColor,
                        focusedBorderColor = accentColor,
                        unfocusedBorderColor = Color.Gray,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = accentColor,
                        errorTextColor = Color.White,
                        errorContainerColor = darkBackground
                    ),
                    isError = errorMessage != null,
                    supportingText = {
                        if (errorMessage != null) {
                            Text(text = errorMessage!!, color = Color.Red, fontSize = 14.sp)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = onBack,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(stringResource(R.string.back), color = Color.White)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Button(
                        onClick = {
                            if(isCorrectToCreate && isAlreadyCreated.not()) {
                                try {
                                    isAlreadyCreated = true
                                    CoroutineScope(Dispatchers.Main).launch {
                                        val response =
                                            MafiaApi.retrofitService.requestToLoginAsGuest(guestName)
                                        if(response.isSuccessful) {
                                            delay(100)
                                            registerViewModel.authValidationAsGuest(
                                                userId = response.body()!!.userId,
                                                name = guestName,
                                                accessToken = response.body()!!.accessToken,
                                                refreshToken = response.body()!!.refreshToken
                                            )
                                            registerViewModel.changeAchievementId(response.body()!!.userId)
                                            registerViewModel.isGuest = true
                                            tokenManager.saveIfGuest(true)
                                            onContinue()
                                        } else {
                                            errorMessage = registerViewModel.context.getString(R.string.loginFailed)
                                            isAlreadyCreated = false
                                        }
                                    }
                                } catch (e: Exception) {
                                    errorMessage = registerViewModel.context.getString(R.string.somethingWentWrong)
                                    isCorrectToCreate = false
                                    isAlreadyCreated = false
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = accentColor,
                            disabledContainerColor = accentColor.copy(alpha = 0.15f)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1.2f),
                        enabled = errorMessage == null && guestName.isNotBlank()
                    ) {
                        Text(registerViewModel.context.getString(R.string.continue_), color = Color.White)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginAsGuestScreenPreview() {
    LoginAsGuestScreen(
        onBack = {},
        onContinue = {},
        registerViewModel = viewModel()
    )
}
