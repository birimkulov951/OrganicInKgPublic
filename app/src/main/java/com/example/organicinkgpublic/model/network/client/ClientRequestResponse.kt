package com.example.organicinkgpublic.model.network.client



import com.example.organicinkgpublic.model.network.orders.com.example.organicinkgandroid.model.network.client.User
import com.google.gson.annotations.SerializedName

data class ClientRequestResponse(
    @SerializedName("details")
    val details: Any,
    @SerializedName("result")
    val result: User,
    @SerializedName("resultCode")
    val resultCode: String
)