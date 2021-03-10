package com.example.organicinkgandroid.model.network.orders


import com.google.gson.annotations.SerializedName

data class OrderResponse(
    @SerializedName("details")
    val details: Any,
    @SerializedName("result")
    val result: Result,
    @SerializedName("resultCode")
    val resultCode: String
)