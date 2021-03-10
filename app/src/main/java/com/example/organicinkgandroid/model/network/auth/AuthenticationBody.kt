package com.example.organicinkgandroid.model.network.auth


import com.google.gson.annotations.SerializedName

data class AuthenticationBody(
    @SerializedName("password")
    val password: String,
    @SerializedName("username")
    val username: String
)