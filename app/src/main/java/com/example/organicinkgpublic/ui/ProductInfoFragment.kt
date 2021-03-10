package com.example.organicinkgpublic.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.viewpager2.widget.ViewPager2
import com.example.organicinkgpublic.MainActivity
import com.example.organicinkgpublic.R
import com.example.organicinkgpublic.adapters.ImageSliderAdapter
import com.example.organicinkgpublic.database.BasketViewModel
import com.example.organicinkgpublic.database.basket.ProductEntity
import com.example.organicinkgpublic.model.network.product.ProductResponse
import com.example.organicinkgpublic.retrofit.ApiClient
import com.example.organicinkgpublic.utils.Constants.PRODUCT_BOUGHT_QUANTITY
import com.example.organicinkgpublic.utils.Constants.PRODUCT_CURRENCY
import com.example.organicinkgpublic.utils.Constants.PRODUCT_DESCRIPTION
import com.example.organicinkgpublic.utils.Constants.PRODUCT_FEEDBACK_SIZE
import com.example.organicinkgpublic.utils.Constants.PRODUCT_ID
import com.example.organicinkgpublic.utils.Constants.PRODUCT_IMAGES
import com.example.organicinkgpublic.utils.Constants.PRODUCT_IS_IN_BASKET
import com.example.organicinkgpublic.utils.Constants.PRODUCT_MEASURE_UNIT
import com.example.organicinkgpublic.utils.Constants.PRODUCT_MINIMUM_ORDER_QUANTITY
import com.example.organicinkgpublic.utils.Constants.PRODUCT_NAME
import com.example.organicinkgpublic.utils.Constants.PRODUCT_PRICE
import com.example.organicinkgpublic.utils.Constants.PRODUCT_PRODUCTION_PLACE
import com.example.organicinkgpublic.utils.Constants.PRODUCT_RATING
import com.example.organicinkgpublic.utils.ZoomOutPageTransformer
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator
import kotlinx.android.synthetic.main.fragment_product_info.view.*
import retrofit2.Call
import retrofit2.Response

class ProductInfoFragment : Fragment() {

    private lateinit var mainViewModel: BasketViewModel
    private lateinit var navController : NavController
    private lateinit var apiClient: ApiClient

    // Helpers
    private var productId = -1
    private var productName = "null"
    private var productProductionPlace = "null"
    private var productRating = -1.0f
    private var productImages = ArrayList<String>()
    private var productPrice = -1.0f
    private var productCurrency = "null"
    private var productMeasureUnit = "null"
    private var productBoughtQuantity = -1
    private val productTimeAdded = System.currentTimeMillis()
    private var productMinimumOrderQuantity = -1
    private var productDescription = "null"
    private var productIsAlreadyInBasket = false
    private var productFeedbackSize = 0

