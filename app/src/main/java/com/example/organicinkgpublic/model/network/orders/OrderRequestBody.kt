package com.example.organicinkgpublic.model.network.orders

import com.google.gson.annotations.SerializedName

data class OrderRequestBody(
    @SerializedName("deliveryAddress")
    val deliveryAddress: String,
    @SerializedName("deliveryType")
    val deliveryType: String,
    @SerializedName("desiredDeliveryDate")
    val desiredDeliveryDate: String,
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("lastName")
    val lastName: String,
    @SerializedName("paymentType")
    val paymentType: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("storageAddress")
    val storageAddress: String
)