package com.leafcellteam.mafia.mainMenu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.leafcellteam.mafia.resources.Res
import com.leafcellteam.mafia.resources.civilian
import com.leafcellteam.mafia.resources.sheriff
import com.leafcellteam.mafia.resources.detective
import com.leafcellteam.mafia.resources.doctor
import com.leafcellteam.mafia.resources.lover
import com.leafcellteam.mafia.resources.journalist
import com.leafcellteam.mafia.resources.mimic
import com.leafcellteam.mafia.resources.mafia
import com.leafcellteam.mafia.resources.barman
import com.leafcellteam.mafia.resources.informator
import com.leafcellteam.mafia.resources.bomber
import com.leafcellteam.mafia.resources.don
import com.leafcellteam.mafia.resources.civilianDescription_
import com.leafcellteam.mafia.resources.sheriffDescription_
import com.leafcellteam.mafia.resources.detectiveDescription_
import com.leafcellteam.mafia.resources.doctorDescription_
import com.leafcellteam.mafia.resources.loverDescription_
import com.leafcellteam.mafia.resources.journalistDescription_
import com.leafcellteam.mafia.resources.mimicDescription_
import com.leafcellteam.mafia.resources.mafiaDescription_
import com.leafcellteam.mafia.resources.barmanDescription_
import com.leafcellteam.mafia.resources.informatorDescription_
import com.leafcellteam.mafia.resources.bomberDescription_
import com.leafcellteam.mafia.resources.donDescription_
import com.leafcellteam.mafia.resources.civilianAbility_
import com.leafcellteam.mafia.resources.sheriffAbility_
import com.leafcellteam.mafia.resources.detectiveAbility_
import com.leafcellteam.mafia.resources.doctorAbility_
import com.leafcellteam.mafia.resources.loverAbility_
import com.leafcellteam.mafia.resources.journalistAbility_
import com.leafcellteam.mafia.resources.mimicAbility_
import com.leafcellteam.mafia.resources.mafiaAbility_
import com.leafcellteam.mafia.resources.barmanAbility_
import com.leafcellteam.mafia.resources.informatorAbility_
import com.leafcellteam.mafia.resources.bomberAbility_
import com.leafcellteam.mafia.resources.donAbility_
import com.leafcellteam.mafia.resources.barmanException_
import com.leafcellteam.mafia.resources.informatorException_
import com.leafcellteam.mafia.resources.bomberException_
import com.leafcellteam.mafia.resources.gameDescription
import com.leafcellteam.mafia.resources.gameAbility
import com.leafcellteam.mafia.resources.gameDescriptionDetail
import com.leafcellteam.mafia.resources.civilianRoles
import com.leafcellteam.mafia.resources.mafiaRoles
import com.leafcellteam.mafia.resources.ability
import com.leafcellteam.mafia.resources.functionality
import com.leafcellteam.mafia.resources.exception

data class RoleDescription(
    val roleName: String,
    val description: String,
    val ability: String,
    val faction: String = "Civilian",
    val exception: String? = null
)

@Composable
fun RoleDescriptions(paddingValues: PaddingValues) {
    val civilian = stringResource(Res.string.civilian)
    val sheriff = stringResource(Res.string.sheriff)
    val detective = stringResource(Res.string.detective)
    val doctor = stringResource(Res.string.doctor)
    val lover = stringResource(Res.string.lover)
    val journalist = stringResource(Res.string.journalist)
    val mimic = stringResource(Res.string.mimic)
    val mafia = stringResource(Res.string.mafia)
    val barman = stringResource(Res.string.barman)
    val informator = stringResource(Res.string.informator)
    val bomber = stringResource(Res.string.bomber)
    val don = stringResource(Res.string.don)

    val civilianDescription = stringResource(Res.string.civilianDescription_)
    val sheriffDescription = stringResource(Res.string.sheriffDescription_)
    val detectiveDescription = stringResource(Res.string.detectiveDescription_)
    val doctorDescription = stringResource(Res.string.doctorDescription_)
    val loverDescription = stringResource(Res.string.loverDescription_)
    val journalistDescription = stringResource(Res.string.journalistDescription_)
    val mimicDescription = stringResource(Res.string.mimicDescription_)
    val mafiaDescription = stringResource(Res.string.mafiaDescription_)
    val barmanDescription = stringResource(Res.string.barmanDescription_)
    val informatorDescription = stringResource(Res.string.informatorDescription_)
    val bomberDescription = stringResource(Res.string.bomberDescription_)
    val donDescription = stringResource(Res.string.donDescription_)

    val civilianAbility = stringResource(Res.string.civilianAbility_)
    val sheriffAbility = stringResource(Res.string.sheriffAbility_)
    val detectiveAbility = stringResource(Res.string.detectiveAbility_)
    val doctorAbility = stringResource(Res.string.doctorAbility_)
    val loverAbility = stringResource(Res.string.loverAbility_)
    val journalistAbility = stringResource(Res.string.journalistAbility_)
    val mimicAbility = stringResource(Res.string.mimicAbility_)
    val mafiaAbility = stringResource(Res.string.mafiaAbility_)
    val barmanAbility = stringResource(Res.string.barmanAbility_)
    val informatorAbility = stringResource(Res.string.informatorAbility_)
    val bomberAbility = stringResource(Res.string.bomberAbility_)
    val donAbility = stringResource(Res.string.donAbility_)

    val barmanException = stringResource(Res.string.barmanException_)
    val informatorException = stringResource(Res.string.informatorException_)
    val bomberException = stringResource(Res.string.bomberException_)

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


    RoleAndDescriptionSection(roles, paddingValues)
}

@Composable
fun RoleAndDescriptionSection(roles: List<RoleDescription>, paddingValues: PaddingValues) {
    val cardColors = listOf(Color(0xFF7B68EE), Color(0xFF1E1E2E))

    val civilianRoles = roles.filter { it.faction == "Civilian" }
    val mafiaRoles = roles.filter { it.faction == "Mafia" }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .padding(paddingValues)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item {
            RoleCard(RoleDescription(roleName = stringResource(Res.string.gameDescription), ability = stringResource(Res.string.gameAbility), description = stringResource(Res.string.gameDescriptionDetail)), isNotInclude = true)
        }
        if (civilianRoles.isNotEmpty()) {
            item {
                Text(
                    text = stringResource(Res.string.civilianRoles),
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
                    text = stringResource(Res.string.mafiaRoles),
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
                    text = "${stringResource(Res.string.ability)}: ${role.ability}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White.copy(alpha = 0.8f)
                )
            } else {
                Text(
                    text = "${stringResource(Res.string.functionality)}: ${role.ability}",
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
                    text = "${stringResource(Res.string.exception)}: $it",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Yellow
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewRoleDescriptions() {
    RoleDescriptions(
        paddingValues = PaddingValues(0.dp)
    )
}
