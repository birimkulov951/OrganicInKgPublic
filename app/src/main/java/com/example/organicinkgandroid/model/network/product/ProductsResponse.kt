package com.example.organicinkgandroid.model.network.product

import com.google.gson.annotations.SerializedName


data class ProductsResponse(
    @SerializedName("details")
    val details: Any,
    @SerializedName("result")
    val result: ArrayList<Product>,
    @SerializedName("resultCode")
    val resultCode: String
)