package com.example.organicinkgandroid.model.network.contacts

import com.google.gson.annotations.SerializedName

data class ContactInfo (
     @SerializedName("id")
     val id: Int,
     @SerializedName("phoneNumber")
     val phoneNumber: String,
     @SerializedName("address")
     val address : String,
     @SerializedName("email")
     val email: String
 )


