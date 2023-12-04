package com.boostcampwm2023.snappoint.data.local.converter

import androidx.room.TypeConverter
import com.boostcampwm2023.snappoint.data.local.entity.LocalPost
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object PostConverter {

    @TypeConverter
    fun postToJson(localPost: LocalPost): String {
        return Json.encodeToString(localPost)
    }

    @TypeConverter
    fun jsonToPost(json: String): LocalPost {
        return Json.decodeFromString<LocalPost>(json)
    }
}