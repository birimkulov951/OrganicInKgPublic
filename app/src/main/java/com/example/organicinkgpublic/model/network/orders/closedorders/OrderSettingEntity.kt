package com.example.organicinkgpublic.model.network.orders.closedorders


import com.google.gson.annotations.SerializedName

data class OrderSettingEntity(
    @SerializedName("deliveryPrice")
    val deliveryPrice: Int?,
    @SerializedName("deliveryType")
    val deliveryType: String?,
    @SerializedName("id")
    val id: Int?
)