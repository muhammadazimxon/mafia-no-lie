package com.leafcellteam.mafia.network

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.leafcellteam.mafia.resources.Res
import com.leafcellteam.mafia.resources.noInternetConnection
import com.leafcellteam.mafia.resources.pleaseCheckInternetConnection
import com.leafcellteam.mafia.resources.waitingForConnection

@Composable
fun NetworkStatusMonitor() {
    val context = LocalContext.current
    val networkMonitor = remember { NetworkMonitor(context) }
    val isConnected by networkMonitor.isConnected.collectAsState(initial = true)

    if (!isConnected) {
        NoInternetDialog()
    }
}

@Composable
private fun NoInternetDialog() {
    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1E1E2E)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "No Internet",
                    modifier = Modifier.size(64.dp),
                    tint = Color(0xFFFF6B6B)
                )

                Text(
                    text = stringResource(Res.string.noInternetConnection),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = stringResource(Res.string.pleaseCheckInternetConnection),
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )

                CircularProgressIndicator(
                    modifier = Modifier.size(32.dp),
                    color = Color(0xFF4A90E2),
                    strokeWidth = 3.dp
                )

                Text(
                    text = stringResource(Res.string.waitingForConnection),
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}