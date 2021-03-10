package com.example.organicinkgpublic.model.network.cart


import com.google.gson.annotations.SerializedName

data class ProductItemRequestBody(
    @SerializedName("amount")
    val amount: Int,
    @SerializedName("productId")
    val productId: Int
)