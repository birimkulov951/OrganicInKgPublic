package com.example.organicinkgandroid.model.network.orders.com.example.organicinkgandroid.model.network.raiting

import com.example.organicinkgandroid.model.network.product.Product
import com.google.gson.annotations.SerializedName

data class Rating(
    @SerializedName("id")
    var id: Int,
    @SerializedName("score")
    var score: Double,
    @SerializedName("product")
    var product: Product
)