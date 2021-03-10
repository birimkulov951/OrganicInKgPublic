package com.example.organicinkgandroid.model.network.orders.createorder

data class Result(
    val deliveryAddress: String,
    val deliveryType: String,
    val desiredDeliveryDate: String,
    val firstName: String,
    val id: Int,
    val lastName: String,
    val orderNumber: String,
    val orderStatus: String,
    val orderTotalPrice: Double,
    val paymentType: String,
    val phoneNumber: String,
    val products: List<Product>,
    val storageAddress: String
)