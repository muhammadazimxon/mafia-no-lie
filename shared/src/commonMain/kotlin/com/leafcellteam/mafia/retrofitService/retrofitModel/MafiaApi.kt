package com.leafcellteam.mafia.retrofitService.retrofitModel

import com.leafcellteam.mafia.network.NetworkModule
import com.leafcellteam.mafia.retrofitService.utils.MafiaApiService

object MafiaApi {
    val retrofitService: MafiaApiService get() = NetworkModule.mafiaService
}
