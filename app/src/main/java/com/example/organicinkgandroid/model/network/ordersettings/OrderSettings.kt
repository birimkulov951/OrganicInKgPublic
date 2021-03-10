package com.example.organicinkgandroid.model.network.ordersettings


import com.google.gson.annotations.SerializedName

data class OrderSettings(
    @SerializedName("deliveryPrice")
    val deliveryPrice: Int?,
    @SerializedName("deliveryType")
    val deliveryType: String?,
    @SerializedName("id")
    val id: Int?
)