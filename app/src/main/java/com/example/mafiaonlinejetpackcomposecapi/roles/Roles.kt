package com.example.mafiaonlinejetpackcomposecapi.roles

import com.example.mafiaonlinejetpackcomposecapi.R

sealed class Role(
    val description: String,
    val category: Category,
    val image: Int
) {
    enum class Category {
        CIVILIAN, MAFIA
    }
    data object Sherif : Role(
        description = "Checks one player per night to see if they are Mafia.",
        category = Category.CIVILIAN,
        image = R.drawable.sherif
    )
    data object Detective : Role(
        description = "Investigates players' alignment.",
        category = Category.CIVILIAN,
        image = R.drawable.detective
    )
    data object Doctor : Role(
        description = "Saves one player per night.",
        category = Category.CIVILIAN,
        image = R.drawable.doctor
    )
    data object Lover : Role(
        description = "Falls in love — if one dies, the other too.",
        category = Category.CIVILIAN,
        image = R.drawable.lover
    )
    data object Mimic : Role(
        description = "Copies another player's ability.",
        category = Category.CIVILIAN,
        image = R.drawable.mimic
    )
    data object Journalist : Role(
        description = "Compares two people’s alignments.",
        category = Category.CIVILIAN,
        image = R.drawable.journalist
    )
    data object Civilian : Role(
        description = "A regular townsperson.",
        category = Category.CIVILIAN,
        image = R.drawable.civilian
    )
    data object Barman : Role(
        description = "Drunks one player, disabling their ability.",
        category = Category.MAFIA,
        image = R.drawable.barman
    )
    data object Informator : Role(
        description = "Receives info about other roles.",
        category = Category.MAFIA,
        image = R.drawable.informator
    )
    data object Don : Role(
        description = "Leads the Mafia, can cancel a kill.",
        category = Category.MAFIA,
        image = R.drawable.don
    )
    data object Bomber : Role(
        description = "Upon death, blows up someone with them.",
        category = Category.MAFIA,
        image = R.drawable.bomber
    )
    data object Mafia : Role(
        description = "Kills people.",
        category = Category.MAFIA,
        image = R.drawable.mafia
    )
    companion object {
        fun fromName(name: String): Role? = when (name.uppercase()) {
            "SHERIF" -> Sherif
            "DETECTIVE" -> Detective
            "DOCTOR" -> Doctor
            "LOVER" -> Lover
            "MIMIC" -> Mimic
            "JOURNALIST" -> Journalist
            "CIVILIAN" -> Civilian
            "BARMAN" -> Barman
            "INFORMATOR" -> Informator
            "DON" -> Don
            "BOMBER" -> Bomber
            "MAFIA" -> Mafia
            else -> null
        }
    }
}

data class RoleData(
    val role: Role,
    var isChosen: Boolean = false,
)