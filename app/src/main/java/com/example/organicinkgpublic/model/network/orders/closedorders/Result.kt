package com.example.organicinkgpublic.model.network.orders.closedorders


import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("deliveryAddress")
    val deliveryAddress: String?,
    @SerializedName("deliveryType")
    val deliveryType: String?,
    @SerializedName("desiredDeliveryDate")
    val desiredDeliveryDate: String?,
    @SerializedName("firstName")
    val firstName: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("lastName")
    val lastName: String?,
    @SerializedName("orderNumber")
    val orderNumber: String,
    @SerializedName("orderStatus")
    val orderStatus: String?,
    @SerializedName("orderTotalPrice")
    val orderTotalPrice: Int?,
    @SerializedName("paymentType")
    val paymentType: String?,
    @SerializedName("phoneNumber")
    val phoneNumber: String?,
    @SerializedName("products")
    val products: List<Product>?,
    @SerializedName("storageAddress")
    val storageAddress: String?
)