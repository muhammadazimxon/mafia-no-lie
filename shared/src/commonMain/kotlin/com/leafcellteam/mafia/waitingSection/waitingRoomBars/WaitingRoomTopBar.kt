package com.leafcellteam.mafia.waitingSection.waitingRoomBars

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import org.jetbrains.compose.resources.painterResource
import androidx.compose.ui.text.style.TextOverflow
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leafcellteam.mafia.resources.*
import org.jetbrains.compose.resources.DrawableResource

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun WaitingRoomTopBar(
    roomName: String,
    chosenRoles: List<DrawableResource>,
    onBack: () -> Unit
) {
    TopAppBar(
        navigationIcon = { Icon(imageVector = Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "back", modifier = Modifier.clickable { onBack() }) },
        title = { Text(
            modifier = Modifier.padding(start = 5.dp).width(160.dp),
            text = roomName,
            fontSize = 20.sp,
            maxLines = 1,
            minLines = 1,
            overflow = TextOverflow.Ellipsis
        ) },
        colors = TopAppBarColors(
            containerColor = Color(0xFF1E1E2E),
            scrolledContainerColor = Color.Transparent,
            navigationIconContentColor = Color.White,
            titleContentColor = Color.White,
            actionIconContentColor = Color.Transparent
        ),
        actions = {
            if (chosenRoles.isNotEmpty()) {
                val totalIcons = chosenRoles.size
                val iconSize = when {
                    totalIcons <= 4 -> 26.dp
                    totalIcons <= 8 -> 22.dp
                    totalIcons <= 12 -> 18.dp
                    else -> 16.dp
                }

                Column(
                    modifier = Modifier
                        .border(2.dp, Color(0xFF7B68EE), RoundedCornerShape(5.dp))
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                        .sizeIn(maxWidth = 200.dp)
                ) {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        maxItemsInEachRow = 6
                    ) {
                        chosenRoles.forEach { roleResId ->
                            Image(
                                painter = painterResource(roleResId),
                                contentDescription = "role-image",
                                modifier = Modifier.size(iconSize),
                                colorFilter = when (roleResId) {
                                    Res.drawable.doctor,
                                    Res.drawable.journalist,
                                    Res.drawable.lover, Res.drawable.mimic -> ColorFilter.tint(Color.Cyan)
                                    Res.drawable.informator -> ColorFilter.tint(Color.Red)
                                    Res.drawable.don -> ColorFilter.tint(Color(166, 85, 85, 255))
                                    Res.drawable.mafia -> ColorFilter.tint(Color(166, 85, 85, 255))
                                    else -> null
                                }
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
@Preview
fun WaitingRoomTopBarPreview() {
    WaitingRoomTopBar(
        roomName = "Name",
        chosenRoles = listOf(
            Res.drawable.barman,
            Res.drawable.doctor,
            Res.drawable.sherif,
            Res.drawable.barman,
            Res.drawable.doctor,
            Res.drawable.sherif,
            Res.drawable.sherif
        ),
        {}
    )
}