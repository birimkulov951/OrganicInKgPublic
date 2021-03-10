package com.example.organicinkgpublic.model.network.ordersettings


import com.google.gson.annotations.SerializedName

data class OrderSettingsResponse(
    @SerializedName("details")
    val details: Any?,
    @SerializedName("result")
    val result: List<OrderSettings>?,
    @SerializedName("resultCode")
    val resultCode: String?
)