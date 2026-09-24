package com.leafcellteam.mafia.network

import com.leafcellteam.mafia.DOMAIN
import com.leafcellteam.mafia.tokenManager.TokenProvider
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

fun createKtorClient(tokenProvider: TokenProvider) = HttpClient {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            prettyPrint = true
            isLenient = true
        })
    }

    install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.ALL
    }

    defaultRequest {
        url(DOMAIN)
        contentType(ContentType.Application.Json)
    }

    // Auth will be handled per-request or via a custom plugin if needed,
    // but per-request is easier for "Requires-Auth: false" logic.
}

val json = Json {
    ignoreUnknownKeys = true
    encodeDefaults = true
}
