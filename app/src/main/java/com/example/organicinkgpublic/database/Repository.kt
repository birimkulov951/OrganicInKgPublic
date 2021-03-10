package com.example.organicinkgpublic.database

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.organicinkgpublic.database.basket.ProductDAO
import com.example.organicinkgpublic.database.basket.ProductEntity

// Declares the DAO as a private property in the constructor. Pass in the DAO instead of the whole database, because you only need access to the DAO
class Repository(/*private val filtrationDao: SettingsDAO, */private val productDao: ProductDAO) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    //val allSettings: LiveData<List<SettingsEntity>> = filtrationDao.loadAllSettings()
    val allProductsInBasket: LiveData<List<ProductEntity>> = productDao.loadAllProducts()

    // The suspend modifier tells the compiler that this needs to be called from a coroutine or another suspending function.
    /*@Suppress("RedundantSuspendModifier")*/

    // Products functions in basket
    @WorkerThread
    suspend fun insertProduct(product: ProductEntity) {
        productDao.insertProduct(product)
    }
    @WorkerThread
    suspend fun updateProduct(product: ProductEntity) {
        productDao.updateProduct(product)
    }
    @WorkerThread
    suspend fun deleteProduct(product: ProductEntity) {
        productDao.deleteProduct(product)
    }
    @WorkerThread
    suspend fun deleteProductByProductId(productId: Int) {
        productDao.deleteProductByProductId(productId)
    }
    @WorkerThread
    suspend fun deleteAllProducts() {
        productDao.deleteAllProducts()
    }
    @WorkerThread
    suspend fun loadProductById(productId: Int) : ProductEntity {
        return productDao.loadProductById(productId)
    }

}
