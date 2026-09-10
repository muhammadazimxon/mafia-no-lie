package com.leafcellteam.mafia.gameRoom.components.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leafcellteam.mafia.gameRoom.components.dialogs.base.BaseDialogCard
import com.leafcellteam.mafia.gameRoom.components.dialogs.base.IconRoleCircle

@Composable
fun CustomDialog(
    text: String,
    title: String,
    roleImage: Int?,
    onConfirm: () -> Unit,
    confirmText: String
) {
    BaseDialogCard(onDismiss = onConfirm) {
        Content(
            roleImage = roleImage,
            title = title,
            text = text,
            onConfirm = onConfirm,
            confirmText = confirmText
        )
    }
}

@Composable
private fun Content(
    roleImage: Int?,
    title: String,
    text: String,
    onConfirm: () -> Unit,
    confirmText: String
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        IconRoleCircle(roleImage)
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = text,
            fontSize = 16.sp,
            color = Color(0xFFB0B0B0),
            lineHeight = 20.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onConfirm,
            modifier = Modifier
                .width(80.dp)
                .height(36.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7B68EE))
        ) {
            Text(
                text = confirmText,
                fontSize = 14.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CustomDialogPreview() {
    CustomDialog(
        text = "asd",
        title = "Notification",
        onConfirm = { },
        confirmText = "OK",
        roleImage = null
    )
}