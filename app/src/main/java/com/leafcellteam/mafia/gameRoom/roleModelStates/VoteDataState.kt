package com.leafcellteam.mafia.gameRoom.roleModelStates

data class VoteDataState(
    val isVotesReset: Boolean = false,
    val votedPlayerId: Int = -1,
    val isNotVoted: Boolean = false,
    val isConfirmVoteDialog: Boolean = false,
    val votePlayer: String = "",
)