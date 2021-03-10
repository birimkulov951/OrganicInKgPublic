package com.example.organicinkgpublic.model.network.supplier


import com.google.gson.annotations.SerializedName


data class Supplier(
    @SerializedName("email")
    val email: String,
    @SerializedName("ewalletNumber")
    val ewalletNumber: String,
    @SerializedName("fullName")
    val fullName: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("isActive")
    val isActive: Boolean,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("placeOfProduction")
    val placeOfProduction: PlaceOfProduction,
    @SerializedName("produces")
    val produces: String,
    @SerializedName("supplierFile")
    val supplierFile: List<SupplierFile>
)