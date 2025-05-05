package com.example.mafiaonlinejetpackcomposecapi.gameRoom

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mafiaonlinejetpackcomposecapi.models.gameRoom.eventInfo.EventInfo
import com.example.mafiaonlinejetpackcomposecapi.models.gameRoom.playerModels.Player
import com.example.mafiaonlinejetpackcomposecapi.models.gameRoom.playerModels.PlayerRoleDto
import com.example.mafiaonlinejetpackcomposecapi.models.gameRoom.chatModel.ChatMessage
import com.example.mafiaonlinejetpackcomposecapi.roles.Role

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MafiaGameRoom(
    modifier: Modifier,
    currentPlayerId: Int
) {
    val playersDto = listOf(
        PlayerRoleDto(1, "Abricos", "MAFIA", listOf(200, 50, 50), true),
        PlayerRoleDto(2, "Ulug'bek", "CIVILIAN", listOf(50, 200, 50), true),
        PlayerRoleDto(3, "Kotlin", "CIVILIAN", listOf(50, 50, 200), true),
        PlayerRoleDto(4, "Java", "DON", listOf(200, 200, 50), true),
        PlayerRoleDto(5, "JavaScript", "INFORMATOR", listOf(200, 100, 0), true),
        PlayerRoleDto(6, "C#", "SHERIF", listOf(100, 200, 200), true)
    )

    // Parse DTOs into Player models
    val players = remember {
        mutableStateListOf<Player>().apply {
            playersDto.forEach { dto ->
                Role.fromName(dto.role)?.let { role ->
                    val color = Color(dto.avatarColor[0], dto.avatarColor[1], dto.avatarColor[2])
                    add(Player(dto.playerId, dto.playerName, role, color, dto.isAlive))
                }
            }
        }
    }

    val eventInfo by remember { mutableStateOf(
        listOf(
            EventInfo(day = 1, message = "Abricos was killed by mafia"),
            EventInfo(day = 2, message = "Sherif knows someone's role"),
            EventInfo(day = 3, message = "Journalist anounced Ulugbek is in another team with Kotlin"),
            EventInfo(day = 4, message = "Bomber blow up Javascript")
        )
    ) }
    val currentPlayer = players.find { it.playerId == currentPlayerId }
        ?: error("Current player with id=$currentPlayerId not found")

    val messages = remember {
        mutableStateListOf<ChatMessage>(
            ChatMessage(1,"Игрок 1", "Всем привет! Начинаем игру."),
            ChatMessage(2,"Игрок 3", "Кого подозреваете?"),
            ChatMessage(3,"Игрок 5", "Мне кажется, 7-й игрок ведет себя подозрительно.")
        )
    }

    var messageInput by remember { mutableStateOf("") }

    Row(modifier = Modifier.fillMaxSize().background(Color(0xFF1E1E2E)).then(modifier)) {
        Box(
            modifier = Modifier
                .width(80.dp)
                .fillMaxHeight()
                .background(Color(0xFF1E1E2E))
                .padding(8.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = "Players:",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(players) { player ->
                        PlayerCard(player, currentPlayer)
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .background(Color(0xFF282838))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(8.dp)
            ) {
                GameInfo(players, eventInfo)
            }

            Column(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(
                        Color(0xFF20212C),
                        RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    )
            ) {
                Text(
                    text = "Discusion",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(messages) { message ->
                        ChatMessageItem(message, players.find { it.playerId == message.id })
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = messageInput,
                        onValueChange = { messageInput = it },
                        minLines = 1,
                        maxLines = 1,
                        placeholder = { Text("Введите сообщение...") },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color(0xFF7B68EE),
                            unfocusedBorderColor = Color.Gray,
                            focusedTextColor = Color.White
                        )
                    )

                    IconButton(
                        onClick = {
                            if (messageInput.isNotBlank()) {
                                messages.add(ChatMessage(1, "Игрок 1", messageInput))
                                messageInput = ""
                            }
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color(0xFF7B68EE), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Отправить",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PlayerCard(player: Player, currentPlayer: Player) {
    val backgroundColor = when {
        !player.isAlive -> Color(0x33777777)
        currentPlayer.role.category == Role.Category.MAFIA && player.role.category == Role.Category.MAFIA -> Color(0x33FF0000)
        else -> Color(0x3300FF00)
    }
    Card(
        modifier = Modifier.width(80.dp).border(2.dp, if (player.isAlive) Color(0xFF7B68EE) else Color.Gray, RoundedCornerShape(8.dp)),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Box {
                Box(
                    modifier = Modifier.size(40.dp).clip(CircleShape).background(player.avatarColor),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = player.playerName.first().toString(),
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Image(
                    painter = painterResource(id = player.role.image),
                    contentDescription = player.role.toString(),
                    modifier = Modifier.size(16.dp).align(Alignment.BottomEnd).background(Color.White, CircleShape).border(1.dp, Color.DarkGray, CircleShape),
                    contentScale = ContentScale.Crop
                )
                if (!player.isAlive) {
                    Box(modifier = Modifier.matchParentSize().clip(CircleShape).background(Color(0x88000000)))
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = player.playerName,
                    fontSize = 14.sp,
                    color = if (player.isAlive) Color.White else Color.Gray,
                    maxLines = 1,
                    minLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.W400
                )
                if (!player.isAlive) {
                    Text(text = "Killed", color = Color.Red, fontSize = 14.sp)
                    Text(text = "Role: ${player.role}", color = Color.White, fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
fun GameInfo(players: List<Player>, eventInfo: List<EventInfo>) {
    val aliveCount = players.count { it.isAlive }
    val mafiaCount = players.count { it.isAlive && it.role.category == Role.Category.MAFIA }
    Column(modifier = Modifier.fillMaxSize()) {
        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFF2E2E3E))) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Game on process", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Alive: $aliveCount players", color = Color(0xFF00FF00), fontSize = 14.sp)
                Text("Mafia: $mafiaCount", color = Color(0xFFFF0000), fontSize = 14.sp)
                Spacer(modifier = Modifier.height(10.dp))
                Text("Current phase: Discussion", color = Color(0xFFFFD700), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text("Until vote: 60 sec", color = Color(0xFFFFD700), fontSize = 12.sp)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFF2E2E3E))) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Game events:", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                LazyColumn {
                    items(eventInfo) {
                        EventItem("#${it.day}. ${it.message}")
                    }
                }
            }
        }
    }
}

@Composable
fun EventItem(text: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), verticalAlignment = Alignment.Top) {
        Box(modifier = Modifier.size(16.dp).background(Color(0xFFFFD700), CircleShape))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, color = Color.White, fontSize = 14.sp)
    }
}

@Composable
fun ChatMessageItem(message: ChatMessage, player: Player?) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
        player?.let {
            Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(it.avatarColor), contentAlignment = Alignment.Center) {
                Text(it.playerName.first().toString(), color = Color.White)
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(message.playerName, color = Color(0xFF7B68EE), fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Card(modifier = Modifier.padding(top = 4.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF32334D))) {
                Text(message.message, color = Color.White, modifier = Modifier.padding(8.dp))
            }
        }
    }
}


@Composable
@Preview(showBackground = true)
fun MafiaGameRoomPreview() {
    MafiaGameRoom(
        modifier = Modifier,
        currentPlayerId = 2
    )
}