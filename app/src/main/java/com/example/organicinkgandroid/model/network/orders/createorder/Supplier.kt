package com.example.organicinkgandroid.model.network.orders.createorder

import com.example.organicinkgandroid.model.network.supplier.PlaceOfProduction

data class Supplier(
    val email: String,
    val ewalletNumber: String,
    val fullName: String,
    val id: Int,
    val isActive: Boolean,
    val phone: String,
    val placeOfProduction: PlaceOfProduction,
    val produces: String,
    val supplierFile: List<SupplierFile>
)