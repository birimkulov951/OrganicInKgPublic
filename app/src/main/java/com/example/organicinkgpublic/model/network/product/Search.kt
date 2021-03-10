package com.example.organicinkgpublic.model.network.orders.com.example.organicinkgandroid.model.network.product

import com.example.organicinkgpublic.model.network.product.FoundProducts
import com.google.gson.annotations.SerializedName

data class Search (
    @SerializedName("details")
    val details: Any,
    @SerializedName("result")
    val result: FoundProducts,
    @SerializedName("resultCode")
    val resultCode: String
)