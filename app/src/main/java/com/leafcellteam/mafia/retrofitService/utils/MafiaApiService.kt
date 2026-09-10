package com.leafcellteam.mafia.retrofitService.utils

import com.leafcellteam.shared.joinRoom.RoomData
import com.leafcellteam.shared.network.models.AuthCheckResponse
import com.leafcellteam.shared.network.models.EmailVerify
import com.leafcellteam.shared.network.models.LinkerToCreateCharacter
import com.leafcellteam.shared.network.models.LoginRequest
import com.leafcellteam.shared.network.models.RefreshTokenRequest
import com.leafcellteam.shared.network.models.Tokens
import com.leafcellteam.shared.network.models.CreateRoomRequest
import com.leafcellteam.shared.network.models.LoginAsGuestResponse
import com.leafcellteam.shared.network.models.PlayerRegisterDataRequest
import com.leafcellteam.shared.network.models.ProfileData
import com.leafcellteam.shared.network.models.RequestLogInData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface MafiaApiService {
    @GET("update")
    suspend fun update(): List<RoomData>

    @POST("register")
    @Headers("Requires-Auth: false")
    suspend fun register(@Body request: PlayerRegisterDataRequest): Int

    @POST("create")
    suspend fun createRoom(@Body request: CreateRoomRequest): Response<Unit>

    @GET("get-guid")
    suspend fun getGuid(): String

    @GET("auth-check")
    suspend fun validateToken(@Query("isGuest") isGuest: Boolean): AuthCheckResponse

    @POST("re-send-register-code")
    @Headers("Requires-Auth: false")
    suspend fun reSendRegisterCode(@Body request: PlayerRegisterDataRequest)

    @POST("check-join")
    suspend fun checkJoin(@Query("roomCode") roomCode: String) : Boolean

    @POST("game-calculation")
    suspend fun sendGameCalculation(
        @Query("isCivilian") isCivilian: Boolean,
        @Query("currentPlayerId") currentPlayerId: Int
    )

    @PATCH("leave-game-logic")
    suspend fun sendLeaveGameLogic(@Query("currentPlayerId") currentPlayerId: Int)

    @PATCH("disconnect-via-id")
    suspend fun sendDisconnection(
        @Query("currentPlayerId") currentPlayerId: Int,
        @Query("id") id: Int
    )

    @POST("log-in")
    @Headers("Requires-Auth: false")
    suspend fun getEmailExists(@Body loginRequest: LoginRequest) : RequestLogInData

    @POST("verify-email")
    @Headers("Requires-Auth: false")
    suspend fun verifyEmail(@Body emailVerify : EmailVerify) : LinkerToCreateCharacter

    @POST("create-character")
    @Headers("Requires-Auth: false")
    suspend fun createCharacter(
        @Query("key") key : String,
        @Query("email") email : String,
        @Query("name") name: String,
        @Query("password") password : String
    ) : Response<Boolean>

    @POST("refresh")
    @Headers("Requires-Auth: false")
    suspend fun refreshTokens(@Body refreshToken : RefreshTokenRequest) : Tokens

    @POST("login-as-guest")
    @Headers("Requires-Auth: false")
    suspend fun requestToLoginAsGuest(@Query("playerName") name : String) : Response<LoginAsGuestResponse>

    @GET("check-guest-name")
    @Headers("Requires-Auth: false")
    suspend fun checkForGuestName(@Query("name") name: String): Response<Boolean>

    @GET("profile-info")
    @Headers("Requires-Auth: true")
    suspend fun profileInfo(@Query("playerId") playerId: Int): Response<ProfileData>

    @PATCH("edit-profile")
    @Headers("Requires-Auth: true")
    suspend fun editProfile(@Query("playerId") playerId: Int, @Query("newPlayerName") newPlayerName: String): Response<Boolean>

    @GET("is-available-for-change-password")
    @Headers("Requires-Auth: false")
    suspend fun isAvailableForChangePassword(@Query("emailAddress") emailAddress: String): Response<Unit>

    @PATCH("logout-guest")
    @Headers("Requires-Auth: false")
    suspend fun removeGuest(@Query("guestId") guestId: Int, @Query("guestName") guestName: String): Response<Unit>

    @PATCH("reset-password")
    @Headers("Requires-Auth: false")
    suspend fun resetPassword(@Query("code") code: String, @Query("emailAddress") emailAddress: String, @Query("newPassword") password: String): Response<Unit>
}