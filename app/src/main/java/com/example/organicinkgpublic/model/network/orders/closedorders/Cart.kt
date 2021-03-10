package com.example.organicinkgpublic.model.network.orders.closedorders


import com.google.gson.annotations.SerializedName

data class Cart(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("productItemEntities")
    val productItemEntities: List<ProductItemEntity>?,
    @SerializedName("totalPrice")
    val totalPrice: Int?
)