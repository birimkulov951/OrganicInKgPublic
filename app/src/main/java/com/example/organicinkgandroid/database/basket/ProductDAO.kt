package com.example.organicinkgandroid.database.basket

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ProductDAO {

    @Query("SELECT * FROM basket_table ORDER BY productTimeAdded DESC")
    fun loadAllProducts() : LiveData<List<ProductEntity>>

    @Query("SELECT * FROM basket_table WHERE productId = :productId")
    fun loadProductById(productId: Int) : ProductEntity

    @Insert
    fun insertProduct(product: ProductEntity)

    @Update
    fun updateProduct(product: ProductEntity)

    @Delete
    fun deleteProduct(product: ProductEntity)

    @Query("DELETE FROM basket_table WHERE productId = :productId")
    fun deleteProductByProductId(productId: Int)

    @Query("DELETE FROM basket_table")
    fun deleteAllProducts()

}