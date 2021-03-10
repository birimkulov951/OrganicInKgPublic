package com.example.organicinkgpublic.ui.order

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.organicinkgpublic.MainActivity
import com.example.organicinkgpublic.R
import com.example.organicinkgpublic.database.BasketViewModel
import com.example.organicinkgpublic.model.network.cart.AddProductToBuyListResponse
import com.example.organicinkgpublic.model.network.elsom.UrlRequestBody
import com.example.organicinkgpublic.model.network.orders.OrderRequestBody
import com.example.organicinkgpublic.model.network.orders.createorder.OrderResponse2
import com.example.organicinkgpublic.model.network.orders.elsom.GetUrlResponse
import com.example.organicinkgpublic.model.network.test.DeletedServerResponse
import com.example.organicinkgpublic.retrofit.ApiClient
import com.example.organicinkgpublic.retrofit.SessionManager
import com.example.organicinkgpublic.utils.Constants
import com.example.organicinkgpublic.utils.Constants.CASH
import com.example.organicinkgpublic.utils.Constants.ELSOM
import kotlinx.android.synthetic.main.fragment_summary.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SummaryFragment : Fragment() {

    private val TAG = "SummaryFragment"

    private lateinit var basketViewModel: BasketViewModel
    private lateinit var navController: NavController
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient

    // Global payment vars
    private var paymentMethodData: String = "empty"
    private var paymentMethodNumberData: String = "empty"
    private var shipmentMethodData: String = "empty"
    private var shipmentAddressData: String = "empty"
    private var desiredDateData: String = "empty"

    // Contact Info Vars
    private var contactName: String = "empty"
    private var contactSurname: String = "empty"
    private var contactPhoneNumber: String = "empty"
    private var isChecked: Boolean = false
    private var cartIdArray = ArrayList<Int>()

    private var orderNumber: String = "empty"
    private var totalPrice: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView: View = inflater.inflate(R.layout.fragment_summary, container, false)

        apiClient = ApiClient()
        sessionManager = SessionManager(requireContext())
        
        paymentMethodData = sessionManager.fetchPaymentMethod()!!
        paymentMethodNumberData = sessionManager.fetchPaymentMethodNumber()!!
        shipmentMethodData = sessionManager.fetchShipmentMethod()!!
        shipmentAddressData = sessionManager.fetchShipmentMethodAddress()!!
        desiredDateData = sessionManager.fetchDesiredDate()!!
        contactName = sessionManager.fetchContactName()!!
        contactSurname = sessionManager.fetchContactSurname()!!

        if(paymentMethodData == ELSOM) {
            rootView.summary_fragment_confirm.text = getString(R.string.elsom_payment)
        }

        // Initializing product's total price
        totalPrice = (activity as MainActivity).totalPrice.toInt()
        if (shipmentMethodData == Constants.COURIER) {
            totalPrice += sessionManager.fetchCourierPrice()
            rootView.summary_fragment_shipment.text = sessionManager.fetchCourierPrice().toString()
        }
        rootView.summary_fragment_price.text = totalPrice.toString()

        // Initializing products
        (activity as MainActivity).productsToBeOrderedInBasket2.forEach{ item ->

            try {
                val productCurrency = (activity as MainActivity).productsToBeOrderedInBasket2[0].productCurrency
                rootView.summary_fragment_currency.text = productCurrency

                val productName1 = (activity as MainActivity).productsToBeOrderedInBasket2[0].productName
                val productPrice1 = (activity as MainActivity).productsToBeOrderedInBasket2[0].productPrice
                rootView.summary_fragment_product_name_1.text = productName1
                rootView.summary_fragment_product_price_1.text = "${productPrice1.toInt()*(activity as MainActivity).productsToBeOrderedInBasket2[0].productQuantityToOrder}"


                val productName2 = (activity as MainActivity).productsToBeOrderedInBasket2[1].productName
                val productPrice2 = (activity as MainActivity).productsToBeOrderedInBasket2[1].productPrice
                rootView.summary_fragment_product_name_2.text = productName2
                rootView.summary_fragment_product_price_2.text = "${productPrice2.toInt()*(activity as MainActivity).productsToBeOrderedInBasket2[1].productQuantityToOrder}"
                rootView.summary_fragment_product_name_2.visibility = View.VISIBLE
                rootView.summary_fragment_product_price_2.visibility = View.VISIBLE

                val productName3 = (activity as MainActivity).productsToBeOrderedInBasket2[2].productName
                val productPrice3 = (activity as MainActivity).productsToBeOrderedInBasket2[2].productPrice
                rootView.summary_fragment_product_name_3.text = productName3
                rootView.summary_fragment_product_price_3.text = "${productPrice3.toInt()*(activity as MainActivity).productsToBeOrderedInBasket2[2].productQuantityToOrder}"
                rootView.summary_fragment_product_name_3.visibility = View.VISIBLE
                rootView.summary_fragment_product_price_3.visibility = View.VISIBLE

                val productName4 = (activity as MainActivity).productsToBeOrderedInBasket2[3].productName
                val productPrice4 = (activity as MainActivity).productsToBeOrderedInBasket2[3].productPrice
                rootView.summary_fragment_product_name_4.text = productName4
                rootView.summary_fragment_product_price_4.text = "${productPrice4.toInt()*(activity as MainActivity).productsToBeOrderedInBasket2[3].productQuantityToOrder}"
                rootView.summary_fragment_product_name_4.visibility = View.VISIBLE
                rootView.summary_fragment_product_price_4.visibility = View.VISIBLE

                val productName5 = (activity as MainActivity).productsToBeOrderedInBasket2[4].productName
                val productPrice5 = (activity as MainActivity).productsToBeOrderedInBasket2[4].productPrice
                rootView.summary_fragment_product_name_5.text = productName5
                rootView.summary_fragment_product_price_5.text = "${productPrice5.toInt()*(activity as MainActivity).productsToBeOrderedInBasket2[4].productQuantityToOrder}"
                rootView.summary_fragment_product_name_5.visibility = View.VISIBLE
                rootView.summary_fragment_product_price_5.visibility = View.VISIBLE

                val productName6 = (activity as MainActivity).productsToBeOrderedInBasket2[5].productName
                val productPrice6 = (activity as MainActivity).productsToBeOrderedInBasket2[5].productPrice
                rootView.summary_fragment_product_name_6.text = productName6
                rootView.summary_fragment_product_price_6.text = "${productPrice6.toInt()*(activity as MainActivity).productsToBeOrderedInBasket2[5].productQuantityToOrder}"
                rootView.summary_fragment_product_name_6.visibility = View.VISIBLE
                rootView.summary_fragment_product_price_6.visibility = View.VISIBLE

                val productName7 = (activity as MainActivity).productsToBeOrderedInBasket2[6].productName
                val productPrice7 = (activity as MainActivity).productsToBeOrderedInBasket2[6].productPrice
                rootView.summary_fragment_product_name_7.text = productName7
                rootView.summary_fragment_product_price_7.text = "${productPrice7.toInt()*(activity as MainActivity).productsToBeOrderedInBasket2[6].productQuantityToOrder}"
                rootView.summary_fragment_product_name_7.visibility = View.VISIBLE
                rootView.summary_fragment_product_price_7.visibility = View.VISIBLE

                val productName8 = (activity as MainActivity).productsToBeOrderedInBasket2[7].productName
                val productPrice8 = (activity as MainActivity).productsToBeOrderedInBasket2[7].productPrice
                rootView.summary_fragment_product_name_8.text = productName8
                rootView.summary_fragment_product_price_8.text = "${productPrice8.toInt()*(activity as MainActivity).productsToBeOrderedInBasket2[7].productQuantityToOrder}"
                rootView.summary_fragment_product_name_8.visibility = View.VISIBLE
                rootView.summary_fragment_product_price_8.visibility = View.VISIBLE

                val productName9 = (activity as MainActivity).productsToBeOrderedInBasket2[8].productName
                val productPrice9 = (activity as MainActivity).productsToBeOrderedInBasket2[8].productPrice
                rootView.summary_fragment_product_name_9.text = productName9
                rootView.summary_fragment_product_price_9.text = "${productPrice9.toInt()*(activity as MainActivity).productsToBeOrderedInBasket2[8].productQuantityToOrder}"
                rootView.summary_fragment_product_name_9.visibility = View.VISIBLE
                rootView.summary_fragment_product_price_9.visibility = View.VISIBLE

                val productName10 = (activity as MainActivity).productsToBeOrderedInBasket2[9].productName
                val productPrice10 = (activity as MainActivity).productsToBeOrderedInBasket2[9].productPrice
                rootView.summary_fragment_product_name_10.text = productName10
                rootView.summary_fragment_product_price_10.text = "${productPrice10.toInt()*(activity as MainActivity).productsToBeOrderedInBasket2[9].productQuantityToOrder}"
                rootView.summary_fragment_product_name_10.visibility = View.VISIBLE
                rootView.summary_fragment_product_price_10.visibility = View.VISIBLE

            } catch (e: IndexOutOfBoundsException) {
                Log.d(TAG, "onCreateView: IndexOutOfBoundsException")
            }


        }

        // Back Button listener
        rootView.summary_fragment_back_btn.setOnClickListener{
            (activity as MainActivity).onBackPressed()
        }

            /** Payment Confirm Button listener*/

        /** Payment Confirm Button listener*/
        rootView.summary_fragment_confirm.setOnClickListener {

            hideViews(rootView)
            addProductToBuyingList(rootView)

        }


        return rootView
    }


    /** adding product to cart to order them later by order method*/
    private fun addProductToBuyingList(rootView: View) {

        // Pass the token as parameter
        val call  = apiClient.getApiService().addProductToBuyingList("Bearer ${sessionManager.fetchAccessToken()}",(activity as MainActivity).productsToBeOrderedInBasket)

        call.enqueue(object : Callback<AddProductToBuyListResponse> {

            override fun onResponse(
                call: Call<AddProductToBuyListResponse>,
                response: Response<AddProductToBuyListResponse>
            ) {
                // Handle function to display posts
                Log.e("addProductToBuyingList", "onResponse: ${response.body()}")

                when {
                    response.isSuccessful -> {
                        // Product IDs which have been added to buying list.
                        for (el in response.body()!!.result.indices) {
                            cartIdArray.add(response.body()!!.result[el].id)
                        }
                        Log.e("onResponse", "cartIdArray: $cartIdArray")

                        createOrder(rootView)
                    }
                    response.code() == 500 -> {
                        (activity as MainActivity).customToastTwo("Error code: 500. '/api/v1/productItems/arr'",true)
                        showViews(rootView)
                    }
                    else -> {
                        (activity as MainActivity).customToastTwo("Продукт не был найден",true)
                        showViews(rootView)
                    }

                }

            }

            override fun onFailure(call: Call<AddProductToBuyListResponse>, t: Throwable) {
                showViews(rootView)
                (activity as MainActivity).customToastTwo(getString(R.string.unknown_error),true)
                Log.e("addProductToBuyingList", "onFailure: $t")

            }

        })
    }

    /** Creating order */
    private fun createOrder(rootView: View) {

        val order = OrderRequestBody(shipmentAddressData,shipmentMethodData,desiredDateData,contactName,contactSurname,paymentMethodData,contactPhoneNumber,paymentMethodNumberData)
        // Pass the token as parameter
        val call  = apiClient.getApiService().createOrder("Bearer ${sessionManager.fetchAccessToken()}",order)

        call.enqueue(object : Callback<OrderResponse2> {

            override fun onResponse(call: Call<OrderResponse2>, response: Response<OrderResponse2>) {
                // Handle function to display posts
                Log.e("createOrder", "onResponse: ${response.body()}" )

                if (response.isSuccessful) {

                    //sessionManager.saveOrderDetails("empty", "empty", "empty", "empty", "empty")
                    //sessionManager.saveOrderContacts("empty", "empty", "empty")
                    orderNumber = response.body()!!.result.orderNumber
                    sessionManager.saveOrderNumber(orderNumber)

                    if (paymentMethodData == CASH) {
                        showViews(rootView)
                        navController.navigate(R.id.action_summaryFragment_to_orderConfirmationFragment)
                    } else {
                        getURLForPayment(rootView)
                    }

                } else {

                    // if creating order failed we delete product from buying list
                    showViews(rootView)
                    deleteProductFromBuyingList(rootView)

                }
            }

            override fun onFailure(call: Call<OrderResponse2>, t: Throwable) {
                // Error fetching posts
                (activity as MainActivity).customToastTwo(getString(R.string.unknown_error),true)
                Log.e("createOrder", "onFailure: $t" )
                showViews(rootView)
                deleteProductFromBuyingList(rootView)
            }

        })
    }

    /** Deleting product from cart to avoid errors*/
    private fun deleteProductFromBuyingList(rootView: View) {

        for (el in cartIdArray.indices) {
            val call  = apiClient.getApiService().deleteProductFromBuyingList("Bearer ${sessionManager.fetchAccessToken()}", cartIdArray[el])
            call.enqueue(object : Callback<DeletedServerResponse> {
                override fun onResponse(call: Call<DeletedServerResponse>, response: Response<DeletedServerResponse>) {
                    // Handle function to display posts
                    Log.e("deleteProduct", "onResponse: ${response.body()}")

                    if (response.isSuccessful) {
                        Log.e("deleteProduct", "Product (id = ${cartIdArray[el]}) have been deleted from cart successfully")
                        showViews(rootView)
                    }

                }

                override fun onFailure(call: Call<DeletedServerResponse>, t: Throwable) {
                    Log.e("deleteProduct", "onFailure: $t")
                }
            })
        }

    }

    /** Get Elsom URL For Payment */
    private fun getURLForPayment(rootView: View) {

        Handler(Looper.getMainLooper()).postDelayed({
            val urlRequestBody = UrlRequestBody(paymentMethodNumberData,orderNumber,totalPrice)
            val call  = apiClient.getApiService().getURLForPayment(/*"Bearer ${sessionManager.fetchAccessToken()}",*/ urlRequestBody)
            call.enqueue(object : Callback<GetUrlResponse> {
                override fun onResponse(call: Call<GetUrlResponse>, response: Response<GetUrlResponse>) {
                    // Handle function to display posts
                    Log.e("getURLForPayment", "onResponse: ${response.body()}")

                    if (response.isSuccessful) {
                        Log.e("getURLForPayment", "Success")
                        val bundle = Bundle()
                        bundle.putString("ELSOM_URL",response.body()!!.Response.Result.URL)
                        navController.navigate(R.id.action_summaryFragment_to_orderPaymentFragment,bundle)
                    } else {
                        showViews(rootView)
                        (activity as MainActivity).customToastTwo(getString(R.string.could_not_get_url),true)
                    }

                }

                override fun onFailure(call: Call<GetUrlResponse>, t: Throwable) {
                    Log.e("getURLForPayment", "onFailure: $t")
                    showViews(rootView)
                    (activity as MainActivity).customToastTwo(getString(R.string.could_not_get_url),true)
                }
            })
        }, 200)

    }

    private fun hideViews(rootView: View) {
        rootView.summary_fragment_progressbar.visibility = View.VISIBLE
        rootView.summary_fragment_product_make_invisible.visibility = View.GONE
        rootView.summary_fragment_product_make_invisible_2.visibility = View.GONE
        rootView.summary_fragment_product_make_invisible_3.visibility = View.GONE
        rootView.summary_fragment_product_make_invisible_4.visibility = View.GONE
        rootView.summary_fragment_product_make_invisible_5.visibility = View.GONE
        rootView.summary_fragment_confirm.visibility = View.GONE
        rootView.summary_fragment_product_name_1.visibility = View.GONE
        rootView.summary_fragment_product_name_2.visibility = View.GONE
        rootView.summary_fragment_product_name_3.visibility = View.GONE
        rootView.summary_fragment_product_name_4.visibility = View.GONE
        rootView.summary_fragment_product_name_5.visibility = View.GONE
        rootView.summary_fragment_product_name_6.visibility = View.GONE
        rootView.summary_fragment_product_name_7.visibility = View.GONE
        rootView.summary_fragment_product_name_8.visibility = View.GONE
        rootView.summary_fragment_product_name_9.visibility = View.GONE
        rootView.summary_fragment_product_name_10.visibility = View.GONE
        rootView.summary_fragment_product_price_1.visibility = View.GONE
        rootView.summary_fragment_product_price_2.visibility = View.GONE
        rootView.summary_fragment_product_price_3.visibility = View.GONE
        rootView.summary_fragment_product_price_4.visibility = View.GONE
        rootView.summary_fragment_product_price_5.visibility = View.GONE
        rootView.summary_fragment_product_price_6.visibility = View.GONE
        rootView.summary_fragment_product_price_7.visibility = View.GONE
        rootView.summary_fragment_product_price_8.visibility = View.GONE
        rootView.summary_fragment_product_price_9.visibility = View.GONE
        rootView.summary_fragment_product_price_10.visibility = View.GONE
    }

    private fun showViews(rootView: View) {
        rootView.summary_fragment_progressbar.visibility = View.GONE
        rootView.summary_fragment_product_make_invisible.visibility = View.VISIBLE
        rootView.summary_fragment_product_make_invisible_2.visibility = View.VISIBLE
        rootView.summary_fragment_product_make_invisible_3.visibility = View.VISIBLE
        rootView.summary_fragment_product_make_invisible_4.visibility = View.VISIBLE
        rootView.summary_fragment_product_make_invisible_5.visibility = View.VISIBLE
        rootView.summary_fragment_confirm.visibility = View.VISIBLE

        try {
            val productName2 = (activity as MainActivity).productsToBeOrderedInBasket2[1].productName
            rootView.summary_fragment_product_name_2.visibility = View.VISIBLE
            rootView.summary_fragment_product_price_2.visibility = View.VISIBLE

            val productName3 = (activity as MainActivity).productsToBeOrderedInBasket2[2].productName
            rootView.summary_fragment_product_name_3.visibility = View.VISIBLE
            rootView.summary_fragment_product_price_3.visibility = View.VISIBLE

            val productName4 = (activity as MainActivity).productsToBeOrderedInBasket2[3].productName
            rootView.summary_fragment_product_name_4.visibility = View.VISIBLE
            rootView.summary_fragment_product_price_4.visibility = View.VISIBLE

            val productName5 = (activity as MainActivity).productsToBeOrderedInBasket2[4].productName
            rootView.summary_fragment_product_name_5.visibility = View.VISIBLE
            rootView.summary_fragment_product_price_5.visibility = View.VISIBLE

            val productName6 = (activity as MainActivity).productsToBeOrderedInBasket2[5].productName
            rootView.summary_fragment_product_name_6.visibility = View.VISIBLE
            rootView.summary_fragment_product_price_6.visibility = View.VISIBLE

            val productName7 = (activity as MainActivity).productsToBeOrderedInBasket2[6].productName
            rootView.summary_fragment_product_name_7.visibility = View.VISIBLE
            rootView.summary_fragment_product_price_7.visibility = View.VISIBLE

            val productName8 = (activity as MainActivity).productsToBeOrderedInBasket2[7].productName
            rootView.summary_fragment_product_name_8.visibility = View.VISIBLE
            rootView.summary_fragment_product_price_8.visibility = View.VISIBLE

            val productName9 = (activity as MainActivity).productsToBeOrderedInBasket2[8].productName
            rootView.summary_fragment_product_name_9.visibility = View.VISIBLE
            rootView.summary_fragment_product_price_9.visibility = View.VISIBLE

            val productName10 = (activity as MainActivity).productsToBeOrderedInBasket2[9].productName
            rootView.summary_fragment_product_name_10.visibility = View.VISIBLE
            rootView.summary_fragment_product_price_10.visibility = View.VISIBLE

            Log.d(TAG, "showViews: $productName2 $productName3 $productName4 $productName4 " +
                    "$productName5 $productName6 $productName7 $productName9 $productName10")

        } catch (e: IndexOutOfBoundsException) {
            Log.d(TAG, "onCreateView: IndexOutOfBoundsException")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }

}