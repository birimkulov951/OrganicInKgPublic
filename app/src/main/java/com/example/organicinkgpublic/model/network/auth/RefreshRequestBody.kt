package com.example.organicinkgpublic.model.network.auth


import com.google.gson.annotations.SerializedName

data class RefreshRequestBody(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("refreshToken")
    val refreshToken: String,
    @SerializedName("username")
    val username: String
)