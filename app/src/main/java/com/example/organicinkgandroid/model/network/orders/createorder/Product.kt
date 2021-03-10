package com.example.organicinkgandroid.model.network.orders.createorder

data class Product(
    val amount: Int,
    val id: Int,
    val product: ProductX,
    val totalPrice: Double
)