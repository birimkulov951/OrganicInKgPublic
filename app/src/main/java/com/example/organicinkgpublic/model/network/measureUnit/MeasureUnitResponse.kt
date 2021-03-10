package com.example.organicinkgpublic.model.network.measureUnit

import com.google.gson.annotations.SerializedName

data class MeasureUnitResponse(
    @SerializedName("description")
    val description: String,
    @SerializedName("id")
    var id: Int,
    @SerializedName("name")
    val name: String
)
