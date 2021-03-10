package com.example.organicinkgpublic.model.network.orders.closedorders


import com.google.gson.annotations.SerializedName

data class ProductXX(
    @SerializedName("boughtCount")
    val boughtCount: Int?,
    @SerializedName("category")
    val category: CategoryX?,
    @SerializedName("commentEntityList")
    val commentEntityList: List<CommentEntity>?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("measure")
    val measure: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("price")
    val price: Int?,
    @SerializedName("productImages")
    val productImages: List<ProductImage>?,
    @SerializedName("raiting")
    val raiting: Int?,
    @SerializedName("supplier")
    val supplier: Supplier?
)