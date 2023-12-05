package com.boostcampwm2023.snappoint.data.mapper

import com.boostcampwm2023.snappoint.data.local.converter.PostConverter
import com.boostcampwm2023.snappoint.data.local.entity.LocalBlock
import com.boostcampwm2023.snappoint.data.local.entity.LocalPosition
import com.boostcampwm2023.snappoint.data.local.entity.LocalPost
import com.boostcampwm2023.snappoint.data.local.entity.SerializedPost
import com.boostcampwm2023.snappoint.data.remote.model.BlockType
import com.boostcampwm2023.snappoint.data.remote.model.File
import com.boostcampwm2023.snappoint.data.remote.model.PostBlock
import com.boostcampwm2023.snappoint.data.remote.model.response.GetAroundPostResponse
import com.boostcampwm2023.snappoint.presentation.model.PositionState
import com.boostcampwm2023.snappoint.presentation.model.PostBlockState
import com.boostcampwm2023.snappoint.presentation.model.PostSummaryState

fun PostBlock.asPostBlockState(): PostBlockState {
    return when(type){
        BlockType.TEXT.type -> {
            PostBlockState.TEXT(
                uuid = blockUuid!!,
                content = this.content,
            )
        }
        else -> {
            if(this.files!![0].mimeType!!.startsWith("image")){
                PostBlockState.IMAGE(
                    uuid = blockUuid!!,
                    description = this.content,
                    content = this.files[0].url!!,
                    position = this.asPositionState(),
                )
            } else {
                PostBlockState.VIDEO(
                    uuid = blockUuid!!,
                    description = this.content,
                    content = this.files[0].url!!,
                    position = this.asPositionState(),
                )
            }
        }
    }
}

fun PostBlockState.asPostBlock(): PostBlock {
    return when(this){
        is PostBlockState.TEXT -> {
            PostBlock(
                type = BlockType.TEXT.type,
                content = this.content,
            )
        }
        is PostBlockState.IMAGE -> {
            PostBlock(
                content = this.content,
                type = BlockType.MEDIA.type,
                latitude = this.position.latitude,
                longitude = this.position.longitude,
                files = listOf(File(fileUuid = "this is file's uuid")),
            )
        }
        is PostBlockState.VIDEO -> {
            PostBlock(
                content = this.description,
                type = BlockType.MEDIA.type,
                latitude = this.position.latitude,
                longitude = this.position.longitude,
                files = listOf(File(fileUuid = "this is file's uuid")),
            )
        }
    }
}

fun PostBlock.asPositionState(): PositionState {
    return PositionState(
        latitude = this.latitude!!,
        longitude = this.longitude!!
    )
}

fun GetAroundPostResponse.asPostSummaryState(): PostSummaryState {
    return PostSummaryState(
        title = this.title,
        author = "",
        timeStamp = this.createdAt,
        summary = this.summary,
        postBlocks = this.blocks.map { it.asPostBlockState() }
    )
}

fun SerializedPost.asPostSummaryState(): PostSummaryState {
    val localPost = this.post
    return PostSummaryState(
        title = localPost.title,
        author = localPost.author,
        timeStamp = localPost.timestamp,
        postBlocks = localPost.blocks.map { it.asPostBlockState() }
    )
}

fun LocalBlock.asPostBlockState(): PostBlockState {
    return when (this.typeOrdinal) {
        PostBlockState.ViewType.IMAGE.ordinal -> PostBlockState.IMAGE(
            content = this.content,
            description = this.description,
            position = this.position.asPositionState(),
            address = this.address
        )

        PostBlockState.ViewType.VIDEO.ordinal -> PostBlockState.VIDEO(
            content = this.content,
            description = this.description,
            position = this.position.asPositionState(),
            address = this.address
        )

        else -> PostBlockState.TEXT(
            content = this.content
        )
    }
}

fun LocalPosition.asPositionState(): PositionState {
    return PositionState(
        latitude = this.latitude,
        longitude = this.longitude
    )
}

fun PostSummaryState.asSerializedPost(): SerializedPost {
    return SerializedPost(
        LocalPost(
            title = this.title,
            author = this.author,
            timestamp = this.timeStamp,
            blocks = this.postBlocks.map { it.asLocalBlock() }
        )
    )
}

fun PostBlockState.asLocalBlock(): LocalBlock {
    return when(this) {
        is PostBlockState.IMAGE -> LocalBlock(
            typeOrdinal = PostBlockState.ViewType.IMAGE.ordinal,
            content = this.content,
            description = this.description,
            position = this.position.asLocalPosition(),
            address = this.address
        )
        is PostBlockState.VIDEO -> LocalBlock(
            typeOrdinal = PostBlockState.ViewType.VIDEO.ordinal,
            content = this.content,
            description = this.description,
            position = this.position.asLocalPosition(),
            address = this.address
        )
        else -> LocalBlock(
            typeOrdinal = PostBlockState.ViewType.TEXT.ordinal,
            content = this.content
        )
    }
}

fun PositionState.asLocalPosition(): LocalPosition {
    return LocalPosition(
        latitude = this.latitude,
        longitude = this.longitude
    )
}