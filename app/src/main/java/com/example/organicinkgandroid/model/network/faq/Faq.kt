package com.example.organicinkgandroid.model.network.faq

import com.google.gson.annotations.SerializedName

data class Faq(
    @SerializedName("answer")
    var answer: String,
    @SerializedName("id")
    var id: Int,
    @SerializedName("order")
    var order: Int,
    @SerializedName("question")
    var question : String
)