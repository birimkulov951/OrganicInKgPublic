package com.example.organicinkgandroid.model.network


import com.google.gson.annotations.SerializedName

data class Body(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("refreshExpirationTime")
    val refreshExpirationTime: String,
    @SerializedName("refreshToken")
    val refreshToken: String,
    @SerializedName("tokenExpirationTime")
    val tokenExpirationTime: String
)