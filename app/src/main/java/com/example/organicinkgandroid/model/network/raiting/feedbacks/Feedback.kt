package com.example.organicinkgandroid.model.network.raiting.feedbacks

import com.google.gson.annotations.SerializedName

data class Feedback (
    @SerializedName("comment")
    var comment: String,
    @SerializedName("id")
    var id: Int,
    @SerializedName("client")
    var client: String
)