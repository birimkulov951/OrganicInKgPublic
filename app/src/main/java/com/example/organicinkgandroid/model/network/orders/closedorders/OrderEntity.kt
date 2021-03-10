package com.example.organicinkgandroid.model.network.orders.closedorders


import com.google.gson.annotations.SerializedName

data class OrderEntity(
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
    val orderNumber: String?,
    @SerializedName("orderSettingEntity")
    val orderSettingEntity: OrderSettingEntity?,
    @SerializedName("orderStatus")
    val orderStatus: String?,
    @SerializedName("orderTotalPrice")
    val orderTotalPrice: Int?,
    @SerializedName("paymentType")
    val paymentType: String?,
    @SerializedName("phoneNumber")
    val phoneNumber: String?,
    @SerializedName("productItemEntities")
    val productItemEntities: List<Any>?,
    @SerializedName("storageAddress")
    val storageAddress: String?
)