package com.example.organicinkgpublic.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.organicinkgpublic.database.basket.ProductEntity
import com.example.organicinkgpublic.database.basket.ProductRoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BasketViewModel(application: Application) : AndroidViewModel(application) {

    private val productRepository: Repository

    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    //val allFiltration: LiveData<List<SettingsEntity>>
    val allProducts: LiveData<List<ProductEntity>>

    init {

        //val filtrationDao = SettingsRoomDatabase.getDatabase(application).settingsDAO()
        val productsDao = ProductRoomDatabase.getDatabase(application).productDao()

        productRepository = Repository(productsDao)

        //allFiltration = productRepository.allSettings
        allProducts = productRepository.allProductsInBasket

    }

    /**
     * Launching a new coroutine to insert/update/delete a in a non-blocking way
     */
    fun insertProduct(product: ProductEntity) = viewModelScope.launch(Dispatchers.IO) {
        productRepository.insertProduct(product)
    }

    fun updateProduct(product: ProductEntity) = viewModelScope.launch(Dispatchers.IO) {
        productRepository.updateProduct(product)
    }

    fun deleteProduct(product: ProductEntity) = viewModelScope.launch(Dispatchers.IO) {
        productRepository.deleteProduct(product)
    }

    fun deleteProductByProductId(productId: Int) = viewModelScope.launch(Dispatchers.IO) {
        productRepository.deleteProductByProductId(productId)
    }

    fun deleteAllProducts() = viewModelScope.launch(Dispatchers.IO) {
        productRepository.deleteAllProducts()
    }

    fun loadProductById(productId: Int) = viewModelScope.launch(Dispatchers.IO) {
        productRepository.loadProductById(productId)
    }

}
