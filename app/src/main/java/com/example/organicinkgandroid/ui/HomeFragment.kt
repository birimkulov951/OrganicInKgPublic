package com.example.organicinkgandroid.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ProgressBar
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.organicinkgandroid.*
import com.example.organicinkgandroid.adapters.ProductsAdapter
import com.example.organicinkgandroid.database.BasketViewModel
import com.example.organicinkgandroid.database.basket.ProductEntity
import com.example.organicinkgandroid.model.network.orders.com.example.organicinkgandroid.model.network.product.Search
import com.example.organicinkgandroid.model.network.product.Product
import com.example.organicinkgandroid.model.network.product.ProductsResponse
import com.example.organicinkgandroid.retrofit.ApiClient
import com.example.organicinkgandroid.retrofit.SessionManager
import com.example.organicinkgandroid.utils.Constants.PRODUCT_BOUGHT_QUANTITY
import com.example.organicinkgandroid.utils.Constants.PRODUCT_CURRENCY
import com.example.organicinkgandroid.utils.Constants.PRODUCT_DESCRIPTION
import com.example.organicinkgandroid.utils.Constants.PRODUCT_FEEDBACK_SIZE
import com.example.organicinkgandroid.utils.Constants.PRODUCT_ID
import com.example.organicinkgandroid.utils.Constants.PRODUCT_IMAGES
import com.example.organicinkgandroid.utils.Constants.PRODUCT_IS_IN_BASKET
import com.example.organicinkgandroid.utils.Constants.PRODUCT_MEASURE_UNIT
import com.example.organicinkgandroid.utils.Constants.PRODUCT_MINIMUM_ORDER_QUANTITY
import com.example.organicinkgandroid.utils.Constants.PRODUCT_NAME
import com.example.organicinkgandroid.utils.Constants.PRODUCT_PRICE
import com.example.organicinkgandroid.utils.Constants.PRODUCT_PRODUCTION_PLACE
import com.example.organicinkgandroid.utils.Constants.PRODUCT_RATING
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_search_result.*
import kotlinx.android.synthetic.main.fragment_search_result.view.*

import retrofit2.*

class HomeFragment : Fragment(), ProductsAdapter.OnItemClickListener{

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
    private var selectedCategorySM = ""
    private var selectedLocationSM = ""
    private var sortDirectionSM = ""
    private var filtrateBy = ""
    private var priceStart = -1
    private var priceEnd: Any? = null
    private var sortDirection : Any? = null
    private var selectedCategory: Any? = null
    private var selectedLocation : Any? = null
    //private var productIsAlreadyInBasket = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)
        loadMoreProgressBar = rootView.home_fragment_loading
        scrollView = rootView.scroll_view
        sessionManager = SessionManager(requireContext())
        (activity as MainActivity).showBottomNavBar("",false)
        (activity as MainActivity).hideBasketToast()

        productsAdapter = ProductsAdapter(requireContext(), this@HomeFragment)

        basketViewModel = ViewModelProvider(this).get(BasketViewModel::class.java)
        basketViewModel.allProducts.observe(requireActivity(), Observer { words ->
            // Update the cached copy of the words in the adapter.
            words?.let {
                productsAdapter.notifyDataSetChanged()
            }
        })

        apiClient = ApiClient()
        selectedCategorySM = sessionManager.fetchSelectedCategory().toString()
        selectedLocationSM = sessionManager.fetchSelectedLocation().toString()
        priceStart = sessionManager.fetchPriceStart()!!.toInt()
        priceEnd = if (sessionManager.fetchPriceEnd()!!.toInt() == -1){ null}
        else{
            sessionManager.fetchPriceEnd()
        }
        filtrateBy = sessionManager.fetchFiltrateBy().toString()
        sortDirectionSM = sessionManager.fetchSortDirection().toString()
        if (selectedCategorySM == "null" && selectedLocationSM == "null" && priceEnd == null && sortDirectionSM == "null" ){
            isSet = false
        }
        selectedCategory = if (selectedCategorySM == "null"){ null
        } else {
            selectedCategorySM
        }
        selectedLocation = if (selectedLocationSM == "null"){ null
        } else {
            selectedLocationSM
        }
        sortDirection = if (sortDirectionSM == "null") { null
        } else{
                sortDirectionSM
            }




        //get product list
        rootView.home_fragment_progressbar.visibility = View.VISIBLE
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

        /** Opens Filter settings */
        rootView.home_fragment_filter_button.setOnClickListener{
            navController.navigate(R.id.action_homeFragment_to_filterSettingsFragment)
        }

