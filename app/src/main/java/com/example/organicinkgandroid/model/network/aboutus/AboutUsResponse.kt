package com.example.organicinkgandroid.model.network.aboutus

import com.google.gson.annotations.SerializedName

data class AboutUsResponse (
    @SerializedName("header")
    val header: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("order")
    val order: Int,
    @SerializedName("paragraph")
    val paragraph: String
)