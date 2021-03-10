package com.example.organicinkgpublic.model.network.refreshtoken


import com.google.gson.annotations.SerializedName

data class RefreshTokenResponse(
    @SerializedName("details")
    val details: Any?,
    @SerializedName("result")
    val result: Result?,
    @SerializedName("resultCode")
    val resultCode: String?
)