package com.example.organicinkgpublic.model.network.orders.pending


import com.google.gson.annotations.SerializedName

data class Comment(
    @SerializedName("comment")
    val comment: String?,
    @SerializedName("id")
    val id: Int?
)