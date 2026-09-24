package com.leafcellteam.mafia.appUpdateManager

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability

@Composable
fun rememberAppUpdateManager(): AppUpdateState {
    val context = LocalContext.current
    val activity = context as? ComponentActivity
        ?: throw IllegalStateException("Context must be ComponentActivity")

    val updateManager = remember { AppUpdateManagerFactory.create(context) }
    var updateState by remember { mutableStateOf<UpdateDialogState>(UpdateDialogState.None) }
    var downloadProgress by remember { mutableIntStateOf(0) }

    val updateLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode != Activity.RESULT_OK) {
            updateState = UpdateDialogState.None
        }
    }

    val listener = remember {
        InstallStateUpdatedListener { state ->
            when (state.installStatus()) {
                InstallStatus.DOWNLOADING -> {
                    val total = state.totalBytesToDownload()
                    val downloaded = state.bytesDownloaded()
                    downloadProgress = if (total > 0) {
                        ((downloaded * 100) / total).toInt()
                    } else 0
                    updateState = UpdateDialogState.Downloading(downloadProgress)
                }
                InstallStatus.DOWNLOADED -> {
                    updateState = UpdateDialogState.ReadyToInstall
                }
                InstallStatus.INSTALLED -> {
                    updateState = UpdateDialogState.None
                }
                InstallStatus.FAILED -> {
                    updateState = UpdateDialogState.None
                }
                else -> {}
            }
        }
    }

    DisposableEffect(updateManager) {
        updateManager.registerListener(listener)
        onDispose {
            updateManager.unregisterListener(listener)
        }
    }

    val checkForUpdates: () -> Unit = {
        updateManager.appUpdateInfo.addOnSuccessListener { info ->
            when {
                info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE -> {
                    val isFlexible = info.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
                    val isImmediate = info.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)

                    updateState = UpdateDialogState.Available(
                        info = info,
                        isFlexible = isFlexible,
                        isImmediate = isImmediate
                    )
                }
                info.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS -> {
                    if (info.installStatus() == InstallStatus.DOWNLOADED) {
                        updateState = UpdateDialogState.ReadyToInstall
                    }
                }
            }
        }
    }

    val startUpdate: (AppUpdateInfo, Boolean) -> Unit = { info, isImmediate ->
        try {
            val updateType = if (isImmediate) AppUpdateType.IMMEDIATE else AppUpdateType.FLEXIBLE

            updateManager.startUpdateFlowForResult(
                info,
                updateLauncher,
                AppUpdateOptions.newBuilder(updateType).build()
            )
        } catch (e: Exception) {
            updateState = UpdateDialogState.None
        }
    }

    val completeUpdate: () -> Unit = {
        updateManager.completeUpdate()
    }

    return AppUpdateState(
        dialogState = updateState,
        checkForUpdates = checkForUpdates,
        startUpdate = startUpdate,
        completeUpdate = completeUpdate,
        dismissDialog = { updateState = UpdateDialogState.None }
    )
}

data class AppUpdateState(
    val dialogState: UpdateDialogState,
    val checkForUpdates: () -> Unit,
    val startUpdate: (AppUpdateInfo, Boolean) -> Unit,
    val completeUpdate: () -> Unit,
    val dismissDialog: () -> Unit
)

sealed class UpdateDialogState {
    object None : UpdateDialogState()
    data class Available(
        val info: AppUpdateInfo,
        val isFlexible: Boolean,
        val isImmediate: Boolean
    ) : UpdateDialogState()
    data class Downloading(val progress: Int) : UpdateDialogState()
    object ReadyToInstall : UpdateDialogState()
}