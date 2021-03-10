package com.example.organicinkgandroid.model.network.cart

data class Category(
    val description: String,
    val id: Int,
    val imagePath: String,
    val name: String,
    val parentCategory: Any
)