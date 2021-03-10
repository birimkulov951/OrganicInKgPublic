package com.example.organicinkgandroid.model.network.cart

data class ResultX(
    val amount: Int,
    val id: Int,
    val product: Product,
    val totalPrice: Double
)