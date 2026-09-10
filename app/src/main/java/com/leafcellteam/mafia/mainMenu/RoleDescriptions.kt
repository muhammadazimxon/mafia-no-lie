package com.leafcellteam.mafia.mainMenu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.leafcellteam.mafia.R

data class RoleDescription(
    val roleName: String,
    val description: String,
    val ability: String,
    val faction: String = "Civilian",
    val exception: String? = null
)

@Composable
fun RoleDescriptions(paddingValues: PaddingValues, sheetState: ModalBottomSheetState) {
    val civilian = stringResource(com.leafcellteam.mafia.R.string.civilian)
    val sheriff = stringResource(com.leafcellteam.mafia.R.string.sheriff)
    val detective = stringResource(com.leafcellteam.mafia.R.string.detective)
    val doctor = stringResource(com.leafcellteam.mafia.R.string.doctor)
    val lover = stringResource(com.leafcellteam.mafia.R.string.lover)
    val journalist = stringResource(com.leafcellteam.mafia.R.string.journalist)
    val mimic = stringResource(com.leafcellteam.mafia.R.string.mimic)
    val mafia = stringResource(com.leafcellteam.mafia.R.string.mafia)
    val barman = stringResource(com.leafcellteam.mafia.R.string.barman)
    val informator = stringResource(com.leafcellteam.mafia.R.string.informator)
    val bomber = stringResource(com.leafcellteam.mafia.R.string.bomber)
    val don = stringResource(com.leafcellteam.mafia.R.string.don)

    val civilianDescription = stringResource(com.leafcellteam.mafia.R.string.civilianDescription_)
    val sheriffDescription = stringResource(com.leafcellteam.mafia.R.string.sheriffDescription_)
    val detectiveDescription = stringResource(com.leafcellteam.mafia.R.string.detectiveDescription_)
    val doctorDescription = stringResource(com.leafcellteam.mafia.R.string.doctorDescription_)
    val loverDescription = stringResource(com.leafcellteam.mafia.R.string.loverDescription_)
    val journalistDescription = stringResource(com.leafcellteam.mafia.R.string.journalistDescription_)
    val mimicDescription = stringResource(com.leafcellteam.mafia.R.string.mimicDescription_)
    val mafiaDescription = stringResource(com.leafcellteam.mafia.R.string.mafiaDescription_)
    val barmanDescription = stringResource(com.leafcellteam.mafia.R.string.barmanDescription_)
    val informatorDescription = stringResource(com.leafcellteam.mafia.R.string.informatorDescription_)
    val bomberDescription = stringResource(com.leafcellteam.mafia.R.string.bomberDescription_)
    val donDescription = stringResource(com.leafcellteam.mafia.R.string.donDescription_)

    val civilianAbility = stringResource(com.leafcellteam.mafia.R.string.civilianAbility_)
    val sheriffAbility = stringResource(com.leafcellteam.mafia.R.string.sheriffAbility_)
    val detectiveAbility = stringResource(com.leafcellteam.mafia.R.string.detectiveAbility_)
    val doctorAbility = stringResource(com.leafcellteam.mafia.R.string.doctorAbility_)
    val loverAbility = stringResource(com.leafcellteam.mafia.R.string.loverAbility_)
    val journalistAbility = stringResource(com.leafcellteam.mafia.R.string.journalistAbility_)
    val mimicAbility = stringResource(com.leafcellteam.mafia.R.string.mimicAbility_)
    val mafiaAbility = stringResource(com.leafcellteam.mafia.R.string.mafiaAbility_)
    val barmanAbility = stringResource(com.leafcellteam.mafia.R.string.barmanAbility_)
    val informatorAbility = stringResource(com.leafcellteam.mafia.R.string.informatorAbility_)
    val bomberAbility = stringResource(com.leafcellteam.mafia.R.string.bomberAbility_)
    val donAbility = stringResource(com.leafcellteam.mafia.R.string.donAbility_)

    val barmanException = stringResource(com.leafcellteam.mafia.R.string.barmanException_)
    val informatorException = stringResource(com.leafcellteam.mafia.R.string.informatorException_)
    val bomberException = stringResource(R.string.bomberException_)

    val roles = remember {
        listOf(
            RoleDescription(
                roleName = civilian,
                description = civilianDescription,
                ability = civilianAbility
            ),
            RoleDescription(
                roleName = sheriff,
                description = sheriffDescription,
                ability = sheriffAbility
            ),
            RoleDescription(
                roleName = detective,
                description = detectiveDescription,
                ability = detectiveAbility
            ),
            RoleDescription(
                roleName = doctor,
                description = doctorDescription,
                ability = doctorAbility
            ),
            RoleDescription(
                roleName = lover,
                description = loverDescription,
                ability = loverAbility
            ),
            RoleDescription(
                roleName = journalist,
                description = journalistDescription,
                ability = journalistAbility
            ),
            RoleDescription(
                roleName = mimic,
                description = mimicDescription,
                ability = mimicAbility
            ),

            RoleDescription(
                roleName = mafia,
                description = mafiaDescription,
                ability = mafiaAbility,
                "Mafia"
            ),
            RoleDescription(
                roleName = barman,
                description = barmanDescription,
                ability = barmanAbility,
                "Mafia",
                exception = barmanException
            ),
            RoleDescription(
                roleName = informator,
                description = informatorDescription,
                ability = informatorAbility,
                "Mafia",
                exception = informatorException
            ),
            RoleDescription(
                roleName = bomber,
                description = bomberDescription,
                ability = bomberAbility,
                "Mafia",
                exception = bomberException
            ),
            RoleDescription(
                roleName = don,
                description = donDescription,
                ability = donAbility,
                "Mafia"
            )
        )
    }


    RoleAndDescriptionSection(roles, paddingValues, sheetState)
}

