package com.example.organicinkgandroid.model.network.cart

data class MeasureUnitResponse(
    val description: String,
    val id: Int,
    val measureUnitEntityList: List<Any>,
    val name: String
)