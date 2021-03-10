package com.example.organicinkgpublic.model.network.orders.closedorders


import com.google.gson.annotations.SerializedName

data class ProductImage(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("imageUrl")
    val imageUrl: String?
)