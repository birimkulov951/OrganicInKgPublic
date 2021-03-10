package com.example.organicinkgandroid.model.network.orders


import com.google.gson.annotations.SerializedName

data class ParentCategory(
    @SerializedName("description")
    val description: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("parentCategory")
    val parentCategory: Any
)