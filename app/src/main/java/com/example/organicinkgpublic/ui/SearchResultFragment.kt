package com.example.organicinkgpublic.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.organicinkgpublic.MainActivity
import com.example.organicinkgpublic.R
import com.example.organicinkgpublic.adapters.ProductsAdapter
import com.example.organicinkgpublic.database.BasketViewModel
import com.example.organicinkgpublic.database.basket.ProductEntity
import com.example.organicinkgpublic.model.network.orders.com.example.organicinkgandroid.model.network.product.Search
import com.example.organicinkgpublic.model.network.product.Product
import com.example.organicinkgpublic.retrofit.ApiClient
import com.example.organicinkgpublic.utils.Constants
import kotlinx.android.synthetic.main.fragment_search_result.*
import kotlinx.android.synthetic.main.fragment_search_result.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchResultFragment : Fragment(), ProductsAdapter.OnItemClickListener{

    lateinit var productsAdapter: ProductsAdapter

    private lateinit var basketViewModel: BasketViewModel

    lateinit var navController: NavController
    private lateinit var apiClient: ApiClient

    // Vars
    var listItems: ArrayList<Product> = ArrayList()
    lateinit var productName: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_search_result, container, false)
        apiClient = ApiClient()

        productName = arguments?.getString("productName").toString()

        basketViewModel = ViewModelProvider(this).get(BasketViewModel::class.java)
        basketViewModel.allProducts.observe(requireActivity(), Observer { words ->
            // Update the cached copy of the words in the adapter.
            words?.let {
               // productsAdapter.notifyDataSetChanged()
            }
        })

        rootView.search_result_prod_name.text = productName


        rootView.search_result_back_button.setOnClickListener {
            requireActivity().onBackPressed()
        }
        rootView.search_result_filter_button.setOnClickListener {
            navController.navigate(R.id.action_searchResultFragment2_to_filterSettingsFragment)
        }

        getProductsByName(productName, rootView)

        return rootView

    }


    private fun getProductsByName(productName: String, rootView : View) {
        Log.e("product tp search", productName)
        rootView.search_result_progressbar.visibility = View.VISIBLE
        val call = apiClient.getApiService().getProductsByName(productName)
        call.enqueue(object : Callback<Search> {

            override fun onFailure(call: Call<Search>, t: Throwable) {
                (activity as MainActivity).customToast(getString(R.string.unknown_error), true)
                Log.e("search error" , t.message.toString())}

            override fun onResponse(
                call: Call<Search>,
                response: Response<Search>
            ) {
                hideKeyBoard()
                rootView.search_result_progressbar.visibility = View.GONE
                if (response.isSuccessful && response.body()!!.result.size != 0){
                    rootView.search_result_progressbar.visibility = View.GONE
                    listItems = response.body()!!.result
                    updateUI()
                }
                else if(response.body()!!.result.size == 0){
                    rootView.search_result_not_found_message.visibility = View.VISIBLE
                }
                else{
                    (activity as MainActivity).customToast(getString(R.string.unknown_error), true)
                }
            }
        })

    }

    private fun updateUI() {
        productsAdapter = ProductsAdapter( requireContext(), this)
        search_result_recycler_view.adapter = productsAdapter
        productsAdapter.setItems(listItems)
    }

    override fun onItemClick(v: View?, position: Int, id: Long) {
        val clickedItem: Product  = productsAdapter.getProductAt(position)
        val sender = Bundle()

        val listOfImages = ArrayList<String>()
        for (element in clickedItem.productImages) {
            listOfImages.add(element.imageUrl)
        }

        sender.putInt(Constants.PRODUCT_ID, clickedItem.id)
        sender.putString(Constants.PRODUCT_NAME, clickedItem.name)
        sender.putString(Constants.PRODUCT_PRODUCTION_PLACE, clickedItem.supplier.placeOfProduction.region)
        if (clickedItem.rating == null) {
            sender.putFloat(Constants.PRODUCT_RATING, -1.0f)
        } else {
            sender.putFloat(Constants.PRODUCT_RATING, clickedItem.rating!!)
        }
        sender.putStringArrayList(Constants.PRODUCT_IMAGES, listOfImages)
        sender.putInt(Constants.PRODUCT_PRICE, clickedItem.price.toInt())
        sender.putInt(Constants.PRODUCT_BOUGHT_QUANTITY, clickedItem.boughtCount)
        sender.putInt(Constants.PRODUCT_MINIMUM_ORDER_QUANTITY, clickedItem.measure)
        sender.putString(Constants.PRODUCT_DESCRIPTION, clickedItem.description)
        sender.putString(Constants.PRODUCT_CURRENCY, clickedItem.currency)
        sender.putString(Constants.PRODUCT_MEASURE_UNIT, clickedItem.measureUnitResponse.name)
        sender.putBoolean(Constants.PRODUCT_IS_IN_BASKET, (activity as MainActivity).productsIdInBasket.contains(clickedItem.id))

        Log.e("TAG", "isProductInBasket: ${(activity as MainActivity).productsIdInBasket}  ${(activity as MainActivity).productsIdInBasket.contains(clickedItem.id)} ${clickedItem.id}")

        navController.navigate(R.id.action_searchResultFragment2_to_singleProductFragment, sender)
    }

    override fun onAddItemToBasket(v: View?, position: Int, id: Long) {
        val clickedItem: Product  = productsAdapter.getProductAt(position)

        val productId = clickedItem.id
        val productName = clickedItem.name
        val productProductionPlace = clickedItem.supplier.placeOfProduction.region
        val productRating = clickedItem.rating
        val productPrice = clickedItem.price
        val productCurrency = clickedItem.currency
        val productMeasureUnit = clickedItem.measureUnitResponse.name
        val productBoughtQuantity = -1
        val productTimeAdded = System.currentTimeMillis()
        var productMinimumOrderQuantity = clickedItem.measure
        val productDescription = clickedItem.description

        var productImages = ""
        clickedItem.productImages.forEach{
            if (productImages == "") {
                productImages = it.imageUrl
            } else {
                productImages = productImages + "&" + it.imageUrl
            }
        }

        if(productMinimumOrderQuantity == 0) {
            productMinimumOrderQuantity = 1
        }

        productsAdapter.getPositionOfAnimatedButton(position)

        if (!(context as MainActivity).productsIdInBasket.contains(clickedItem.id)) {
            (activity as MainActivity).customToast(getString(R.string.product_added_to_basket), false)
            val product = ProductEntity(
                productId, productName, productProductionPlace, productRating,
                productImages, productPrice, productCurrency, productMeasureUnit, productBoughtQuantity, productMinimumOrderQuantity,
                productDescription, productTimeAdded, productMinimumOrderQuantity, 0)

            basketViewModel.insertProduct(product)

        } else {
            basketViewModel.deleteProductByProductId(clickedItem.id)
        }
    }

    private fun hideKeyBoard() {
        val view = activity?.currentFocus
        if (view != null) {
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm!!.hideSoftInputFromWindow(requireView().windowToken, 0)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }

}
