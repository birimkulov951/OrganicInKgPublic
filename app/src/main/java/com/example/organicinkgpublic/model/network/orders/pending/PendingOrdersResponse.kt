package com.example.organicinkgpublic.model.network.orders.pending


import com.google.gson.annotations.SerializedName

data class PendingOrdersResponse(
    @SerializedName("details")
    val details: String?,
    @SerializedName("result")
    val result: List<Result>?,
    @SerializedName("resultCode")
    val resultCode: String?
)