    @Suppress("UNCHECKED_CAST")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_product_info, container, false)
        (activity as MainActivity).hideBasketToast()
        (activity as MainActivity).showBottomNavBar("", false)

        mainViewModel = ViewModelProvider(this).get(BasketViewModel::class.java)
        apiClient = ApiClient()

        /** Back Button*/
        rootView.product_info_back_button.setOnClickListener{
            activity?.onBackPressed()
        }

        /** Getting all data*/
        productId = requireArguments().getInt(PRODUCT_ID, -1)
        productName = requireArguments().getString(PRODUCT_NAME, "null")
        productProductionPlace = requireArguments().getString(PRODUCT_PRODUCTION_PLACE, "null")
        productRating = requireArguments().getFloat(PRODUCT_RATING,-1.0f)
        productImages = requireArguments().getStringArrayList(PRODUCT_IMAGES) as ArrayList<String>
        productPrice = requireArguments().getFloat(PRODUCT_PRICE, -1.0f)
        productBoughtQuantity = requireArguments().getInt(PRODUCT_BOUGHT_QUANTITY, -1)
        productMinimumOrderQuantity = requireArguments().getInt(PRODUCT_MINIMUM_ORDER_QUANTITY, -1)
        productDescription = requireArguments().getString(PRODUCT_DESCRIPTION, "null")
        productIsAlreadyInBasket = requireArguments().getBoolean(PRODUCT_IS_IN_BASKET, false)
        productCurrency = requireArguments().getString(PRODUCT_CURRENCY, "сом")
        productMeasureUnit = requireArguments().getString(PRODUCT_MEASURE_UNIT, "кг")
        productFeedbackSize = requireArguments().getInt(PRODUCT_FEEDBACK_SIZE, 0)

        var productImagesHelper = ""
        productImages.forEach{
            if (productImagesHelper == "") {
                productImagesHelper = it
            } else {
                productImagesHelper = "$productImagesHelper&$it"
            }
        }

        /** Image adapter, dots indicator and zoom out animation*/
        val dotsIndicator = rootView.findViewById<SpringDotsIndicator>(R.id.product_info_fragment_dots_indicator)
        val viewPager2 = rootView.findViewById<ViewPager2>(R.id.product_info_fragment_slider_view_pager)
        val imageSliderAdapter = ImageSliderAdapter(productImages,requireContext())
        viewPager2.adapter = imageSliderAdapter

        val zoomOutPageTransformer = ZoomOutPageTransformer()
        viewPager2.setPageTransformer { page, position ->
            zoomOutPageTransformer.transformPage(page, position)
        }
        dotsIndicator.setViewPager2(viewPager2)


        /** Buy product */
        rootView.product_info_to_buy_button.setOnClickListener{

            val sender = Bundle()

            // Send productId to show right RecyclerView item in BasketFragment
            sender.putInt(PRODUCT_ID, productId)

            if (!productIsAlreadyInBasket) {


                if(productMinimumOrderQuantity == -1 || productMinimumOrderQuantity == 0) {
                    productMinimumOrderQuantity = 1
                }

                val product = ProductEntity(
                    productId, productName, productProductionPlace, productRating,
                    productImagesHelper,
                    productPrice.toFloat(),productCurrency,productMeasureUnit, productBoughtQuantity, productMinimumOrderQuantity,
                    productDescription, productTimeAdded, productMinimumOrderQuantity, 0)

                mainViewModel.insertProduct(product)

                rootView.product_info_to_basket_button.text = getText(R.string.in_basket)
                rootView.product_info_to_basket_button.setBackgroundResource(R.drawable.in_cart_button_background)

                navController.navigate(R.id.action_singleProductFragment_to_basketFragment,sender)
            } else {

                navController.navigate(R.id.action_singleProductFragment_to_basketFragment,sender)

            }

        }

        /** Add product ot Basket*/
        if (productIsAlreadyInBasket) {
            rootView.product_info_to_basket_button.text = getText(R.string.in_basket)
            rootView.product_info_to_basket_button.setBackgroundResource(R.drawable.in_cart_button_background)
        }
        rootView.product_info_to_basket_button.setOnClickListener {

            if(productMinimumOrderQuantity == -1 || productMinimumOrderQuantity == 0) {
                productMinimumOrderQuantity = 1
            }

            if (!productIsAlreadyInBasket) {
                productIsAlreadyInBasket = true
                val product = ProductEntity(
                    productId, productName, productProductionPlace, productRating,
                    productImagesHelper, productPrice.toFloat(),productCurrency,productMeasureUnit, productBoughtQuantity, productMinimumOrderQuantity,
                    productDescription, productTimeAdded, productMinimumOrderQuantity, 0)

                mainViewModel.insertProduct(product)

                rootView.product_info_to_basket_button.text = getText(R.string.in_basket)
                rootView.product_info_to_basket_button.setBackgroundResource(R.drawable.in_cart_button_background)
            } else {
                productIsAlreadyInBasket = false

                mainViewModel.deleteProductByProductId(productId)


                rootView.product_info_to_basket_button.text = getText(R.string.to_cart)
                rootView.product_info_to_basket_button.setBackgroundResource(R.drawable.to_cart_button_background)
            }

        }

        /** More Info Butoon listener*/
        rootView.product_info_more_details_button.setOnClickListener{

        }


        /** Binding views*/
        bindingViews(rootView)


        /** To open FeedBacks Fragment */
        rootView.text_view_feedback.text = "Отзывы ($productFeedbackSize)"
        rootView.product_info_product_feedbacks.setOnClickListener{
            val sender = Bundle()
            sender.putInt(PRODUCT_ID, productId)
            navController.navigate(R.id.action_singleProductFragment_to_feedbackFragment,sender)
        }

        updateViews(rootView)

        return rootView
    }

    private fun updateViews(rootView: View) {
        val call = apiClient.getApiService().getProductById(productId)
        call.enqueue(object:retrofit2.Callback<ProductResponse> {

            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<ProductResponse>, response: Response<ProductResponse>) {

                if (response.isSuccessful){

                    rootView.product_info_product_purchased_quantity.text = " ${response.body()!!.result.boughtCount} раз"
                    if(response.body()!!.result.measure != 0) {
                        rootView.product_info_product_minimum_order_quantity.text = " ${response.body()!!.result.measure} кг"
                    }


                    Log.e("tesssssssst", "onResponse: ${response.body()!!.result.boughtCount.toString()}" )
                    /*val product = ProductEntity(
                        productId, productName, productProductionPlace, productRating,
                        productImagesHelper,
                        productPrice.toFloat(),productCurrency,productMeasureUnit, productBoughtQuantity, productMinimumOrderQuantity,
                        productDescription, productTimeAdded, productMinimumOrderQuantity, 0)

                    mainViewModel.insertProduct(product)*/
                }

            }

            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                Log.e("ProductInfoFragment", t.message.toString())
            }

        })
    }


    @SuppressLint("SetTextI18n")
    private fun bindingViews(rootView: View) {
        if (productName != "null"){
            rootView.product_info_toolbar_name.text = productName
            rootView.product_info_product_name.text = productName
        }
        if (productProductionPlace != "null"){
            rootView.product_info_toolbar_location.text = productProductionPlace
            rootView.product_info_product_location.text = productProductionPlace
        }
        Log.e("productRating", productRating.toString())

        if (productRating != -1.0f) {
            if (productRating == 0.0f) {
                rootView.product_info_toolbar_rating_icon.setImageResource(R.drawable.ic_star_zero)
                rootView.product_info_product_rating_icon.setImageResource(R.drawable.ic_star_zero)
                rootView.product_info_product_rating.text = ""
                rootView.product_info_toolbar_rating.text = ""
            }
            else{
                rootView.product_info_product_rating.text = productRating.toString()
                rootView.product_info_toolbar_rating.text = productRating.toString()
                when (productRating) {
                    in 4.6..5.0 -> {
                        rootView.product_info_toolbar_rating_icon.setImageResource(R.drawable.ic_star_five)
                        rootView.product_info_product_rating_icon.setImageResource(R.drawable.ic_star_five)
                    }
                    in 4.0..4.59 -> {
                        rootView.product_info_toolbar_rating_icon.setImageResource(R.drawable.ic_star_four)
                        rootView.product_info_product_rating_icon.setImageResource(R.drawable.ic_star_four)
                    }
                    in 3.0..3.99 -> {
                        rootView.product_info_toolbar_rating_icon.setImageResource(R.drawable.ic_star_three)
                        rootView.product_info_product_rating_icon.setImageResource(R.drawable.ic_star_three)
                    }
                    in 2.0..2.99 -> {
                        rootView.product_info_toolbar_rating_icon.setImageResource(R.drawable.ic_star_two)
                        rootView.product_info_product_rating_icon.setImageResource(R.drawable.ic_star_two)
                    }
                    in 1.0..1.99 -> {
                        rootView.product_info_toolbar_rating_icon.setImageResource(R.drawable.ic_star_one)
                        rootView.product_info_product_rating_icon.setImageResource(R.drawable.ic_star_one)
                    }
                    else -> {
                        rootView.product_info_toolbar_rating_icon.setImageResource(R.drawable.ic_star_zero)
                        rootView.product_info_product_rating_icon.setImageResource(R.drawable.ic_star_zero)
                    }
                }
            }
        }

        if (productPrice != -1.0f){
            rootView.product_info_product_price.text = productPrice.toString()
        }
        if (productCurrency != "null"){
            rootView.product_info_currency.text = " $productCurrency"
        }
        if (productMeasureUnit != "null"){
            rootView.product_info_measure_unit.text = " /$productMeasureUnit"
        }
        if (productBoughtQuantity != -1){
            rootView.product_info_product_purchased_quantity.text = " $productBoughtQuantity раз"
        } else {
            rootView.product_info_product_purchased_quantity.text = " 0 раз"
        }
        if (productMinimumOrderQuantity != -1){
            rootView.product_info_product_minimum_order_quantity.text = " $productMinimumOrderQuantity кг"
        } else {
            productMinimumOrderQuantity = 10
            rootView.product_info_product_minimum_order_quantity.text = " 10 кг"
        }
        if (productDescription != "null" || productDescription != ""){
            rootView.product_info_product_description.text = productDescription

            Log.e("TAG", "bindingViews: ${rootView.product_info_product_description.lineCount}")
            if (rootView.product_info_product_description.lineCount > 3) {
                rootView.product_info_more_details_button.ellipsize = TextUtils.TruncateAt.END
                rootView.product_info_more_details_button.visibility = View.VISIBLE
            } else {
                rootView.product_info_more_details_button.visibility = View.GONE
            }

        } else {
            rootView.product_info_more_details_button.visibility = View.GONE
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }

}