@Composable
fun RoleAndDescriptionSection(roles: List<RoleDescription>, paddingValues: PaddingValues, sheetState: ModalBottomSheetState) {
    val cardColors = listOf(Color(0xFF7B68EE), Color(0xFF1E1E2E))

    val civilianRoles = roles.filter { it.faction == "Civilian" }
    val mafiaRoles = roles.filter { it.faction == "Mafia" }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .padding(if(sheetState.currentValue == ModalBottomSheetValue.Expanded) paddingValues else PaddingValues(0.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item {
            RoleCard(RoleDescription(roleName = stringResource(R.string.gameDescription), ability = stringResource(R.string.gameAbility), description = stringResource(R.string.gameDescriptionDetail)), isNotInclude = true)
        }
        if (civilianRoles.isNotEmpty()) {
            item {
                Text(
                    text = stringResource(R.string.civilianRoles),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            items(civilianRoles) { role ->
                RoleCard(role)
            }
        }

        if (mafiaRoles.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(R.string.mafiaRoles),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            items(mafiaRoles) { role ->
                RoleCard(role)
            }
        }
    }
}

@Composable
fun RoleCard(role: RoleDescription, isNotInclude: Boolean = false) {
    val cardColors = listOf(
        Color(0xFF49349F),
        Color(0xFF1E1E2E)
    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF282838))
    ) {
        Column(
            modifier = Modifier
                .background(
                    brush = Brush.horizontalGradient(
                        colors = cardColors,
                        startX = 0f,
                        endX = 1000f
                    )
                )
                .padding(16.dp)
        ) {
            Text(
                text = role.roleName,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            if(isNotInclude.not()) {
                Text(
                    text = "${stringResource(R.string.ability)}: ${role.ability}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White.copy(alpha = 0.8f)
                )
            } else {
                Text(
                    text = "${stringResource(R.string.functionality)}: ${role.ability}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = role.description,
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.8f)
            )
            role.exception?.let {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${stringResource(R.string.exception)}: $it",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Yellow
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRoleDescriptions() {
    var modalBottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    RoleDescriptions(
        paddingValues = PaddingValues(0.dp),
        sheetState = modalBottomSheetState
    )
}
