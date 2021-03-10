package com.example.organicinkgpublic.model.network.orders.closedorders


import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("amount")
    val amount: Int?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("product")
    val product: ProductX?,
    @SerializedName("totalPrice")
    val totalPrice: Int?
)