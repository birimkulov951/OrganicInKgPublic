package com.example.organicinkgpublic.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ProgressBar
import androidx.core.widget.NestedScrollView
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
import com.example.organicinkgpublic.model.network.product.Product
import com.example.organicinkgpublic.model.network.product.ProductsResponse
import com.example.organicinkgpublic.retrofit.ApiClient
import com.example.organicinkgpublic.retrofit.SessionManager
import com.example.organicinkgpublic.utils.Constants
import kotlinx.android.synthetic.main.fragment_categories_sub.view.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoriesSubFragment : Fragment(), ProductsAdapter.OnItemClickListener {

    lateinit var mainSearch: EditText
    lateinit var productToSearch: String
    lateinit var sessionManager: SessionManager

    private lateinit var basketViewModel: BasketViewModel
    lateinit var navController: NavController
    lateinit var productsAdapter: ProductsAdapter
    private lateinit var apiClient: ApiClient
    private var page = 1
    private var size = 16
    private var loading = true
    var isSet = true
    private var helperList = ArrayList<Product>()
    private lateinit var scrollView : NestedScrollView
    private lateinit var loadMoreProgressBar : ProgressBar

    // Helpers
    private var categoryData = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView: View =  inflater.inflate(R.layout.fragment_categories_sub, container, false)

        val bundle = this.arguments
        var categoryData = ""
        if (bundle != null) {
            categoryData = bundle.getString("CATEGORY_DATA", "-1")
        }

        rootView.categories_sub_fragment_name.text = categoryData

        rootView.categories_sub_fragment_toolbar.setOnClickListener{
            (activity as MainActivity).onBackPressed()
        }


        loadMoreProgressBar = rootView.categories_sub_fragment_loading
        scrollView = rootView.nested_scroll_view_2
        sessionManager = SessionManager(requireContext())
        (activity as MainActivity).showBottomNavBar("",false)
        (activity as MainActivity).hideBasketToast()

        productsAdapter = ProductsAdapter(requireContext(), this@CategoriesSubFragment)

        basketViewModel = ViewModelProvider(this).get(BasketViewModel::class.java)
        basketViewModel.allProducts.observe(requireActivity(), Observer { words ->
            // Update the cached copy of the words in the adapter.
            words?.let {
                productsAdapter.notifyDataSetChanged()
            }
        })

        apiClient = ApiClient()

        //get product list
        rootView.categories_sub_fragment_progressbar.visibility = View.VISIBLE
        getProductList(rootView)
        if (!isSet) {
            Log.e("hey", "I am")
            scrollView.setOnScrollChangeListener { v: NestedScrollView, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
                if (scrollY == v.getChildAt(0).measuredHeight.minus(v.measuredHeight)) {
                    if (loading) {
                        page++
                        loadMoreProgressBar.visibility = View.VISIBLE
                        getProductList(rootView)
                        loading = false
                    }
                }
            }
        }

        return rootView
    }


    private fun getProductList(rootView: View) : ArrayList<Product> {
        rootView.categories_sub_fragment_progressbar.visibility = View.VISIBLE
        if (isSet){
            loadMoreProgressBar.visibility = View.GONE
            val call = apiClient.getApiService().getProductsBySubCategory(categoryData)
            call.enqueue(object : Callback<ProductsResponse> {
                override fun onResponse(call: Call<ProductsResponse>, response: Response<ProductsResponse>) {
                    if (response.isSuccessful) {
                        if (response.body()!!.result.isEmpty()) {
                            rootView.categories_sub_fragment_progressbar.visibility = View.GONE
                            loadMoreProgressBar.visibility = View.GONE
                            rootView.categories_sub_fragment_not_found.visibility = View.VISIBLE
                        }
                        else {
                            rootView.categories_sub_fragment_progressbar.visibility = View.GONE
                            rootView.categories_sub_fragment_is_empty_text.visibility = View.GONE
                            rootView.categories_sub_fragment_not_found.visibility = View.GONE
                            rootView.categories_sub_fragment_recyclerView.adapter =  productsAdapter
                            productsAdapter.setItems(response.body()!!.result)
                            helperList = response.body()!!.result
                            loadMoreProgressBar.visibility = View.GONE
                            rootView.categories_sub_fragment_recyclerView.startLayoutAnimation()
                        }

                    } else{
                        (activity as MainActivity).customToast(getString(R.string.unknown_error),true)
                        Log.e("Home", "${response.body()?.result?.size}")
                    }

                }

                override fun onFailure(call: Call<ProductsResponse>, t: Throwable) {
                    Log.e("HomeFragment", "Failed to retrieve ${t.message}")
                    if (!isNetworkAvailable(context)) {
                        /* rootView.home_fragment_progressbar.visibility = View.GONE
                         rootView.home_fragment_is_empty_text.visibility = View.VISIBLE
                         rootView.home_fragment_is_empty_text.text = getString(R.string.no_internet)*/

                        (activity as MainActivity).customToast(getString(R.string.no_internet),true)
                        //rootView.home_fragment_swipe_refresher.isRefreshing = false
                    } else {
                        (activity as MainActivity).customToast(getString(R.string.unknown_error),true)
                    }
                }

            })
            return helperList
        }

        else {
            val call = apiClient.getApiService().getProductList(page, size)
            call.enqueue(object : Callback<ProductsResponse> {
                override fun onResponse(
                    call: Call<ProductsResponse>,
                    response: Response<ProductsResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()!!.result.isEmpty()) {
                            rootView.categories_sub_fragment_progressbar.visibility = View.GONE
                            loadMoreProgressBar.visibility = View.GONE
                        } else {
                            rootView.categories_sub_fragment_progressbar.visibility = View.GONE
                            rootView.categories_sub_fragment_is_empty_text.visibility = View.GONE
                            rootView.categories_sub_fragment_recyclerView.adapter = productsAdapter
                            if (helperList.size != 0) {
                                productsAdapter.loadMore(response.body()!!.result)
                            } else {
                                productsAdapter.setItems(response.body()!!.result)
                            }
                            helperList = response.body()!!.result
                            if (helperList.size < 4){
                                loadMoreProgressBar.visibility = View.GONE
                            }
                            else {
                                loadMoreProgressBar.visibility = View.VISIBLE
                                loading = true
                                rootView.categories_sub_fragment_recyclerView.startLayoutAnimation()
                            }
                        }
                    } else {
                        (activity as MainActivity).customToast(getString(R.string.unknown_error), true)
                        Log.e("Home", "${response.body()?.result?.size}")
                    }

                }

                override fun onFailure(call: Call<ProductsResponse>, t: Throwable) {
                    Log.e("HomeFragment", "Failed to retrieve ${t.message}")
                    if (!isNetworkAvailable(context)) {
                        /* rootView.home_fragment_progressbar.visibility = View.GONE
                        rootView.home_fragment_is_empty_text.visibility = View.VISIBLE
                        rootView.home_fragment_is_empty_text.text = getString(R.string.no_internet)*/

                        (activity as MainActivity).customToast(getString(R.string.no_internet), true)
                        //rootView.home_fragment_swipe_refresher.isRefreshing = false
                    } else {
                        (activity as MainActivity).customToast(getString(R.string.unknown_error), true)
                    }
                }

            })
        }
        return helperList
    }



    fun isNetworkAvailable(context: Context?): Boolean {
        if (context == null) return false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
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

        navController.navigate(R.id.action_homeFragment_to_singleProductFragment, sender)

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