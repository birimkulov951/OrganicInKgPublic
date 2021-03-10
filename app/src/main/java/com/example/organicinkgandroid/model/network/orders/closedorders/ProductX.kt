package com.example.organicinkgandroid.model.network.orders.closedorders


import com.google.gson.annotations.SerializedName

   // "measureUnitResponse":{"id":1,"name":"кг","description":"string","measureUnitEntityList":[]},

data class ProductX(
    @SerializedName("boughtCount")
    val boughtCount: Int?,
    @SerializedName("category")
    val category: Category?,
    @SerializedName("comments")
    val comments: List<Comment>?,
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
    val productImages: List<ProductImageX>?,
    @SerializedName("raiting")
    val raiting: Int?,
    @SerializedName("supplier")
    val supplier: SupplierX?,
    @SerializedName("currency")
    val currency: String?,
    @SerializedName("ewalletNumber")
    val ewalletNumber: String?
)