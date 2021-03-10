package com.example.organicinkgpublic.model.network.auth


import com.google.gson.annotations.SerializedName

data class UserEntityBody(

    @SerializedName("authorities")
    val authorities: List<String>,

    @SerializedName("password")
    val password: String,

    @SerializedName("username")
    val username: String

)