package com.example.mafiaonlinejetpackcomposecapi

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mafiaonlinejetpackcomposecapi.retrofitService.MafiaApi
import com.example.mafiaonlinejetpackcomposecapi.retrofitService.PlayerRegisterDataRequest
import com.example.mafiaonlinejetpackcomposecapi.viewModel.WaitingRoomViewModel
import kotlinx.coroutines.launch

@Composable
fun Register(
    onLogIn: () -> Unit,
    waitingViewModel: WaitingRoomViewModel
){
    var password by remember { mutableStateOf("") }
    var passwordVisualization by remember { mutableStateOf(VisualTransformation.None) }
    var passwordIcon by remember { mutableIntStateOf(R.drawable.visibile) }
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E1E2E))
        ,
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .height(360.dp)
                .width(450.dp)
                .background(Color(0xFF3D3D5B))
                .padding(vertical = 25.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            TextField(
                value = waitingViewModel.currentPlayer,
                onValueChange = { waitingViewModel.currentPlayer = it },
                modifier = Modifier.clip(RoundedCornerShape(20.dp)),
                singleLine = true,
                placeholder = {
                    Text(
                        text = "Enter name"
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(141, 126, 162, 255),
                    unfocusedContainerColor = Color(158, 142, 182, 255),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )
            TextField(
                value = password,
                onValueChange = { password = it },
                placeholder = {
                    Text(
                        text = "Enter password"
                    )
                },
                modifier = Modifier.clip(RoundedCornerShape(20.dp)),
                singleLine = true,
                trailingIcon = {
                    IconButton(
                        onClick = {


                            if( passwordVisualization == VisualTransformation.None ) {
                                passwordVisualization = PasswordVisualTransformation()
                                passwordIcon = R.drawable.visibility_off
                            }
                            else {
                                passwordVisualization = VisualTransformation.None
                                passwordIcon = R.drawable.visibile
                            }
                        }
                    ) { Icon(painter = painterResource(passwordIcon), contentDescription = null, tint = Color.White) }

                },
                visualTransformation = passwordVisualization,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(141, 126, 162, 255),
                    unfocusedContainerColor = Color(158, 142, 182, 255),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    coroutineScope.launch {
                        try {
                            waitingViewModel.playerId = MafiaApi.retrofitService.register(PlayerRegisterDataRequest(waitingViewModel.currentPlayer))

//                            if ( response.isSuccessful ) {
//                                val player = response.body()
//                                println("✅ Успешная регистрация: $player")
                            onLogIn()
//                            } else {
//                                val errorBody = response.errorBody()?.string()
//                                println("❌ Ошибка регистрации. Код: ${response.code()}, тело: $errorBody")
//                            }
                        } catch (e: Exception) {
                            println("💥 Ошибка при запросе: ${e.message}")
                        }
                    }


                },
                modifier = Modifier.width(150.dp).height(45.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF7B68EE),
                    contentColor = Color.White
                )
            ){
                Text(
                    text = "Log In",
                    fontSize = 20.sp
                )
            }
            Button(
                onClick = {},
                modifier = Modifier.width(150.dp).height(45.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF7B68EE),
                    contentColor = Color.White
                )
            ){
                Text(
                    text = "Register",
                    fontSize = 20.sp
                )
            }
        }
    }
}

@Preview
@Composable
fun RegisterPreview(){
    Register({}, viewModel())
}
