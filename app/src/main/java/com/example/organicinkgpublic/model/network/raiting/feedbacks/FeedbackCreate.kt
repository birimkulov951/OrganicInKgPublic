package com.example.organicinkgpublic.model.network.orders.com.example.organicinkgandroid.model.network.raiting.feedbacks

import com.google.gson.annotations.SerializedName

data class FeedbackCreate(
    @SerializedName("comment")
    var comment: String,
    @SerializedName("productId")
    var id: Int

)