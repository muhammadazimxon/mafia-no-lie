package com.leafcellteam.shared.roles

sealed class Role(val category: Category) {
    enum class Category {
        CIVILIAN, MAFIA
    }

    data object Civilian   : Role(Category.CIVILIAN)
    data object Sherif     : Role(Category.CIVILIAN)
    data object Detective  : Role(Category.CIVILIAN)
    data object Doctor     : Role(Category.CIVILIAN)
    data object Lover      : Role(Category.CIVILIAN)
    data object Journalist : Role(Category.CIVILIAN)
    data object Mimic      : Role(Category.CIVILIAN)

    data object Mafia      : Role(Category.MAFIA)
    data object Barman     : Role(Category.MAFIA)
    data object Informator : Role(Category.MAFIA)
    data object Bomber     : Role(Category.MAFIA)
    data object Don        : Role(Category.MAFIA)

    companion object {
        fun fromName(name: String): Role = when (name.uppercase()) {
            "CIVILIAN"  -> Civilian
            "SHERIF"    -> Sherif
            "DETECTIVE" -> Detective
            "DOCTOR"    -> Doctor
            "LOVER"     -> Lover
            "JOURNALIST"-> Journalist
            "MIMIC"     -> Mimic
            "MAFIA"     -> Mafia
            "BARMAN"    -> Barman
            "INFORMATOR"-> Informator
            "BOMBER"    -> Bomber
            "DON"       -> Don
            else        -> Civilian
        }
    }
}
