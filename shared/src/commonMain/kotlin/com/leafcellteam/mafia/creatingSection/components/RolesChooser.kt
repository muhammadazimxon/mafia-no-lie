package com.leafcellteam.mafia.creatingSection.components

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
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leafcellteam.mafia.resources.Res
import com.leafcellteam.mafia.creatingSection.models.RolesChooserParam
import com.leafcellteam.mafia.resources.baseline_help_outline_24
import com.leafcellteam.mafia.resources.doctor
import com.leafcellteam.mafia.resources.don
import com.leafcellteam.mafia.resources.enable
import com.leafcellteam.mafia.resources.informator
import com.leafcellteam.mafia.resources.journalist
import com.leafcellteam.mafia.resources.lover
import com.leafcellteam.mafia.resources.mimic
import com.leafcellteam.mafia.resources.role
import com.leafcellteam.mafia.roles.Role

@Composable
fun RolesChooser(rolesChooserParam: RolesChooserParam) {
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
                    painter = painterResource(rolesChooserParam.image),
                    contentDescription = "role-image",
                    colorFilter = when (rolesChooserParam.image) {
                        Res.drawable.doctor, Res.drawable.journalist, Res.drawable.mimic, Res.drawable.lover ->
                            ColorFilter.tint(Color.Cyan)
                        Res.drawable.informator -> ColorFilter.tint(Color.Red)
                        Res.drawable.don -> ColorFilter.tint(Color.Magenta)
                        else -> null
                    }
                )
                IconButton(
                    onClick = { rolesChooserParam.helpButtonClick(rolesChooserParam.position) },
                    modifier = Modifier.size(25.dp)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.baseline_help_outline_24),
                        contentDescription = null,
                        tint = Color(222, 222, 222, 255)
                    )
                }
                Text(
                    text = stringResource(Res.string.enable) + " " + stringResource(Res.string.role) + " " + if(rolesChooserParam.role is Role.Bomber) "Engineer saboteur\n" else rolesChooserParam.role,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W500,
                    color = Color.White
                )
            }
            Checkbox(
                checked = rolesChooserParam.value,
                modifier = Modifier.size(30.dp),
                enabled = rolesChooserParam.onClick(rolesChooserParam),
                onCheckedChange = { rolesChooserParam.onCheckedChange(it) },
                colors = CheckboxColors(
                    checkedCheckmarkColor = Color(155, 0, 0, 255),
                    uncheckedCheckmarkColor = Color.Gray,
                    checkedBoxColor = Color(232, 112, 112, 255),
                    uncheckedBoxColor = Color.LightGray,
                    disabledCheckedBoxColor = Color.Gray,
                    disabledUncheckedBoxColor = Color.Gray,
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
@Preview
fun RolesChooserPreview() {
    var state by remember { mutableStateOf(false) }
    RolesChooser(
        RolesChooserParam(
            value = state,
            onCheckedChange = { state = it },
            helpButtonClick = {},
            position = 0,
            role = Role.Informator,
            image = Res.drawable.don,
            onClick = { false }
        ),
    )
}