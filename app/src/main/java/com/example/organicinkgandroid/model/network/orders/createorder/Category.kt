package com.example.organicinkgandroid.model.network.orders.createorder

data class Category(
    val description: String,
    val id: Int,
    val imagePath: String,
    val name: String,
    val parentCategory: Any
)