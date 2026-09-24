package com.leafcellteam.mafia.contentModerationService

import java.util.Locale

object ContentModerationService {

    private val prohibitedWords = setOf(
        "fuck", "shit", "bitch", "asshole", "bastard", "damn", "crap",
        "nigger", "nigga", "cunt", "whore", "slut", "dick", "cock", "pussy",
        "ass", "faggot", "fag", "retard",

        "blyat", "suka", "pizda", "hui", "chmo", "debil", "mudak",
        "pidoras", "govno", "zasranec", "urod", "mudilo", "pidar",
        "ebat", "ebal", "her", "huyesos", "pedik", "daun", "eblan",
        "shluha", "ohuet", "nahuy", "nahui",

        "блять", "блядь", "сука", "пизда", "хуй", "чмо", "дебил", "мудак",
        "пидорас", "пидор", "говно", "засранец", "урод", "мудило",
        "ебать", "ебал", "ебаный", "ебать", "хер", "хуесос", "педик",
        "даун", "еблан", "шлюха", "охуеть", "нахуй", "нахер"
    )

    fun containsProhibitedContent(text: String): Boolean {
        if (text.isBlank()) return false

        val lowerText = text.lowercase(Locale.getDefault())

        for (word in prohibitedWords) {
            if (lowerText.contains(word)) {
                return true
            }
        }

        val cleanedText = lowerText.replace(Regex("[\\s\\W_]+"), "")
        for (word in prohibitedWords) {
            val cleanedWord = word.replace(Regex("[\\s\\W_]+"), "")
            if (cleanedText.contains(cleanedWord)) {
                return true
            }
        }

        return false
    }

    fun moderateText(text: String): String {
        if (text.isBlank()) return text

        var moderatedText = text

        for (word in prohibitedWords) {
            val pattern = "\\b${Regex.escape(word)}\\b".toRegex(RegexOption.IGNORE_CASE)
            moderatedText = pattern.replace(moderatedText) { matchResult ->
                "*".repeat(matchResult.value.length)
            }
        }

        moderatedText = replaceObfuscatedWords(moderatedText)

        return moderatedText
    }

    private fun replaceObfuscatedWords(text: String): String {
        var result = text
        val lowerText = text.lowercase(Locale.getDefault())

        for (word in prohibitedWords) {
            val cleanedWord = word.replace(Regex("[\\s\\W_]+"), "")
            val index = findObfuscatedWord(lowerText, cleanedWord)

            if (index >= 0) {
                var length = 0
                var matchedChars = 0

                for (i in index until text.length) {
                    if (matchedChars >= cleanedWord.length) break

                    if (text[i].isLetterOrDigit()) {
                        matchedChars++
                    }
                    length++
                }

                if (matchedChars == cleanedWord.length) {
                    result = result.replaceRange(index, index + length, "*".repeat(length))
                }
            }
        }

        return result
    }

    private fun findObfuscatedWord(text: String, word: String): Int {
        var wordIndex = 0
        var startIndex = -1

        for (i in text.indices) {
            val c = text[i].lowercaseChar()

            if (c.isLetterOrDigit()) {
                if (c == word[wordIndex]) {
                    if (wordIndex == 0) {
                        startIndex = i
                    }
                    wordIndex++

                    if (wordIndex >= word.length) {
                        return startIndex
                    }
                } else {
                    wordIndex = 0
                    startIndex = -1
                }
            }
        }

        return -1
    }

    data class ModerationResult(
        val originalText: String,
        val moderatedText: String,
        val isClean: Boolean,
        val containsProhibited: Boolean
    )

    fun analyzeText(text: String): ModerationResult {
        val moderated = moderateText(text)
        val containsProhibited = containsProhibitedContent(text)
        val isClean = text == moderated

        return ModerationResult(
            originalText = text,
            moderatedText = moderated,
            isClean = isClean,
            containsProhibited = containsProhibited
        )
    }

    fun validateUsername(name: String): String? {
        return when {
            name.isBlank() -> "Имя не может быть пустым"
            name.length < 3 -> "Имя должно содержать минимум 3 символа"
            name.length > 20 -> "Имя не может быть длиннее 20 символов"
            containsProhibitedContent(name) -> "Имя содержит недопустимые слова"
            !name.matches(Regex("^[a-zA-Zа-яА-ЯёЁ0-9_\\-]+$")) -> "Имя может содержать только буквы, цифры, дефис и подчеркивание"
            else -> null
        }
    }
}