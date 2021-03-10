package com.example.organicinkgpublic.model.network.orders.com.example.organicinkgandroid.model.network.client


import com.google.gson.annotations.SerializedName


data class Cart(
    @SerializedName("hibernateLazyInitializer")
    val hibernateLazyInitializer: HibernateLazyInitializer,
    @SerializedName("id")
    val id: Int,
    @SerializedName("productItemEntities")
    val productItemEntities: List<Any>,
    @SerializedName("totalPrice")
    val totalPrice: Any
)
