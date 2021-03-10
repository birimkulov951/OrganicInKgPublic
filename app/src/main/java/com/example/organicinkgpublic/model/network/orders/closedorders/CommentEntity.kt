package com.example.organicinkgpublic.model.network.orders.closedorders


import com.google.gson.annotations.SerializedName

data class CommentEntity(
    @SerializedName("clientEntity")
    val clientEntity: ClientEntity?,
    @SerializedName("comment")
    val comment: String?,
    @SerializedName("id")
    val id: Int?
)