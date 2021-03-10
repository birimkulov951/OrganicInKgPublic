package com.example.organicinkgpublic.model.network.orders


import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("cashPaymentOrderStatus")
    val cashPaymentOrderStatus: String,
    @SerializedName("deliveryAddress")
    val deliveryAddress: String,
    @SerializedName("deliveryType")
    val deliveryType: String,
    @SerializedName("desiredDeliveryDate")
    val desiredDeliveryDate: String,
    @SerializedName("elsomPaymentOrderStatus")
    val elsomPaymentOrderStatus: Any,
    @SerializedName("id")
    val id: Int,
    @SerializedName("orderNumber")
    val orderNumber: String,
    @SerializedName("paymentType")
    val paymentType: String,
    @SerializedName("products")
    val products: List<Product>,
    @SerializedName("storageAddress")
    val storageAddress: String
)