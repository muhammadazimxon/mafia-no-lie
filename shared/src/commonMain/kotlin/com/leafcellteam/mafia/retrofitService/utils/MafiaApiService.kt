package com.leafcellteam.mafia.retrofitService.utils

import com.leafcellteam.mafia.joinRoom.models.RoomData
import com.leafcellteam.mafia.retrofitService.retrofitModel.*
import com.leafcellteam.mafia.network.NetworkResponse

interface MafiaApiService {
    suspend fun update(): List<RoomData>
    suspend fun register(request: PlayerRegisterDataRequest): Int
    suspend fun createRoom(request: CreateRoomRequest): NetworkResponse<Unit>
    suspend fun getGuid(): String
    suspend fun validateToken(isGuest: Boolean): AuthCheckResponse
    suspend fun reSendRegisterCode(request: PlayerRegisterDataRequest)
    suspend fun checkJoin(roomCode: String) : Boolean
    suspend fun sendGameCalculation(isCivilian: Boolean, currentPlayerId: Int)
    suspend fun sendLeaveGameLogic(currentPlayerId: Int)
    suspend fun sendDisconnection(currentPlayerId: Int, id: Int)
    suspend fun getEmailExists(loginRequest: LoginRequest) : RequestLogInData
    suspend fun verifyEmail(emailVerify : EmailVerify) : LinkerToCreateCharacter
    suspend fun createCharacter(key : String, email : String, name: String, password : String) : NetworkResponse<Boolean>
    suspend fun refreshTokens(refreshToken : RefreshTokenRequest) : Tokens
    suspend fun requestToLoginAsGuest(name : String) : NetworkResponse<LoginAsGuestResponse>
    suspend fun checkForGuestName(name: String): NetworkResponse<Boolean>
    suspend fun profileInfo(playerId: Int): NetworkResponse<ProfileData>
    suspend fun editProfile(playerId: Int, newPlayerName: String): NetworkResponse<Boolean>
    suspend fun isAvailableForChangePassword(emailAddress: String): NetworkResponse<Unit>
    suspend fun removeGuest(guestId: Int, guestName: String): NetworkResponse<Unit>
    suspend fun resetPassword(code: String, emailAddress: String, password: String): NetworkResponse<Unit>
}
