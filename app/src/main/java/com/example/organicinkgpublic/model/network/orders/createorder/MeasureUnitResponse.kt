package com.example.organicinkgpublic.model.network.orders.createorder

data class MeasureUnitResponse(
    val description: String,
    val id: Int,
    val measureUnitEntityList: List<Any>,
    val name: String
)