        /** SwipeRefresherLayout onClickListener*/
//        rootView.home_fragment_swipe_refresher.setColorSchemeColors(Color.parseColor("#009B00"))
//        rootView.home_fragment_swipe_refresher.setOnRefreshListener {
//            Handler(Looper.getMainLooper()).postDelayed({
//                /** Calling Fields */
//                getProductList(rootView)
//            }, 200)
//        }



        // Search
        mainSearch = rootView.edit_text_search
        mainSearch.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                productToSearch = mainSearch.text.toString()
                getProductsByName(productToSearch, rootView)
            };true
        }

        return rootView
    }

    private fun getProductsByName(productName : String, rootView:View) {
    rootView.home_fragment_progressbar.visibility = View.VISIBLE
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
            rootView.home_fragment_progressbar.visibility = View.GONE
            loadMoreProgressBar.visibility = View.GONE
            if (response.isSuccessful && response.body()!!.result.size != 0){
                rootView.home_fragment_progressbar.visibility = View.GONE
                rootView.home_fragment_not_found.visibility = View.GONE
                rootView.home_fragment_recyclerView.adapter =  productsAdapter
                productsAdapter.setItems(response.body()!!.result)
            }
            else if(response.body()!!.result.size == 0){
               // rootView.home_fragment_not_found.visibility = View.VISIBLE
            }
            else{
                (activity as MainActivity).customToast(getString(R.string.unknown_error), true)
            }
        }
    })

}




    private fun getProductList(rootView: View) : ArrayList<Product> {
        rootView.home_fragment_progressbar.visibility = View.VISIBLE
        if (isSet){
            loadMoreProgressBar.visibility = View.GONE
            val call = apiClient.getApiService().getProductsByFilter(selectedCategory, selectedLocation, priceStart, priceEnd, 30, filtrateBy, sortDirection)
            call.enqueue(object : Callback<ProductsResponse> {
                override fun onResponse(call: Call<ProductsResponse>, response: Response<ProductsResponse>) {
                    if (response.isSuccessful) {
                        if (response.body()!!.result.isEmpty()) {
                            rootView.home_fragment_progressbar.visibility = View.GONE
                            loadMoreProgressBar.visibility = View.GONE
                            rootView.home_fragment_not_found.visibility = View.VISIBLE
                        }
                        else {
                            rootView.home_fragment_progressbar.visibility = View.GONE
                            rootView.home_fragment_is_empty_text.visibility = View.GONE
                            rootView.home_fragment_not_found.visibility = View.GONE
                            rootView.home_fragment_recyclerView.adapter =  productsAdapter
                            productsAdapter.setItems(response.body()!!.result)
                            helperList = response.body()!!.result
                            loadMoreProgressBar.visibility = View.GONE
                            rootView.home_fragment_recyclerView.startLayoutAnimation()
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
                        rootView.home_fragment_progressbar.visibility = View.GONE
                        loadMoreProgressBar.visibility = View.GONE
                    } else {
                        rootView.home_fragment_progressbar.visibility = View.GONE
                        rootView.home_fragment_is_empty_text.visibility = View.GONE
                        rootView.home_fragment_recyclerView.adapter = productsAdapter
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
                            rootView.home_fragment_recyclerView.startLayoutAnimation()
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

        sender.putInt(PRODUCT_ID, clickedItem.id)
        sender.putString(PRODUCT_NAME, clickedItem.name)
        sender.putString(PRODUCT_PRODUCTION_PLACE, clickedItem.supplier.placeOfProduction.region)
        if (clickedItem.rating == null) {
            sender.putFloat(PRODUCT_RATING, -1.0f)
        } else {
            sender.putFloat(PRODUCT_RATING, clickedItem.rating!!)
        }
        sender.putStringArrayList(PRODUCT_IMAGES, listOfImages)
        sender.putInt(PRODUCT_PRICE, clickedItem.price.toInt())
        sender.putInt(PRODUCT_BOUGHT_QUANTITY, clickedItem.boughtCount)
        sender.putInt(PRODUCT_MINIMUM_ORDER_QUANTITY, clickedItem.measure)
        sender.putString(PRODUCT_DESCRIPTION, clickedItem.description)
        sender.putString(PRODUCT_CURRENCY, clickedItem.currency)
        sender.putString(PRODUCT_MEASURE_UNIT, clickedItem.measureUnitResponse.name)
        sender.putBoolean(PRODUCT_IS_IN_BASKET, (activity as MainActivity).productsIdInBasket.contains(clickedItem.id))
        sender.putInt(PRODUCT_FEEDBACK_SIZE, clickedItem.comments.size)

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
