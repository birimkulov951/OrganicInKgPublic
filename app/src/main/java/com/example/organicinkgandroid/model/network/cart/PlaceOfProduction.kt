package com.example.organicinkgandroid.model.network.cart

import com.google.gson.annotations.SerializedName

data class PlaceOfProduction(
@SerializedName("city")
val city: String,
@SerializedName("country")
val country : String,
@SerializedName("region")
val region: String,
@SerializedName("street")
val street: String
)

