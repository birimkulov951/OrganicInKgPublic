package com.example.organicinkgandroid.model.network.orders.closedorders


import com.google.gson.annotations.SerializedName

data class Client(
    @SerializedName("cart")
    val cart: Cart?,
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
    @SerializedName("phoneNumber")
    val phoneNumber: String?
)