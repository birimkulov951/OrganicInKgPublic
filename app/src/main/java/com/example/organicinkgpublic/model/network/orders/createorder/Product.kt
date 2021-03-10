package com.example.organicinkgpublic.model.network.orders.createorder

data class Product(
    val amount: Int,
    val id: Int,
    val product: ProductX,
    val totalPrice: Double
)