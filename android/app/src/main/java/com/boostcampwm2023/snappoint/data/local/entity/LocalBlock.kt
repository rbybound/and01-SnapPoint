package com.boostcampwm2023.snappoint.data.local.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LocalBlock(
    @SerialName("type")
    val typeOrdinal: Int,
    @SerialName("content")
    val content: String,
    @SerialName("description")
    val description: String = "",
    @SerialName("position")
    val position: LocalPosition = LocalPosition(),
    @SerialName("address")
    val address: String = ""
)
