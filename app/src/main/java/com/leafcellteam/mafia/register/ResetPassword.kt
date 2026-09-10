package com.leafcellteam.mafia.register

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leafcellteam.mafia.loginAsGuest.accentColor
import com.leafcellteam.mafia.loginAsGuest.cardBackground
import com.leafcellteam.mafia.loginAsGuest.darkBackground
import com.leafcellteam.mafia.loginAsGuest.textFieldColor
import com.leafcellteam.mafia.retrofitService.retrofitModel.MafiaApi
import android.util.Patterns
import kotlinx.coroutines.launch
import com.leafcellteam.mafia.R

enum class ResetPasswordState {
    EMAIL_INPUT,
    PASSWORD_INPUT
}

@Composable
fun ResetPassword(
    onBack: () -> Unit = {},
    onSuccess: () -> Unit = {}
) {
    var currentState by remember { mutableStateOf(ResetPasswordState.EMAIL_INPUT) }
    var savedEmail by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        darkBackground,
                        darkBackground.copy(alpha = 0.8f)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        AnimatedContent(
            targetState = currentState,
            transitionSpec = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(400, easing = FastOutSlowInEasing)
                ) + fadeIn(animationSpec = tween(400)) togetherWith
                        slideOutHorizontally(
                            targetOffsetX = { -it },
                            animationSpec = tween(400, easing = FastOutSlowInEasing)
                        ) + fadeOut(animationSpec = tween(400))
            },
            label = "state_transition"
        ) { state ->
            when (state) {
                ResetPasswordState.EMAIL_INPUT -> EmailInputScreen(
                    onBack = onBack,
                    onSuccess = { email ->
                        savedEmail = email
                        currentState = ResetPasswordState.PASSWORD_INPUT
                    }
                )

                ResetPasswordState.PASSWORD_INPUT -> PasswordInputScreen(
                    email = savedEmail,
                    onBack = { currentState = ResetPasswordState.EMAIL_INPUT },
                    onSuccess = onSuccess
                )
            }
        }
    }
}

