package com.leafcellteam.mafia.roles

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.leafcellteam.mafia.R
import com.leafcellteam.shared.roles.Role

fun Role.toImageRes(): Int = when (this) {
    is Role.Civilian -> R.drawable.civilian
    is Role.Sherif -> R.drawable.sherif
    is Role.Detective -> R.drawable.detective
    is Role.Doctor -> R.drawable.doctor
    is Role.Lover -> R.drawable.lover
    is Role.Journalist -> R.drawable.journalist
    is Role.Mimic -> R.drawable.mimic
    is Role.Mafia -> R.drawable.mafia
    is Role.Barman -> R.drawable.barman
    is Role.Informator -> R.drawable.informator
    is Role.Bomber -> R.drawable.bomber
    is Role.Don -> R.drawable.don
}

fun Role.toDescriptionRes(): Int = when (this) {
    is Role.Civilian -> R.string.civilian_description
    is Role.Sherif -> R.string.sherif_description
    is Role.Detective -> R.string.detective_description
    is Role.Doctor -> R.string.doctor_description
    is Role.Lover -> R.string.lover_description
    is Role.Journalist -> R.string.journalist_description
    is Role.Mimic -> R.string.mimic_description
    is Role.Mafia -> R.string.mafia_description
    is Role.Barman -> R.string.barman_description
    is Role.Informator -> R.string.informator_description
    is Role.Bomber -> R.string.bomber_description
    is Role.Don -> R.string.don_description
}

@Composable
fun Role.getDescription(): String = stringResource(this.toDescriptionRes())
