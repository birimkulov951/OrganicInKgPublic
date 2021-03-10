package com.example.organicinkgandroid.model.network.orders.closedorders


import com.google.gson.annotations.SerializedName

data class ProductItemEntity(
    @SerializedName("amount")
    val amount: Int?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("product")
    val product: ProductXX?,
    @SerializedName("totalPrice")
    val totalPrice: Int?
)