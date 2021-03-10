package com.example.organicinkgpublic.model.network.orders.com.example.organicinkgandroid.model.network.client

import com.google.gson.annotations.SerializedName

data class Info (
    @SerializedName("email")
    var email : String,
    @SerializedName("firstName")
    var firstName: String,
    @SerializedName("lastName")
    var lastName: String,
    @SerializedName("middleName")
    var middleName : String
)