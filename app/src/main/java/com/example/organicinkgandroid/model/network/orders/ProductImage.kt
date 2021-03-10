package com.example.organicinkgandroid.model.network.orders


import com.google.gson.annotations.SerializedName

data class ProductImage(
    @SerializedName("id")
    val id: Int,
    @SerializedName("imageUrl")
    val imageUrl: String
)