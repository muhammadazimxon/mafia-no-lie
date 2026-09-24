package com.leafcellteam.mafia.historySection.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaginatedResult<T>(
    @SerialName("items")
    val items: List<T>,

    @SerialName("totalCount")
    val totalCount: Int,

    @SerialName("pageNumber")
    val pageNumber: Int,

    @SerialName("pageSize")
    val pageSize: Int,

    @SerialName("hasNextPage")
    val hasNextPage: Boolean
)