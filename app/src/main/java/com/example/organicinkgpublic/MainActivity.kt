package com.example.organicinkgpublic

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.organicinkgpublic.database.BasketViewModel
import com.example.organicinkgpublic.model.ProductEntityExample
import com.example.organicinkgpublic.model.network.auth.RefreshRequestBody
import com.example.organicinkgpublic.model.network.cart.ProductItemRequestBody
import com.example.organicinkgpublic.model.network.refreshtoken.RefreshTokenResponse
import com.example.organicinkgpublic.retrofit.ApiClient
import com.example.organicinkgpublic.retrofit.SessionManager
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    private lateinit var sessionManager: SessionManager
    private lateinit var basketViewModel: BasketViewModel

    // Vars
    val productsIdInBasket = ArrayList<Int>()
    val productsToBeOrderedInBasket = ArrayList<ProductItemRequestBody>()
    val productsToBeOrderedInBasket2 = ArrayList<ProductEntityExample>()
    var totalPrice = 0.0f
    var isVisibleBNB = true
    var isVisibleCustomToast = false
    var isVisibleBasketToast = false
    var isFirstLaunching = true

    var isDifferentCurrenciesSelected = false
    var currencyString = ""
    var isDifferentCurrenciesSelectedHelper = false

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sessionManager = SessionManager(this)


        /** Status Bar icon color*/
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        val window: Window = this.window
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
            // todo test this
           // window.statusBarColor = Color.BLACK
        }


        /** To observe product id in basket DB*/
        basketViewModel = ViewModelProvider(this).get(BasketViewModel::class.java)
        basketViewModel.allProducts.observe(this, Observer { words ->
            words?.let {
                productsIdInBasket.clear()
                productsToBeOrderedInBasket.clear()
                productsToBeOrderedInBasket2.clear()
                totalPrice = 0.0f

                for (el in words.indices) {

                    productsIdInBasket.add(words[el].productId)

                    if (words[el].isCheckedToBeOrdered == 1) {
                        productsToBeOrderedInBasket.add(
                            ProductItemRequestBody(
                                words[el].productQuantityToOrder,
                                words[el].productId
                            )
                        )
                        productsToBeOrderedInBasket2.add(ProductEntityExample(words[el].productId,words[el].productName,words[el].productPrice,words[el].productCurrency,words[el].productQuantityToOrder))

                        totalPrice += (words[el].productPrice * words[el].productQuantityToOrder)

                        // To know if different currencies selected
                        if (!isDifferentCurrenciesSelectedHelper) {
                            currencyString = words[el].productCurrency
                            Log.e(TAG, "onCreate: $currencyString")
                            isDifferentCurrenciesSelectedHelper = true
                            isDifferentCurrenciesSelected = false
                        } else if (currencyString != words[el].productCurrency) {
                            isDifferentCurrenciesSelected = true
                        }

                        //Log.e(TAG, "onCreate: $isDifferentCurrenciesSelectedHelper $currencyString $isDifferentCurrenciesSelected")

                    }

                }

                isDifferentCurrenciesSelectedHelper = false


                calculateTotalPrice()
                Log.e(
                    TAG,
                    "productsIdInBasket: $productsIdInBasket, productsToBeOrderedInBasket: $productsToBeOrderedInBasket totalPrice $totalPrice"
                )

            }
        })

        /** Setting BNB navigation*/
        val navController = findNavController(R.id.fragment)
        bottom_navigation_bar.setupWithNavController(navController)

        showBottomNavBar("",false)


        /** Refresh AccessToken */
        if (sessionManager.fetchAccessTokenExpTime()!! != "null") {
            val expirationDate = sessionManager.fetchAccessTokenExpTime()!!.substring(0,10)
            val expirationDateTimestamp = SimpleDateFormat("yyyy-MM-dd").parse(expirationDate)!!.time

            if (System.currentTimeMillis() > expirationDateTimestamp) {
                refreshTokens()
                Log.e(TAG, "MainActivity - Refreshing tokens: AccessToken: ${sessionManager.fetchAccessToken()}, tokenExpirationTime: ${sessionManager.fetchAccessTokenExpTime()}, , phoneNumber : ${sessionManager.fetchPhoneNumber()}" )
            }

        }


    }


    /** onBackPressed */
    override fun onBackPressed() {
       /* if (!isVisibleBNB) {
            showBottomNavBar("",false)
        }*/
        super.onBackPressed()
    }

    /** To show bottomNavBar */
    internal fun showBottomNavBar(message: String,redAlert: Boolean) {

        if(!isVisibleBNB) {
            isVisibleBNB = true
            val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up)
            bottom_navigation_bar.visibility = View.VISIBLE
            bottom_navigation_bar.startAnimation(slideUp)
        }

        //isVisibleBNB = true

        Handler(Looper.getMainLooper()).postDelayed({
            if (message != "") {
                customToast(message,redAlert)
            }
        }, 1000)

    }

    /** To hide bottomNavBar */
    internal fun hideBottomNavBar() {

        if(isVisibleBNB) {
            isVisibleBNB = false
            val slideDown = AnimationUtils.loadAnimation(applicationContext, R.anim.slide_down_2)
            bottom_navigation_bar.visibility = View.GONE
            bottom_navigation_bar.startAnimation(slideDown)
        }

    }

    /**
     * To invoke a custom toast message from any fragment
     */
    internal fun customToast(message: String, isRedAlert: Boolean) {

       if (!isVisibleCustomToast) {
           isVisibleCustomToast = true

           val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up)
           val slideDown = AnimationUtils.loadAnimation(applicationContext, R.anim.slide_down_2)
           val greenColor = "#007600"
           val redColor = "#FF0303"

           if (isRedAlert) {
               main_activity_text_message.setBackgroundColor(Color.parseColor(redColor))
           } else {
               main_activity_text_message.setBackgroundColor(Color.parseColor(greenColor))
           }

           main_activity_text_message.text = message
           main_activity_text_message.visibility = View.VISIBLE
           main_activity_text_message.startAnimation(slideUp)
           main_activity_text_message.setPadding(
               15,
               15,
               15,
               bottom_navigation_bar.measuredHeight + 15
           )

           // Thread sleep
           Handler(Looper.getMainLooper()).postDelayed({
               main_activity_text_message.visibility = View.GONE
               main_activity_text_message.startAnimation(slideDown)
               isVisibleCustomToast = false
           }, 3000)
       }
    }

    /**
     * To invoke a custom toast message from any fragment
     */
    internal fun customToastTwo(message: String, isRedAlert: Boolean) {
        if (!isVisibleCustomToast) {
            isVisibleCustomToast = true

            val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up)
            val slideDown = AnimationUtils.loadAnimation(applicationContext, R.anim.slide_down_2)
            val greenColor = "#007600"
            val redColor = "#FF0303"

            if (isRedAlert) {
                main_activity_text_message.setBackgroundColor(Color.parseColor(redColor))
            } else {
                main_activity_text_message.setBackgroundColor(Color.parseColor(greenColor))
            }

            main_activity_text_message.text = message
            main_activity_text_message.visibility = View.VISIBLE
            main_activity_text_message.startAnimation(slideUp)
            main_activity_text_message.setPadding(
                15,
                15,
                15, /*bottom_navigation_bar.measuredHeight + */
                15
            )

            // Thread sleep
            Handler(Looper.getMainLooper()).postDelayed({
                main_activity_text_message.visibility = View.GONE
                main_activity_text_message.startAnimation(slideDown)
                isVisibleCustomToast = false

            }, 3000)
        }
    }

    /** To show BasketToast */
    internal fun showBasketToast(message: String, isRedAlert: Boolean) {

        if (!isVisibleBasketToast) {
            isVisibleBasketToast = true

            val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up)
            val greenColor = "#007600"
            val redColor = "#FF0303"

            if (isRedAlert) {
                main_activity_basket_text_message.setBackgroundColor(Color.parseColor(redColor))
            } else {
                main_activity_basket_text_message.setBackgroundColor(Color.parseColor(greenColor))
            }

            main_activity_basket_text_message.visibility = View.VISIBLE
            main_activity_basket_text_message.text = message
            main_activity_basket_text_message.startAnimation(slideUp)
            main_activity_basket_text_message.setPadding(15, 15, 15, bottom_navigation_bar.measuredHeight + 15)

        } else {
            main_activity_basket_text_message.text = message
        }

    }
    /** To hide BasketToast */
    internal fun hideBasketToast() {
        if (isVisibleBasketToast) {
            isVisibleBasketToast = false
            val slideDown = AnimationUtils.loadAnimation(applicationContext, R.anim.slide_down_2)
            main_activity_basket_text_message.visibility = View.GONE
            main_activity_basket_text_message.startAnimation(slideDown)
        }
    }

    /** To hide BasketToastTwo */
    internal fun hideBasketToastTwo() {
        val slideDown = AnimationUtils.loadAnimation(applicationContext, R.anim.slide_down_2)
        isVisibleBasketToast = false
        main_activity_basket_text_message.visibility = View.GONE
        //main_activity_basket_text_message.setPadding(15, 15, 15, 15)
        //main_activity_basket_text_message.startAnimation(slideDown)
    }

    /** Calculating total price */
    fun calculateTotalPrice() {

        if (isFirstLaunching) {

            isFirstLaunching = false

        } else {

            if (isDifferentCurrenciesSelected) {

                val message = getString(R.string.with_same_currency)

                showBasketToast(message, false)


                Handler(Looper.getMainLooper()).postDelayed({
                    if(isVisibleBasketToast) {
                        hideBasketToast()
                    }
                }, 3000)

            } else {

                val message = "Сумма заказа: ${totalPrice.toInt()} $currencyString. Выбранно товаров: ${productsToBeOrderedInBasket.size}."

                if (totalPrice != 0.0f && productsToBeOrderedInBasket.size > 0) {
                    showBasketToast(message, false)
                } else {
                    hideBasketToast()
                }
            }

        }

    }

    private fun refreshTokens() {

        val apiClient = ApiClient()

        val body = RefreshRequestBody(sessionManager.fetchAccessToken()!!,sessionManager.fetchRefreshToken()!!,sessionManager.fetchPhoneNumber()!!)

        val call  = apiClient.getApiService().refreshTokens(body)

        call.enqueue(object : Callback<RefreshTokenResponse> {

            override fun onResponse(
                call: Call<RefreshTokenResponse>,
                response: Response<RefreshTokenResponse>
            ) {


                if (response.isSuccessful && response.body()?.resultCode!! == "SUCCESS") {
                    // Saving refreshed tokens to prefs
                    sessionManager.saveAuthTokens(response.body()?.result?.body?.id!!,
                    response.body()?.result?.body?.accessToken!!,
                    response.body()?.result?.body?.tokenExpirationTime!!,
                    response.body()?.result?.body?.refreshToken!!,
                    response.body()?.result?.body?.refreshExpirationTime!!)

                    Log.d(TAG, "onResponse: tokens refreshed successfully")

                } else {

                    Log.d(TAG, "onResponse: ${response.body()}")

                }
            }

            override fun onFailure(call: Call<RefreshTokenResponse>, t: Throwable) {
                Log.e(TAG, "onResponse: $t}")
            }

        })
    }

}