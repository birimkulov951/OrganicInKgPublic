package com.example.organicinkgpublic.model.network.refreshtoken


import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("body")
    val body: Body?,
    @SerializedName("headers")
    val headers: Headers?,
    @SerializedName("statusCode")
    val statusCode: String?,
    @SerializedName("statusCodeValue")
    val statusCodeValue: Int?
)