package com.example.organicinkgandroid.model.network.supplier


import com.google.gson.annotations.SerializedName


data class SupplierFile(
    @SerializedName("id")
    val id: Int,
    @SerializedName("imgUrl")
    val imgUrl: String,
    @SerializedName("supplierFileType")
    val supplierFileType: String
)