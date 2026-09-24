package com.leafcellteam.mafia.network

import com.leafcellteam.mafia.joinRoom.models.RoomData
import com.leafcellteam.mafia.retrofitService.retrofitModel.*
import com.leafcellteam.mafia.retrofitService.utils.MafiaApiService
import com.leafcellteam.mafia.tokenManager.TokenProvider
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class KtorMafiaApiService(
    private val client: HttpClient,
    private val tokenProvider: TokenProvider
) : MafiaApiService {

    private suspend fun HttpRequestBuilder.auth(required: Boolean = true) {
        if (required) {
            val token = tokenProvider.getAccessToken()
            if (token != null) {
                header(HttpHeaders.Authorization, "Bearer $token")
            }
        }
    }

    override suspend fun update(): List<RoomData> = client.get("update") { auth() }.body()

    override suspend fun register(request: PlayerRegisterDataRequest): Int = 
        client.post("register") { 
            setBody(request)
        }.body()

    override suspend fun createRoom(request: CreateRoomRequest): NetworkResponse<Unit> {
        val response = client.post("create") {
            auth()
            setBody(request)
        }
        return NetworkResponse(null, response.status.value, response.status.isSuccess())
    }

    override suspend fun getGuid(): String = client.get("get-guid") { auth() }.body()

    override suspend fun validateToken(isGuest: Boolean): AuthCheckResponse = 
        client.get("auth-check") {
            auth()
            parameter("isGuest", isGuest)
        }.body()

    override suspend fun reSendRegisterCode(request: PlayerRegisterDataRequest) {
        client.post("re-send-register-code") { setBody(request) }
    }

    override suspend fun checkJoin(roomCode: String): Boolean = 
        client.post("check-join") {
            auth()
            parameter("roomCode", roomCode)
        }.body()

    override suspend fun sendGameCalculation(isCivilian: Boolean, currentPlayerId: Int) {
        client.post("game-calculation") {
            auth()
            parameter("isCivilian", isCivilian)
            parameter("currentPlayerId", currentPlayerId)
        }
    }

    override suspend fun sendLeaveGameLogic(currentPlayerId: Int) {
        client.patch("leave-game-logic") {
            auth()
            parameter("currentPlayerId", currentPlayerId)
        }
    }

    override suspend fun sendDisconnection(currentPlayerId: Int, id: Int) {
        client.patch("disconnect-via-id") {
            auth()
            parameter("currentPlayerId", currentPlayerId)
            parameter("id", id)
        }
    }

    override suspend fun getEmailExists(loginRequest: LoginRequest): RequestLogInData = 
        client.post("log-in") { setBody(loginRequest) }.body()

    override suspend fun verifyEmail(emailVerify: EmailVerify): LinkerToCreateCharacter = 
        client.post("verify-email") { setBody(emailVerify) }.body()

    override suspend fun createCharacter(key: String, email: String, name: String, password: String): NetworkResponse<Boolean> {
        val response = client.post("create-character") {
            parameter("key", key)
            parameter("email", email)
            parameter("name", name)
            parameter("password", password)
        }
        return NetworkResponse(response.body<Boolean>(), response.status.value, response.status.isSuccess())
    }

    override suspend fun refreshTokens(refreshToken: RefreshTokenRequest): Tokens = 
        client.post("refresh") { setBody(refreshToken) }.body()

    override suspend fun requestToLoginAsGuest(name: String): NetworkResponse<LoginAsGuestResponse> {
        val response = client.post("login-as-guest") { parameter("playerName", name) }
        return NetworkResponse(response.body(), response.status.value, response.status.isSuccess())
    }

    override suspend fun checkForGuestName(name: String): NetworkResponse<Boolean> {
        val response = client.get("check-guest-name") { parameter("name", name) }
        return NetworkResponse(response.body(), response.status.value, response.status.isSuccess())
    }

    override suspend fun profileInfo(playerId: Int): NetworkResponse<ProfileData> {
        val response = client.get("profile-info") {
            auth()
            parameter("playerId", playerId)
        }
        return NetworkResponse(response.body(), response.status.value, response.status.isSuccess())
    }

    override suspend fun editProfile(playerId: Int, newPlayerName: String): NetworkResponse<Boolean> {
        val response = client.patch("edit-profile") {
            auth()
            parameter("playerId", playerId)
            parameter("newPlayerName", newPlayerName)
        }
        return NetworkResponse(response.body(), response.status.value, response.status.isSuccess())
    }

    override suspend fun isAvailableForChangePassword(emailAddress: String): NetworkResponse<Unit> {
        val response = client.get("is-available-for-change-password") { parameter("emailAddress", emailAddress) }
        return NetworkResponse(Unit, response.status.value, response.status.isSuccess())
    }

    override suspend fun removeGuest(guestId: Int, guestName: String): NetworkResponse<Unit> {
        val response = client.patch("logout-guest") {
            parameter("guestId", guestId)
            parameter("guestName", guestName)
        }
        return NetworkResponse(Unit, response.status.value, response.status.isSuccess())
    }

    override suspend fun resetPassword(code: String, emailAddress: String, password: String): NetworkResponse<Unit> {
        val response = client.patch("reset-password") {
            parameter("code", code)
            parameter("emailAddress", emailAddress)
            parameter("newPassword", password)
        }
        return NetworkResponse(Unit, response.status.value, response.status.isSuccess())
    }
}
