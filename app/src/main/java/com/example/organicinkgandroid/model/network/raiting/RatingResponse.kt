package com.example.organicinkgandroid.model.network.orders.com.example.organicinkgandroid.model.network.raiting

import com.google.gson.annotations.SerializedName

data class RatingResponse (
    @SerializedName("details")
    val details: Any,
    @SerializedName("result")
    val result: Rating,
    @SerializedName("resultCode")
    val resultCode: String
)