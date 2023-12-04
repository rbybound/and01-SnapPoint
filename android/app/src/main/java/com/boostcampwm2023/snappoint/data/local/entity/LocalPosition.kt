package com.boostcampwm2023.snappoint.data.local.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LocalPosition(
    @SerialName("latitude")
    val latitude: Double = 0.0,
    @SerialName("longitude")
    val longitude: Double = 0.0
)
