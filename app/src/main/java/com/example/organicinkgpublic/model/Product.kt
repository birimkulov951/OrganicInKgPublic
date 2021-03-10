package com.example.organicinkgpublic.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product (

   // var productId : Int,
    @SerializedName("") var productName: String,
    var region: String,
    var productPrice : String,
    var productRating : String,
    var productImage : Int,
    var ratingImage : Int,
    var productDescription : String,
    var minToBuy : Int,
    var timesPurchased : Int





):Parcelable