package com.leafcellteam.mafia.roles

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource
import com.leafcellteam.mafia.resources.Res
import com.leafcellteam.mafia.resources.barman
import com.leafcellteam.mafia.resources.barman_description
import com.leafcellteam.mafia.resources.bomber
import com.leafcellteam.mafia.resources.bomber_description
import com.leafcellteam.mafia.resources.civilian
import com.leafcellteam.mafia.resources.civilian_description
import com.leafcellteam.mafia.resources.detective
import com.leafcellteam.mafia.resources.detective_description
import com.leafcellteam.mafia.resources.doctor
import com.leafcellteam.mafia.resources.doctor_description
import com.leafcellteam.mafia.resources.don
import com.leafcellteam.mafia.resources.don_description
import com.leafcellteam.mafia.resources.informator
import com.leafcellteam.mafia.resources.informator_description
import com.leafcellteam.mafia.resources.journalist
import com.leafcellteam.mafia.resources.journalist_description
import com.leafcellteam.mafia.resources.lover
import com.leafcellteam.mafia.resources.lover_description
import com.leafcellteam.mafia.resources.mafia
import com.leafcellteam.mafia.resources.mafia_description
import com.leafcellteam.mafia.resources.mimic
import com.leafcellteam.mafia.resources.mimic_description
import com.leafcellteam.mafia.resources.sherif
import com.leafcellteam.mafia.resources.sherif_description
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

sealed class Role(
    val descriptionRes: StringResource,
    val category: Category,
    val image: DrawableResource
) {
    enum class Category {
        CIVILIAN, MAFIA
    }

    @Composable
    fun getDescription(): String = stringResource(descriptionRes)

    data object Civilian : Role(
        descriptionRes = Res.string.civilian_description,
        category = Category.CIVILIAN,
        image = Res.drawable.civilian
    )

    data object Sherif : Role(
        descriptionRes = Res.string.sherif_description,
        category = Category.CIVILIAN,
        image = Res.drawable.sherif
    )

    data object Detective : Role(
        descriptionRes = Res.string.detective_description,
        category = Category.CIVILIAN,
        image = Res.drawable.detective
    )

    data object Doctor : Role(
        descriptionRes = Res.string.doctor_description,
        category = Category.CIVILIAN,
        image = Res.drawable.doctor
    )

    data object Lover : Role(
        descriptionRes = Res.string.lover_description,
        category = Category.CIVILIAN,
        image = Res.drawable.lover
    )

    data object Journalist : Role(
        descriptionRes = Res.string.journalist_description,
        category = Category.CIVILIAN,
        image = Res.drawable.journalist
    )

    data object Mimic : Role(
        descriptionRes = Res.string.mimic_description,
        category = Category.CIVILIAN,
        image = Res.drawable.mimic
    )

    data object Mafia : Role(
        descriptionRes = Res.string.mafia_description,
        category = Category.MAFIA,
        image = Res.drawable.mafia
    )

    data object Barman : Role(
        descriptionRes = Res.string.barman_description,
        category = Category.MAFIA,
        image = Res.drawable.barman
    )

    data object Informator : Role(
        descriptionRes = Res.string.informator_description,
        category = Category.MAFIA,
        image = Res.drawable.informator
    )

    data object Bomber : Role(
        descriptionRes = Res.string.bomber_description,
        category = Category.MAFIA,
        image = Res.drawable.bomber
    )

    data object Don : Role(
        descriptionRes = Res.string.don_description,
        category = Category.MAFIA,
        image = Res.drawable.don
    )

    companion object {
        fun fromName(name: String): Role = when (name.uppercase()) {
            "CIVILIAN" -> Civilian
            "SHERIF" -> Sherif
            "DETECTIVE" -> Detective
            "DOCTOR" -> Doctor
            "LOVER" -> Lover
            "JOURNALIST" -> Journalist
            "MIMIC" -> Mimic
            "MAFIA" -> Mafia
            "BARMAN" -> Barman
            "INFORMATOR" -> Informator
            "BOMBER" -> Bomber
            "DON" -> Don
            else -> Civilian
        }
    }
}