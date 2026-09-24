package com.leafcellteam.mafia.network

class NetworkResponse<T>(
    val body: T?,
    val code: Int,
    val isSuccessful: Boolean
) {
    companion object {
        fun <T> success(body: T): NetworkResponse<T> = NetworkResponse(body, 200, true)
        fun <T> error(code: Int): NetworkResponse<T> = NetworkResponse(null, code, false)
    }
}
