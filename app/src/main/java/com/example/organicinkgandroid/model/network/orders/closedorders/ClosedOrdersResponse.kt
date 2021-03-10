package com.example.organicinkgandroid.model.network.orders.closedorders


import com.google.gson.annotations.SerializedName

data class ClosedOrdersResponse(
    @SerializedName("details")
    val details: String?,
    @SerializedName("result")
    val result: List<Result>?,
    @SerializedName("resultCode")
    val resultCode: String?
)