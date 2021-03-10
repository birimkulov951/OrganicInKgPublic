package com.example.organicinkgandroid.model.network.orders.com.example.organicinkgandroid.model.network.client



import com.google.gson.annotations.SerializedName


data class User(
    @SerializedName("cart")
    val cart: Cart,
    @SerializedName("email")
    val email: String?,
    @SerializedName("firstName")
    val firstName: String?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("lastName")
    val lastName: String?,
    @SerializedName("middleName")
    val middleName: String?,
    @SerializedName("phoneNumber")
    val phoneNumber: String
)