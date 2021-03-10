package com.example.organicinkgandroid.model.network.raiting

import com.google.gson.annotations.SerializedName
    data class RatingCreate (
    @SerializedName("clientId")
    var clientId: Int,
    @SerializedName("productId")
    var productId: Int,
    @SerializedName("score")
    var score: Int
)