@Composable
private fun EmailInputScreen(
    onBack: () -> Unit,
    onSuccess: (String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var isSuccess by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val emailCannotBeEmpty = stringResource(R.string.emailCannotBeEmpty)
    val invalidEmailFormat = stringResource(R.string.invalidEmailFormat)
    val userNotFound = stringResource(R.string.userNotFound)
    val tooManyAttempts = stringResource(R.string.tooManyAttempts)
    val errorOccurred = stringResource(R.string.errorOccurred)
    val connectionError = stringResource(R.string.connectionError)

    fun validateEmail(email: String): String? {
        return when {
            email.isBlank() -> emailCannotBeEmpty
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> invalidEmailFormat
            else -> null
        }
    }

    ResetPasswordCard(
        icon = Icons.Default.Lock,
        title = stringResource(R.string.resetPassword),
        subtitle = stringResource(R.string.enterEmailForReset),
        isSuccess = isSuccess,
        successMessage = stringResource(R.string.codeSentToEmail)
    ) {
        CustomTextField(
            value = email,
            onValueChange = { newEmail ->
                email = newEmail
                errorMessage = if (newEmail.isNotBlank()) {
                    validateEmail(newEmail)
                } else null
            },
            placeholder = stringResource(R.string.exampleEmail),
            icon = Icons.Default.Email,
            isError = errorMessage != null,
            errorMessage = errorMessage,
            enabled = !isLoading && !isSuccess
        )

        Spacer(modifier = Modifier.height(8.dp))

        ActionButtons(
            onBack = onBack,
            onSubmit = {
                val validationError = validateEmail(email)
                if (validationError == null) {
                    isLoading = true
                    errorMessage = null

                    scope.launch {
                        try {
                            val response = MafiaApi.retrofitService.isAvailableForChangePassword(email)

                            if (response.isSuccessful) {
                                isLoading = false
                                isSuccess = true
                                kotlinx.coroutines.delay(1500)
                                onSuccess(email)
                            } else {
                                isLoading = false
                                errorMessage = when (response.code()) {
                                    404 -> userNotFound
                                    429 -> tooManyAttempts
                                    else -> response.message() ?: errorOccurred
                                }
                            }
                        } catch (e: Exception) {
                            isLoading = false
                            errorMessage = connectionError
                        }
                    }
                } else {
                    errorMessage = validationError
                }
            },
            isLoading = isLoading,
            isSuccess = isSuccess,
            isEnabled = email.isNotBlank() && errorMessage == null,
            submitText = if (isSuccess) stringResource(R.string.sent) else stringResource(R.string.sendCode)
        )

        if (!isSuccess) {
            Text(
                text = stringResource(R.string.weWillSendResetCode),
                color = Color.Gray,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                lineHeight = 16.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
private fun PasswordInputScreen(
    email: String,
    onBack: () -> Unit,
    onSuccess: () -> Unit
) {
    var confirmationCode by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repeatedPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var isSuccess by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val passwordMinLength = stringResource(R.string.passwordMinLength)
    val enterConfirmationCode = stringResource(R.string.enterConfirmationCode)
    val enterNewPassword = stringResource(R.string.enterNewPassword)
    val repeatPasswordText = stringResource(R.string.repeatPassword)
    val passwordsDoNotMatch = stringResource(R.string.passwordsDoNotMatch)
    val invalidConfirmationCode = stringResource(R.string.invalidConfirmationCode)
    val codeExpiredOrNotExist = stringResource(R.string.codeExpiredOrNotExist)
    val incorrectData = stringResource(R.string.incorrectData)
    val errorOccurred = stringResource(R.string.errorOccurred)
    val connectionError = stringResource(R.string.connectionError)

    fun validatePassword(password: String): String? {
        return if (password.length >= 6) null else passwordMinLength
    }

    fun validateForm(): String? {
        return when {
            confirmationCode.isBlank() -> enterConfirmationCode
            password.isBlank() -> enterNewPassword
            validatePassword(password) != null -> validatePassword(password)
            repeatedPassword.isBlank() -> repeatPasswordText
            password != repeatedPassword -> passwordsDoNotMatch
            else -> null
        }
    }

    ResetPasswordCard(
        icon = Icons.Default.Edit,
        title = stringResource(R.string.newPassword),
        subtitle = stringResource(R.string.enterCodeAndNewPassword),
        isSuccess = isSuccess,
        successMessage = stringResource(R.string.passwordSuccessfullyUpdated)
    ) {
        CustomTextField(
            value = confirmationCode,
            onValueChange = {
                confirmationCode = it
                errorMessage = null
            },
            placeholder = stringResource(R.string.codeFromEmail),
            icon = Icons.Default.Email,
            isError = false,
            enabled = !isLoading && !isSuccess
        )

        CustomTextField(
            value = password,
            onValueChange = {
                password = it
                errorMessage = null
            },
            placeholder = stringResource(R.string.newPassword),
            icon = Icons.Default.Lock,
            isError = false,
            enabled = !isLoading && !isSuccess,
            isPassword = true
        )

        CustomTextField(
            value = repeatedPassword,
            onValueChange = {
                repeatedPassword = it
                errorMessage = null
            },
            placeholder = stringResource(R.string.repeatPassword),
            icon = Icons.Default.Lock,
            isError = errorMessage != null,
            errorMessage = errorMessage,
            enabled = !isLoading && !isSuccess,
            isPassword = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        ActionButtons(
            onBack = onBack,
            onSubmit = {
                val validationError = validateForm()
                if (validationError == null) {
                    isLoading = true
                    errorMessage = null

                    scope.launch {
                        try {
                            val response = MafiaApi.retrofitService.resetPassword(
                                code = confirmationCode.trim(),
                                emailAddress = email,
                                password = password
                            )

                            if (response.isSuccessful) {
                                isLoading = false
                                isSuccess = true
                                kotlinx.coroutines.delay(1500)
                                onSuccess()
                            } else {
                                isLoading = false
                                errorMessage = when (response.code()) {
                                    400 -> invalidConfirmationCode
                                    404 -> codeExpiredOrNotExist
                                    422 -> incorrectData
                                    else -> response.message() ?: errorOccurred
                                }
                            }
                        } catch (e: Exception) {
                            isLoading = false
                            errorMessage = connectionError
                        }
                    }
                } else {
                    errorMessage = validationError
                }
            },
            isLoading = isLoading,
            isSuccess = isSuccess,
            isEnabled = confirmationCode.isNotBlank() &&
                    password.isNotBlank() &&
                    repeatedPassword.isNotBlank(),
            submitText = if (isSuccess) stringResource(R.string.done) else stringResource(R.string.updatePassword)
        )

        if (!isSuccess) {
            Text(
                text = stringResource(R.string.codeValidFor15Minutes),
                color = Color.Gray,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                lineHeight = 16.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
private fun ResetPasswordCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    isSuccess: Boolean,
    successMessage: String,
    content: @Composable ColumnScope.() -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isSuccess) 1.05f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "card_scale"
    )

    Card(
        modifier = Modifier
            .width(340.dp)
            .padding(16.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        colors = CardDefaults.cardColors(containerColor = cardBackground),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(28.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedIcon(icon = icon)

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = subtitle,
                    color = Color.LightGray,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            AnimatedVisibility(
                visible = isSuccess,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF4CAF50).copy(alpha = 0.15f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = successMessage,
                        color = Color(0xFF4CAF50),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            content()
        }
    }
}

@Composable
private fun AnimatedIcon(icon: ImageVector) {
    val rotation by rememberInfiniteTransition(label = "icon_rotation").animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "rotation"
    )

    Box(
        modifier = Modifier
            .size(64.dp)
            .background(
                color = accentColor.copy(alpha = 0.15f),
                shape = RoundedCornerShape(16.dp)
            )
            .graphicsLayer { rotationZ = rotation },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = accentColor,
            modifier = Modifier.size(32.dp)
        )
    }
}

@Composable
private fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    icon: ImageVector,
    isError: Boolean,
    errorMessage: String? = null,
    enabled: Boolean = true,
    isPassword: Boolean = false
) {
    var passwordVisible by remember { mutableStateOf(false) }
    if(isPassword) {
        Column {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = {
                    Text(placeholder, color = Color.Gray)
                },
                leadingIcon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (isError) Color.Red else accentColor
                    )
                },
                trailingIcon = {
                    if (isPassword) {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                painter = if (passwordVisible)
                                    painterResource(R.drawable.visibile)
                                else
                                    painterResource(R.drawable.visibility_off),
                                contentDescription = stringResource(R.string.showPassword),
                                tint = Color.White
                            )
                        }
                    }
                },
                singleLine = true,
                visualTransformation = if (isPassword && !passwordVisible)
                    PasswordVisualTransformation()
                else
                    VisualTransformation.None,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = textFieldColor,
                    unfocusedContainerColor = textFieldColor,
                    focusedBorderColor = accentColor,
                    unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f),
                    errorBorderColor = Color.Red,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = accentColor,
                    errorTextColor = Color.White,
                    errorContainerColor = textFieldColor,
                    focusedTrailingIconColor = Color.White,
                    unfocusedTrailingIconColor = Color.Gray
                ),
                isError = isError,
                enabled = enabled,
                modifier = Modifier.fillMaxWidth()
            )

            AnimatedVisibility(
                visible = isError && errorMessage != null,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Text(
                    text = errorMessage ?: "",
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }
        }
    } else {
        Column {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = {
                    Text(placeholder, color = Color.Gray)
                },
                leadingIcon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (isError) Color.Red else accentColor
                    )
                },
                singleLine = true,
                visualTransformation = if (isPassword && !passwordVisible)
                    PasswordVisualTransformation()
                else
                    VisualTransformation.None,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = textFieldColor,
                    unfocusedContainerColor = textFieldColor,
                    focusedBorderColor = accentColor,
                    unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f),
                    errorBorderColor = Color.Red,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = accentColor,
                    errorTextColor = Color.White,
                    errorContainerColor = textFieldColor,
                    focusedTrailingIconColor = Color.White,
                    unfocusedTrailingIconColor = Color.Gray
                ),
                isError = isError,
                enabled = enabled,
                modifier = Modifier.fillMaxWidth()
            )

            AnimatedVisibility(
                visible = isError && errorMessage != null,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Text(
                    text = errorMessage ?: "",
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun ActionButtons(
    onBack: () -> Unit,
    onSubmit: () -> Unit,
    isLoading: Boolean,
    isSuccess: Boolean,
    isEnabled: Boolean,
    submitText: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = onBack,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Gray.copy(alpha = 0.3f)
            ),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .weight(1f)
                .height(52.dp),
            enabled = !isLoading
        ) {
            Text(
                stringResource(R.string.back),
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Button(
            onClick = onSubmit,
            colors = ButtonDefaults.buttonColors(
                containerColor = accentColor,
                disabledContainerColor = accentColor.copy(alpha = 0.3f)
            ),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .weight(1.5f)
                .height(52.dp),
            enabled = !isLoading && !isSuccess && isEnabled
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = submitText,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ResetPasswordPreview() {
    ResetPassword()
}