package com.example.organicinkgpublic.model.network.category


import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("description")
    val description: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("imagePath")
    val imagePath: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("parentCategory")
    val parentCategory: ParentCategory?
)