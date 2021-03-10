package com.example.organicinkgandroid.model.network.orders.closedorders


import com.example.organicinkgandroid.model.network.cart.PlaceOfProduction
import com.google.gson.annotations.SerializedName

data class SupplierX(
    @SerializedName("email")
    val email: String?,
    @SerializedName("ewalletNumber")
    val ewalletNumber: String?,
    @SerializedName("fullName")
    val fullName: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("isActive")
    val isActive: Boolean?,
    @SerializedName("phone")
    val phone: String?,
    @SerializedName("placeOfProduction")
    val placeOfProduction: PlaceOfProduction,
    @SerializedName("produces")
    val produces: String?,
    @SerializedName("supplierFile")
    val supplierFile: List<SupplierFileX>?
)