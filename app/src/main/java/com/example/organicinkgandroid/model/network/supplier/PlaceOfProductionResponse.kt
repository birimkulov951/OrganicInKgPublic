package com.example.organicinkgandroid.model.network.supplier

import com.google.gson.annotations.SerializedName

data class PlaceOfProductionResponse(
    @SerializedName("result")
    val result : List<PlaceOfProduction>
)