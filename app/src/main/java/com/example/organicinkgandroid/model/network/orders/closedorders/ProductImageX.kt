package com.example.organicinkgandroid.model.network.orders.closedorders


import com.google.gson.annotations.SerializedName

data class ProductImageX(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("imageUrl")
    val imageUrl: String?
)