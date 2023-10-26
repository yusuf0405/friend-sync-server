package com.joseph.models.post

import kotlinx.serialization.Serializable

@Serializable
class PostWithPagingParam(
    val page: Int,
    val pageSize: Int,
    val userId: Int
)