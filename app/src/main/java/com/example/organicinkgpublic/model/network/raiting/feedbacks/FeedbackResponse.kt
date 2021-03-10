package com.example.organicinkgpublic.model.network.orders.com.example.organicinkgandroid.model.network.raiting.feedbacks

import com.example.organicinkgpublic.model.network.raiting.feedbacks.Feedback
import com.google.gson.annotations.SerializedName

data class FeedbackResponse(
    @SerializedName("details")
    val details: Any,
    @SerializedName("result")
    val result: Feedback,
    @SerializedName("resultCode")
    val resultCode: String
)