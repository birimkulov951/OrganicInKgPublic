package com.example.organicinkgpublic.model.network.orders.closedorders


import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("description")
    val description: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("imagePath")
    val imagePath: String?,
    @SerializedName("name")
    val name: String?
)