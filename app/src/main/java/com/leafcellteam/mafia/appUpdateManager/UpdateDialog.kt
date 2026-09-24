package com.leafcellteam.mafia.appUpdateManager

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.leafcellteam.mafia.R

@Composable
fun AppUpdateDialogs(updateState: AppUpdateState) {
    when (val state = updateState.dialogState) {
        is UpdateDialogState.Available -> {
            UpdateAvailableDialog(
                isImmediate = state.isImmediate,
                onUpdate = {
                    updateState.startUpdate(state.info, state.isImmediate)
                },
                onDismiss = updateState.dismissDialog
            )
        }
        is UpdateDialogState.Downloading -> {
            DownloadingDialog(progress = state.progress)
        }
        is UpdateDialogState.ReadyToInstall -> {
            InstallReadyDialog(
                onInstall = updateState.completeUpdate
            )
        }
        UpdateDialogState.None -> {  }
    }
}

@Composable
private fun UpdateAvailableDialog(
    isImmediate: Boolean,
    onUpdate: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = { if (!isImmediate) onDismiss() },
        properties = DialogProperties(
            dismissOnBackPress = !isImmediate,
            dismissOnClickOutside = !isImmediate
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
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Update",
                    modifier = Modifier.size(64.dp),
                    tint = Color(0xFF4A90E2)
                )

                Text(
                    text = if (isImmediate) stringResource(R.string.needsUpdate) else stringResource(R.string.updateIsAvailable),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = if (isImmediate) {
                        stringResource(R.string.toContinueNeedsUpdate)
                    } else {
                        stringResource(R.string.aNewVersionOfTheApp)
                    },
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (!isImmediate) {
                        OutlinedButton(
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color.White
                            )
                        ) {
                            Text(stringResource(R.string.laterUppercase))
                        }
                    }

                    Button(
                        onClick = onUpdate,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4A90E2)
                        )
                    ) {
                        Text(stringResource(R.string.update))
                    }
                }
            }
        }
    }
}

@Composable
private fun DownloadingDialog(progress: Int) {
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
                    painter = painterResource(R.drawable.baseline_system_update_alt_24),
                    contentDescription = "Downloading",
                    modifier = Modifier.size(64.dp),
                    tint = Color(0xFF4A90E2)
                )

                Text(
                    text = stringResource(R.string.downloadingUpdate),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LinearProgressIndicator(
                        progress = { progress / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp),
                        color = Color(0xFF4A90E2),
                        trackColor = Color(0xFF2D2D3D),
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "$progress%",
                        fontSize = 16.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun InstallReadyDialog(onInstall: () -> Unit) {
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
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Ready",
                    modifier = Modifier.size(64.dp),
                    tint = Color(0xFF4CAF50)
                )

                Text(
                    text = stringResource(R.string.theUpdateIsReady),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = stringResource(R.string.theUpdateHasBeenDownloaded),
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )

                Button(
                    onClick = onInstall,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50)
                    )
                ) {
                    Text(stringResource(R.string.installNow))
                }
            }
        }
    }
}

@Preview(name = "Available - Flexible", showBackground = true)
@Composable
fun PreviewAvailableFlexible() {
    AppUpdateDialogs(
        updateState = AppUpdateState(
            dialogState = UpdateDialogState.Available(
                info = null as AppUpdateInfo,
                isFlexible = true,
                isImmediate = false
            ),
            checkForUpdates = {},
            startUpdate = { _, _ -> },
            completeUpdate = {},
            dismissDialog = {}
        )
    )
}