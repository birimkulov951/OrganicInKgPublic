package com.example.organicinkgandroid.model.network.orders.closedorders


import com.google.gson.annotations.SerializedName

data class Comment(
    @SerializedName("client")
    val client: Client?,
    @SerializedName("comment")
    val comment: String?,
    @SerializedName("id")
    val id: Int?
)