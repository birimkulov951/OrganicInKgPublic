package com.example.organicinkgandroid.model.network

import com.google.gson.annotations.SerializedName

data class ServerResponse(
    @SerializedName("details")
    val details: Any,
    @SerializedName("result")
    val result: Result,
    @SerializedName("resultCode")
    val resultCode: String
)