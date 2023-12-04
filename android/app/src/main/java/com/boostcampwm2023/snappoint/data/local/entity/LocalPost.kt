package com.boostcampwm2023.snappoint.data.local.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LocalPost(
    @SerialName("title")
    val title: String,
    @SerialName("author")
    val author: String,
    @SerialName("timestamp")
    val timestamp: String,
    @SerialName("blocks")
    val blocks: List<LocalBlock>
)
