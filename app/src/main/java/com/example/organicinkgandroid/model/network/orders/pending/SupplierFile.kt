package com.example.organicinkgandroid.model.network.orders.pending


import com.google.gson.annotations.SerializedName

data class SupplierFile(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("imgUrl")
    val imgUrl: String?,
    @SerializedName("supplierFileType")
    val supplierFileType: String?
)