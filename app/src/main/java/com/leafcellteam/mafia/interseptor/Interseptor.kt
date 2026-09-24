package com.leafcellteam.mafia.interseptor

import android.util.Log
import com.leafcellteam.mafia.tokenManager.TokenProvider
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class JwtBearerInterceptor(
    private val tokenProvider: TokenProvider,
) : Interceptor {

    private val mutex = Mutex()

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        if (originalRequest.header("Requires-Auth") == "false") {
            return chain.proceed(originalRequest)
        }

        val accessToken = tokenProvider.getAccessToken()
        val requestWithAuth = originalRequest.withAuthHeader(accessToken)
        val response = chain.proceed(requestWithAuth)

        if (response.code == 401 && accessToken != null) {
            response.close()

            val newToken = runBlocking {
                mutex.withLock {
                    val currentToken = tokenProvider.getAccessToken()

                    if (currentToken != accessToken && currentToken != null) {
                        return@withLock currentToken
                    }

                    try {
                        tokenProvider.refreshAccessToken()
                    } catch (e: Exception) {
                        Log.e("JwtInterceptor", "Failed to refresh token", e)
                        null
                    }
                }
            }

            if (newToken != null) {
                val retryRequest = originalRequest.withAuthHeader(newToken)
                return chain.proceed(retryRequest)
            } else {
                tokenProvider.clearTokens()
                return chain.proceed(originalRequest)
            }
        }

        return response
    }

    private fun Request.withAuthHeader(token: String?) = if (token != null) {
        newBuilder()
            .header("Authorization", "Bearer $token")
            .build()
    } else {
        this
    }
}