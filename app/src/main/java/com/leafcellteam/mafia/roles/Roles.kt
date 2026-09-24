package com.leafcellteam.mafia.roles

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.leafcellteam.mafia.R

sealed class Role(
    val descriptionRes: Int,
    val category: Category,
    val image: Int
) {
    enum class Category {
        CIVILIAN, MAFIA
    }

    @Composable
    fun getDescription(): String = stringResource(descriptionRes)

    data object Civilian : Role(
        descriptionRes = R.string.civilian_description,
        category = Category.CIVILIAN,
        image = R.drawable.civilian
    )

    data object Sherif : Role(
        descriptionRes = R.string.sherif_description,
        category = Category.CIVILIAN,
        image = R.drawable.sherif
    )

    data object Detective : Role(
        descriptionRes = R.string.detective_description,
        category = Category.CIVILIAN,
        image = R.drawable.detective
    )

    data object Doctor : Role(
        descriptionRes = R.string.doctor_description,
        category = Category.CIVILIAN,
        image = R.drawable.doctor
    )

    data object Lover : Role(
        descriptionRes = R.string.lover_description,
        category = Category.CIVILIAN,
        image = R.drawable.lover
    )

    data object Journalist : Role(
        descriptionRes = R.string.journalist_description,
        category = Category.CIVILIAN,
        image = R.drawable.journalist
    )

    data object Mimic : Role(
        descriptionRes = R.string.mimic_description,
        category = Category.CIVILIAN,
        image = R.drawable.mimic
    )

    data object Mafia : Role(
        descriptionRes = R.string.mafia_description,
        category = Category.MAFIA,
        image = R.drawable.mafia
    )

    data object Barman : Role(
        descriptionRes = R.string.barman_description,
        category = Category.MAFIA,
        image = R.drawable.barman
    )

    data object Informator : Role(
        descriptionRes = R.string.informator_description,
        category = Category.MAFIA,
        image = R.drawable.informator
    )

    data object Bomber : Role(
        descriptionRes = R.string.bomber_description,
        category = Category.MAFIA,
        image = R.drawable.bomber
    )

    data object Don : Role(
        descriptionRes = R.string.don_description,
        category = Category.MAFIA,
        image = R.drawable.don
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