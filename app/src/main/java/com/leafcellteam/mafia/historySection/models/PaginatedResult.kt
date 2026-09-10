package com.leafcellteam.mafia.historySection.models

import com.google.gson.annotations.SerializedName

data class PaginatedResult<T>(
    @SerializedName("items")
    val items: List<T>,

    @SerializedName("totalCount")
    val totalCount: Int,

    @SerializedName("pageNumber")
    val pageNumber: Int,

    @SerializedName("pageSize")
    val pageSize: Int,

    @SerializedName("hasNextPage")
    val hasNextPage: Boolean
)