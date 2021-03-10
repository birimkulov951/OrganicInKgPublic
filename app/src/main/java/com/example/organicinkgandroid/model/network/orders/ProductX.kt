package com.example.organicinkgandroid.model.network.orders


import com.google.gson.annotations.SerializedName

data class ProductX(
    @SerializedName("boughtCount")
    val boughtCount: Int,
    @SerializedName("category")
    val category: Category,
    @SerializedName("comments")
    val comments: List<Any>,
    @SerializedName("description")
    val description: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("measure")
    val measure: Double,
    @SerializedName("name")
    val name: String,
    @SerializedName("price")
    val price: Double,
    @SerializedName("productImages")
    val productImages: List<ProductImage>,
    @SerializedName("supplier")
    val supplier: Supplier
)