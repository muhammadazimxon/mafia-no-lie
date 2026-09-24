package com.leafcellteam.mafia

import io.github.aakira.napier.Napier

fun logd(tag: String, message: String) {
    Napier.d(message, tag = tag)
}

fun loge(tag: String, message: String, throwable: Throwable? = null) {
    Napier.e(message, throwable, tag = tag)
}

fun logw(tag: String, message: String) {
    Napier.w(message, tag = tag)
}
