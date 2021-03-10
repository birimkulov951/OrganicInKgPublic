package com.example.organicinkgpublic.model.network.product


import com.example.organicinkgpublic.model.network.category.Category
import com.example.organicinkgpublic.model.network.measureUnit.MeasureUnitResponse
import com.example.organicinkgpublic.model.network.raiting.feedbacks.Feedback
import com.example.organicinkgpublic.model.network.supplier.Supplier
import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("category")
    val category: Category,
    @SerializedName("description")
    val description: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("measure")
    val measure: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("price")
    val price: Float,
    @SerializedName("productImages")
    val productImages: List<ProductImage>,
    @SerializedName("supplier")
    val supplier: Supplier,
    @SerializedName("comments")
    val comments: ArrayList<Feedback>,
    @SerializedName("raiting")
    var rating: Float?,
    @SerializedName("boughtCount")
    val boughtCount: Int,
    @SerializedName("currency")
    var currency: String,
    @SerializedName("measureUnitResponse")
    val measureUnitResponse: MeasureUnitResponse
)