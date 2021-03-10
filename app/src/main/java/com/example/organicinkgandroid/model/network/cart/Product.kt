package com.example.organicinkgandroid.model.network.cart

data class Product(
    val boughtCount: Int,
    val category: Category,
    val comments: List<Any>,
    val currency: String,
    val description: String,
    val id: Int,
    val measure: Double,
    val measureUnitResponse: MeasureUnitResponse,
    val name: String,
    val price: Double,
    val productImages: List<Any>,
    val raiting: Any,
    val supplier: Supplier
)