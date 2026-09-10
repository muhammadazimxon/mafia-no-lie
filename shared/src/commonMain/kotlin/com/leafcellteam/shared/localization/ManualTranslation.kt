package com.leafcellteam.shared.localization

import com.leafcellteam.shared.gameRoom.models.Phase

object ManualTranslation {

    fun getPhase(lang: Language, phase: Phase): List<String> {
        return when (lang) {
            Language.Russian -> {
                when (phase) {
                    Phase.None -> "Нет".split("(?=[А-Я])".toRegex())
                    Phase.DayVote -> "День голосование".split("(?=[А-Я])".toRegex())
                    Phase.NightVote -> "Ночь голосование".split("(?=[А-Я])".toRegex())
                    Phase.NightDiscussion -> "Ночь Обсуждение".split("(?=[А-Я])".toRegex())
                    Phase.DayDiscussion -> "День Обсуждение".split("(?=[А-Я])".toRegex())
                }
            }
            Language.English -> {
                when (phase) {
                    Phase.None -> "None".split("(?=[A-Z])".toRegex())
                    Phase.DayVote -> "Day Vote".split("(?=[A-Z])".toRegex())
                    Phase.NightVote -> "Night Vote".split("(?=[A-Z])".toRegex())
                    Phase.NightDiscussion -> "Night Discussion".split("(?=[A-Z])".toRegex())
                    Phase.DayDiscussion -> "Day Discussion".split("(?=[A-Z])".toRegex())
                }
            }
            Language.Uzbek -> {
                when (phase) {
                    Phase.None -> "Yoq".split("(?=[A-Z])".toRegex())
                    Phase.DayVote -> "Kungi Ovoz".split("(?=[A-Z])".toRegex())
                    Phase.NightVote -> "Kechki Ovoz".split("(?=[A-Z])".toRegex())
                    Phase.NightDiscussion -> "Kechki Muhokama".split("(?=[A-Z])".toRegex())
                    Phase.DayDiscussion -> "Kungi Muhokama".split("(?=[A-Z])".toRegex())
                }
            }
        }
    }

    fun getEvents(lang: Language, events: List<String>): List<String> {
        when (lang) {
            Language.Russian -> {
                if (events.isEmpty()) return emptyList()

                val russianEvents = mutableListOf<String>()
                events.forEach { event ->
                    when (event) {
                        "GAME OVER. CIVILIANS WIN!!!" -> russianEvents += "ИГРА ЗАКОНЧЕНА. МИРНЫЕ ВЫИГРАЛИ !!!"
                        "GAME OVER. MAFIAS WINS!!!" -> russianEvents += "ИГРА ЗАКОНЧЕНА. МАФИИ ВЫИГРАЛИ !!!"
                        "Night Vote Begins" -> russianEvents += "Началась Ночная Голосование"
                        "Day Discussion Begins" -> russianEvents += "Началось дневное обсуждение"
                        "Day Vote Begins" -> russianEvents += "Началось дневное голосование"
                        "Night Discussion Begins" -> russianEvents += "Началось ночное обсуждение"
                        "Somebody has drunk" -> russianEvents += "Кто-то был напоён"
                        "Engineer saboteur did his decision" -> russianEvents += "Инженер-саботажник сделал свой выбор"
                        "Detective checked" -> russianEvents += "Детектив проверил"
                        "Don checked" -> russianEvents += "Дон проверил"
                        "Informator checked" -> russianEvents += "Информатор проверил"
                        "Lover got high with somebody" -> russianEvents += "Любовник провёл ночь с кем-то"
                        "Mafia eliminated a victim" -> russianEvents += "Мафия устранила жертву"
                        "Sherif in action" -> russianEvents += "Шериф в действии"
                        "Chosen a victim by votes" -> russianEvents += "Жертва выбрана голосованием"
                        "Chosen a luck guy to be cured" -> russianEvents += "Выбран счастливчик для лечения"
                    }

                    var new = "НОВОСТИ: "

                    if (event.contains("NEWS")) {
                        val splitEvent = event.split(" ")
                        if (splitEvent.size >= 4) {
                            new += splitEvent[1] + " и " + splitEvent[3]
                        }
                    }
                    if (!event.contains("NOT")) new += " в ОДНОЙ команде"
                    else new += " в РАЗНЫХ командах"

                    russianEvents += new
                }

                return russianEvents
            }

            Language.English -> return events
            Language.Uzbek -> {
                if (events.isEmpty()) return emptyList()

                val uzbekEvents = mutableListOf<String>()
                events.forEach { event ->
                    when (event) {
                        "GAME OVER. CIVILIANS WIN!!!" -> uzbekEvents += "O‘YIN TUGADI. XALQ G‘ALABA QOZONDI !!!"
                        "GAME OVER. MAFIAS WINS!!!" -> uzbekEvents += "O‘YIN TUGADI. MAFIYA G‘ALABA QOZONDI !!!"
                        "Night Vote Begins" -> uzbekEvents += "Tungi ovoz berish boshlandi"
                        "Day Discussion Begins" -> uzbekEvents += "Kunduzgi muhokama boshlandi"
                        "Day Vote Begins" -> uzbekEvents += "Kunduzgi ovoz berish boshlandi"
                        "Night Discussion Begins" -> uzbekEvents += "Tungi muhokama boshlandi"
                        "Somebody has drunk" -> uzbekEvents += "Kimdir ichimlik ichdi"
                        "Engineer saboteur did his decision" -> uzbekEvents += "Muhandis-sabotajchi o‘z tanlovini qildi"
                        "Detective checked" -> uzbekEvents += "Detektiv tekshirdi"
                        "Don checked" -> uzbekEvents += "Don tekshirdi"
                        "Informator checked" -> uzbekEvents += "Informator tekshirdi"
                        "Lover got high with somebody" -> uzbekEvents += "Oshiq kim bilandir tun o‘tkazdi"
                        "Mafia eliminated a victim" -> uzbekEvents += "Mafiya qurbonni yo‘q qildi"
                        "Sherif in action" -> uzbekEvents += "Sherif harakatga o‘tdi"
                        "Chosen a victim by votes" -> uzbekEvents += "Ovoz berish orqali qurbon tanlandi"
                        "Chosen a luck guy to be cured" -> uzbekEvents += "Davolanish uchun omadli odam tanlandi"
                    }

                    var new = "YANGILIKLAR: "

                    if (event.contains("NEWS")) {
                        val splitEvent = event.split(" ")
                        if (splitEvent.size >= 4) {
                            new += splitEvent[1] + " va " + splitEvent[3]
                        }
                    }
                    if (!event.contains("NOT")) new += " BITTA jamoada"
                    else new += " XAR-XIL jamoada"

                    uzbekEvents += new
                }

                return uzbekEvents
            }
        }
    }
}
