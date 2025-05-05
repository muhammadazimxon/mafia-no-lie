package com.example.mafiaonlinejetpackcomposecapi.mainMenu_JoinRoomSection

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mafiaonlinejetpackcomposecapi.R

@Composable
fun Room(
    id: String,
    quantity: Int,
    name: String,
    playersRange: IntRange,
    password: String,
    onJoin: (String) -> Unit,
    onPasswordDialogOpen: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .border(
                width = 2.dp,
                color = Color(141, 126, 162, 255),
                shape = RoundedCornerShape(15.dp)
            )
        ,
        shape = RoundedCornerShape(15.dp),
        color = Color(0xFF2E2E3E)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row {
                Text(
                    modifier = Modifier.width(60.dp).padding(start = 10.dp),
                    text = name,
                    fontSize = 15.sp,
                    color = Color.White,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    minLines = 1
                )
                Text(
                    text = " | ${playersRange.first}-${playersRange.last}",
                    modifier = Modifier.padding(start = 25.dp),
                    fontSize = 15.sp,
                    color = Color.White,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {

                    Text(
                        text = "$quantity",
                        modifier = Modifier.padding(start = 25.dp),
                        fontSize = 15.sp,
                        color = Color.White
                    )
                    Icon(
                        imageVector = Icons.Rounded.Person,
                        modifier = Modifier
                            .size(35.dp)
                            .padding(vertical = 3.dp)
                        ,
                        tint = Color.LightGray,
                        contentDescription = null,
                    )
                }

                if( password.isNotBlank() )
                    Icon(
                        painter = painterResource(id = R.drawable.locker2),
                        modifier = Modifier
                            .size(35.dp)
                            .padding(vertical = 3.dp, /*end = 12.dp*/)
                        ,
                        tint = Color.LightGray,
                        contentDescription = null,
                    )
                Button(
                    onClick = {
                        if( password.isNotBlank() )
                            onPasswordDialogOpen()
                        else
                            onJoin(id)
                    },
                    modifier = Modifier
                        .height(40.dp)
                        .width(80.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF7B68EE),
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Join",
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun RoomPreview() {
    Room(
        name = "name ",
        playersRange = 1..10,
        password = "",
        onJoin = {},
        onPasswordDialogOpen = {},
        id = "",
        quantity = 0
    )
}