package com.example.organicinkgandroid.database.basket

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "basket_table")
data class ProductEntity(

    @ColumnInfo(name = "productId")
    var productId: Int,

    @ColumnInfo(name = "productName")
    var productName: String,

    @ColumnInfo(name = "productProductionPlace")
    var productProductionPlace: String,

    @ColumnInfo(name = "productRating")
    var productRating: Float?,

    @ColumnInfo(name = "productImage")
    var productImage: String,

    @ColumnInfo(name = "productPrice")
    var productPrice: Float,

    @ColumnInfo(name = "productCurrency")
    var productCurrency: String,

    @ColumnInfo(name = "productMeasureUnit")
    var productMeasureUnit: String,

    @ColumnInfo(name = "productBoughtQuantity")
    var productBoughtQuantity: Int,

    @ColumnInfo(name = "productMinimumOrderQuantity")
    var productMinimumOrderQuantity: Int,

    @ColumnInfo(name = "productDescription")
    var productDescription: String,

    @ColumnInfo(name = "productTimeAdded")
    var productTimeAdded: Long,

    @ColumnInfo(name = "productQuantityToOrder")
    var productQuantityToOrder: Int,

    @ColumnInfo(name = "isCheckedToBeOrdered")
    var isCheckedToBeOrdered: Int // 0 is for FALSE. 1 is fro TRUE.

){

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

}