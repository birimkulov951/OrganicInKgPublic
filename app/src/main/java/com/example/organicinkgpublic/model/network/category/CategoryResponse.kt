package com.example.organicinkgpublic.model.network.category


import com.google.gson.annotations.SerializedName

data class CategoryResponse(
    @SerializedName("details")
    val details: Any?,
    @SerializedName("result")
    val result: List<Category>,
    @SerializedName("resultCode")
    val resultCode: String?
)