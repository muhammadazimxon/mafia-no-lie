package com.example.mafiaonlinejetpackcomposecapi.creatingSection.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mafiaonlinejetpackcomposecapi.R
import com.example.mafiaonlinejetpackcomposecapi.roles.Role

@Composable
fun RolesChooser(
    value: Boolean,
    imageId: Int,
    role: Role,
    position: Int,
    onCheckedChange: (Boolean) -> Unit,
    helpButtonClick: (Int) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    modifier = Modifier.size(30.dp),
                    painter = painterResource(imageId),
                    contentDescription = "role-image",
                    colorFilter = when (imageId) {
                        R.drawable.doctor, R.drawable.journalist, R.drawable.lover ->
                            ColorFilter.tint(Color.Cyan)
                        R.drawable.informator -> ColorFilter.tint(Color.Red)
                        R.drawable.don -> ColorFilter.tint(Color.Magenta)
                        else -> null
                    }
                )
                IconButton(
                    onClick = { helpButtonClick(position) },
                    modifier = Modifier.size(25.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_help_outline_24),
                        contentDescription = null,
                        tint = Color(222, 222, 222, 255)
                    )
                }
                Text(
                    text = "Enable $role role",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W500,
                    color = Color.White
                )
            }
            Checkbox(
                checked = value,
                modifier = Modifier.size(30.dp),
                onCheckedChange = onCheckedChange,
                colors = CheckboxColors(
                    checkedCheckmarkColor = Color(155, 0, 0, 255),
                    uncheckedCheckmarkColor = Color.Gray,
                    checkedBoxColor = Color(232, 112, 112, 255),
                    uncheckedBoxColor = Color.LightGray,
                    disabledCheckedBoxColor = Color.Gray,
                    disabledUncheckedBoxColor = Color.LightGray,
                    disabledIndeterminateBoxColor = Color.Transparent,
                    checkedBorderColor = Color(182, 1, 52, 255),
                    uncheckedBorderColor = Color.DarkGray,
                    disabledBorderColor = Color.Transparent,
                    disabledUncheckedBorderColor = Color.Transparent,
                    disabledIndeterminateBorderColor = Color.Transparent
                )
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun RolesChooserPreview() {
    var state by remember { mutableStateOf(false) }
    RolesChooser(
        value = state,
        onCheckedChange = { state = it },
        helpButtonClick = {},
        position = 0,
        role = Role.Informator,
        imageId = R.drawable.don
    )
}