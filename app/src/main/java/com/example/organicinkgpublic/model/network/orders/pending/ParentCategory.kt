package com.example.organicinkgpublic.model.network.orders.pending


import com.google.gson.annotations.SerializedName

data class ParentCategory(
    @SerializedName("description")
    val description: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?
)