package com.example.organicinkgandroid.model.network.orders.closedorders


import com.google.gson.annotations.SerializedName

data class SupplierFileX(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("imgUrl")
    val imgUrl: String?,
    @SerializedName("supplierFileType")
    val supplierFileType: String?
)