package com.example.organicinkgpublic.model.network.product


import com.google.gson.annotations.SerializedName

data class ProductResponse(
    @SerializedName("details")
    val details: Any,
    @SerializedName("result")
    val result: Product,
    @SerializedName("resultCode")
    val resultCode: String
)