package com.example.mafiaonlinejetpackcomposecapi.interseptor

import android.util.Log
import com.example.mafiaonlinejetpackcomposecapi.retrofitService.retrofitModel.MafiaApi
import com.example.mafiaonlinejetpackcomposecapi.retrofitService.retrofitModel.RefreshTokenRequest
import com.example.mafiaonlinejetpackcomposecapi.sharedPreferences.TokenPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.util.concurrent.TimeUnit
import okhttp3.logging.HttpLoggingInterceptor


interface TokenProvider {
    fun getAccessToken(): String?
    fun getRefreshToken(): String?
    suspend fun refreshAccessToken(): String?
    fun clearTokens()
    fun saveTokens(accessToken: String, refreshToken: String)
}
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

class TokenManager(
    private val tokenPreferences: TokenPreferences
) : TokenProvider {
    private val refreshMutex = Mutex()

    override fun getAccessToken(): String? {
        return tokenPreferences.getAccessToken()
    }

    override fun getRefreshToken(): String? {
        return tokenPreferences.getRefreshToken()
    }

    override fun saveTokens(accessToken: String, refreshToken: String) {
        tokenPreferences.saveTokens(accessToken, refreshToken)
    }

    override suspend fun refreshAccessToken(): String? = refreshMutex.withLock {
        val refreshToken = getRefreshToken() ?: return null

        try {
            Log.d("TokenManager", "Attempting to refresh token")

            val response = withContext(Dispatchers.IO) {
                MafiaApi.retrofitService.refreshTokens(RefreshTokenRequest(refreshToken))
            }

            Log.d("TokenManager", "Token refreshed successfully")
            saveTokens(response.accessToken, response.refreshToken)
            return response.accessToken

        } catch (e: Exception) {
            Log.e("TokenManager", "Failed to refresh token", e)
            clearTokens()
            return null
        }
    }

    override fun clearTokens() {
        Log.d("TokenManager", "Clearing tokens")
        tokenPreferences.clearTokens()
    }
}

object HttpClientBuilder {
    fun createOkHttpClient(tokenProvider: TokenProvider): OkHttpClient {
        val jwtInterceptor = JwtBearerInterceptor(tokenProvider)

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(jwtInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .callTimeout(10, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }
}