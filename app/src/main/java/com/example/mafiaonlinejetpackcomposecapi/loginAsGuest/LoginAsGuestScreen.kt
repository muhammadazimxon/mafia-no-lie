package com.example.mafiaonlinejetpackcomposecapi.loginAsGuest

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mafiaonlinejetpackcomposecapi.register.registerViewModel.RegisterViewModel
import com.example.mafiaonlinejetpackcomposecapi.retrofitService.retrofitModel.MafiaApi
import com.example.mafiaonlinejetpackcomposecapi.tokenManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
    var errorMessage by remember { mutableStateOf<String?>("Please enter name") }
    var isCorrectToCreate by remember { mutableStateOf(false) }

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
                    text = "Login as guest",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                OutlinedTextField(
                    value = guestName,
                    onValueChange = { name ->
                        guestName = name

                        errorMessage = when {
                            name.isBlank() -> "Name cannot be empty"
                            name.length < 3 -> "Name should contain at least 3 characters"
                            else -> null
                        }

                        if (errorMessage == null) {
                            CoroutineScope(Dispatchers.Main).launch {
                                val guestIsExists = MafiaApi.retrofitService.checkForGuestName(name)
                                if (guestIsExists.isSuccessful.not()) {
                                    errorMessage = "Something went wrong"
                                    isCorrectToCreate = false
                                } else if(guestIsExists.body() ?: true) {
                                    errorMessage = "This name is already used"
                                    isCorrectToCreate = false
                                } else if(guestIsExists.isSuccessful && guestIsExists.body()?.not() ?: false) {
                                    isCorrectToCreate = true
                                }
                            }
                        }
                    },
                    placeholder = { Text("Enter unique name", color = Color.LightGray) },
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
                        Text("Back", color = Color.White)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Button(
                        onClick = {
                            if(isCorrectToCreate) {
                                CoroutineScope(Dispatchers.Main).launch {
                                    val response = MafiaApi.retrofitService.requestToLoginAsGuest(guestName)
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
                        Text("Continue", color = Color.White)
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
