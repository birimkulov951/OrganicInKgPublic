package com.example.organicinkgpublic.model.network.orders.closedorders


import com.google.gson.annotations.SerializedName

data class ClientEntity(
    @SerializedName("email")
    val email: String?,
    @SerializedName("firstName")
    val firstName: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("lastName")
    val lastName: String?,
    @SerializedName("middleName")
    val middleName: String?,
    @SerializedName("orderEntity")
    val orderEntity: List<OrderEntity>?
)