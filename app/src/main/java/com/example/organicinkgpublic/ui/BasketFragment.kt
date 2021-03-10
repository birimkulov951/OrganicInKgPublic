package com.example.organicinkgpublic.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.organicinkgpublic.MainActivity
import com.example.organicinkgpublic.R
import com.example.organicinkgpublic.adapters.BasketAdapter
import com.example.organicinkgpublic.database.BasketViewModel
import com.example.organicinkgpublic.database.basket.ProductEntity
import com.example.organicinkgpublic.retrofit.ApiClient
import com.example.organicinkgpublic.retrofit.SessionManager
import com.example.organicinkgpublic.utils.Constants
import kotlinx.android.synthetic.main.exit_alert_dialog.view.*
import kotlinx.android.synthetic.main.fragment_basket.view.*
import java.util.ArrayList

class BasketFragment : Fragment(), BasketAdapter.OnItemClickListener {

    private val TAG = "BasketFragment"

    private lateinit var basketViewModel: BasketViewModel
    private lateinit var navController: NavController
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient

    private lateinit var basketAdapter: BasketAdapter

    // Helper vars
    private var productId = -1
    private var basketDBSize: Int = 0
    private var isSelectorClicked = false

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val rootView: View = inflater.inflate(R.layout.fragment_basket, container, false)
        (activity as MainActivity).showBottomNavBar("", false)

        apiClient = ApiClient()
        sessionManager = SessionManager(requireContext())

        sessionManager.saveOrderDetails("empty", "empty", "empty", "empty", "empty")
        sessionManager.saveOrderContacts("empty", "empty", "empty")

        (activity as MainActivity).calculateTotalPrice()


        // getProduct id to show right item in recycler view
        productId = requireArguments().getInt(Constants.PRODUCT_ID, -1)


        basketAdapter = BasketAdapter(this, requireContext())

        // Handling RecycleView and it's animation
        val lac: LayoutAnimationController = AnimationUtils.loadLayoutAnimation(
            activity,
            R.anim.layout_fall_down
        )

        rootView.basket_recyclerview.adapter = basketAdapter
        // Categories RecycleView has fixed size. LayoutManager is written in XML file
        rootView.basket_recyclerview.layoutAnimation = lac
        rootView.basket_recyclerview.startLayoutAnimation()
        // For the better scrolling of fragment
        rootView.basket_recyclerview.isNestedScrollingEnabled = false


        // Get a new or existing ViewModel from the ViewModelProvider.
        basketViewModel = ViewModelProvider(this).get(BasketViewModel::class.java)
        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is in the foreground.
        basketViewModel.allProducts.observe(requireActivity(), Observer { words ->
            // Update the cached copy of the words in the adapter.
            words?.let { it ->
                // Refreshing recyclerView after database modifications
                basketAdapter.setBasketProducts(it)
                basketAdapter.notifyDataSetChanged()

                // Database size
                basketDBSize = words.size

                Log.e(TAG, "onCreateView: ${words.size}")
                Log.e(TAG, "onCreateView: $words")
                rootView.basket_product_counter.text = " ($basketDBSize)"

                // Calculating and showing total price
                //calculateTotalPrice()

                if (basketDBSize == 0) {
                    rootView.basket_is_empty_image.visibility = View.VISIBLE
                    rootView.basket_is_empty_text.visibility = View.VISIBLE
                    rootView.basket_is_empty_button.visibility = View.VISIBLE
                    rootView.basket_product_buy_button.visibility = View.GONE
                    rootView.basket_product_counter.visibility = View.GONE

                } else {
                    rootView.basket_is_empty_image.visibility = View.GONE
                    rootView.basket_is_empty_text.visibility = View.GONE
                    rootView.basket_is_empty_button.visibility = View.GONE
                    rootView.basket_product_buy_button.visibility = View.VISIBLE
                    rootView.basket_product_counter.visibility = View.VISIBLE
                }

            }
        })

        // to add dummy data to db
        //rootView.basket_product_selector.visibility = View.GONE
        rootView.basket_product_selector.setOnClickListener{
            /** Adds all products to Basket DB to buy */

            if (!isSelectorClicked) {
                isSelectorClicked = true
                for (item in basketViewModel.allProducts.value!!.indices) {

                    basketViewModel.allProducts.value!![item].isCheckedToBeOrdered = 1

                    basketViewModel.updateProduct(basketViewModel.allProducts.value!![item])
                }
            } else {
                isSelectorClicked = false

                for (item in basketViewModel.allProducts.value!!.indices) {

                    basketViewModel.allProducts.value!![item].isCheckedToBeOrdered = 0

                    basketViewModel.updateProduct(basketViewModel.allProducts.value!![item])
                }
            }

        }

        // Opens HomeFragment when user leaves basket
        rootView.basket_is_empty_button.setOnClickListener{

            /** Opens main Fragment */
            navController.navigate(R.id.action_basketFragment_to_homeFragment)

        }

        // Opens OrderFragment after order confirmation button pressed
        rootView.basket_product_buy_button.setOnClickListener{

            if (sessionManager.fetchAccessToken()!! == "null") {

                /*(activity as MainActivity).customToast(
                    getString(R.string.please_login_to_order),
                    true
                )*/

                //Handler(Looper.getMainLooper()).postDelayed({
                    val alertDialog = LayoutInflater.from(rootView.context).inflate(R.layout.exit_alert_dialog, null)
                    val builder = AlertDialog.Builder(rootView.context)
                        .setView(alertDialog)
                        .show()
                    alertDialog.text_view_want_to_exit.text = getString(R.string.authorize_to_able_to_order)
                    alertDialog.exit_button.text = getString(R.string.go_to)
                    alertDialog.stay_button.setOnClickListener {
                        builder.dismiss()
                    }
                    alertDialog.exit_button.setOnClickListener {
                        navController.navigate(R.id.action_basketFragment_to_registrationFragment)
                        builder.dismiss()
                    }
                //}, 1000)


            }else if ((activity as MainActivity).productsToBeOrderedInBasket.size == 0) {

                (activity as MainActivity).customToast(
                    getString(R.string.did_not_select_product),
                    true
                )

            } else if ((activity as MainActivity).productsToBeOrderedInBasket.size > 10) {

                (activity as MainActivity).customToast(
                    getString(R.string.can_not_10_products_at_the_same_time),
                    true
                )

            } else {

                // todo clean SELECTED ORDERS TO BUY WHEN BUYING ONLY 1 PRODUCT
                /** To continue for payment */
                navController.navigate(R.id.action_basketFragment_to_orderFragment)

            }

        }


        return rootView

    }

    // On RecyclerView Item Clicked
    override fun onItemClick(v: View?, position: Int) {

        val sender = Bundle()
        val clickedItem: ProductEntity = basketAdapter.getProductAt(position)

        val arrayListOfImages : ArrayList<String>?
        val listOfImages: List<String> = clickedItem.productImage.split("&")
        arrayListOfImages = listOfImages.toCollection(ArrayList())

        sender.putInt(Constants.PRODUCT_ID, clickedItem.productId)
        sender.putString(Constants.PRODUCT_NAME, clickedItem.productName)
        sender.putString(Constants.PRODUCT_PRODUCTION_PLACE, clickedItem.productProductionPlace)
        if (clickedItem.productRating == null) {
            sender.putFloat(Constants.PRODUCT_RATING, -1.0f)
        } else {
            sender.putFloat(Constants.PRODUCT_RATING, clickedItem.productRating!!)
        }
        sender.putStringArrayList(Constants.PRODUCT_IMAGES, arrayListOfImages)
        sender.putFloat(Constants.PRODUCT_PRICE, clickedItem.productPrice)
        //sender.putString(PRODUCT_BOUGHT_QUANTITY, clickedItem.)
        sender.putInt(Constants.PRODUCT_MINIMUM_ORDER_QUANTITY, clickedItem.productMinimumOrderQuantity)
        sender.putString(Constants.PRODUCT_DESCRIPTION, clickedItem.productDescription)
        //sender.putBoolean(Constants.PRODUCT_IS_IN_BASKET, (activity as MainActivity).productsIdInBasket.contains(clickedItem.id))
        sender.putBoolean(Constants.PRODUCT_IS_IN_BASKET, true)

        //Toast.makeText(context, "Open info about this product. Clicked item position: $position", Toast.LENGTH_SHORT).show()
        navController.navigate(R.id.action_basketFragment_to_singleProductFragment,sender)

    }

    override fun onDeleteClick(v: View?, position: Int) {

        // Custom Alert Dialog
        val alertDialog = LayoutInflater.from(context).inflate(R.layout.exit_alert_dialog, null)
        val alertTitle = alertDialog.findViewById<TextView>(R.id.text_view_want_to_exit)

        alertDialog.stay_button.text = getString(R.string.cancel)
        alertDialog.exit_button.text = getString(R.string.delete)

        alertTitle.text = getString(R.string.sure_to_delete_product)

        val builder = AlertDialog.Builder(context)
            .setView(alertDialog)
            .show()

        alertDialog.stay_button.setOnClickListener {
            builder.dismiss()
        }
        alertDialog.exit_button.setOnClickListener {
            builder.dismiss()
            val clickedItem: ProductEntity = basketAdapter.getProductAt(position)
            basketViewModel.deleteProduct(clickedItem)
        }

    }

    override fun onPlusClick(v: View?, position: Int) {
        val clickedItem: ProductEntity = basketAdapter.getProductAt(position)

        clickedItem.productQuantityToOrder = clickedItem.productQuantityToOrder + 1
        basketViewModel.updateProduct(clickedItem)

    }

    override fun onMinusClick(v: View?, position: Int) {
        val clickedItem: ProductEntity = basketAdapter.getProductAt(position)

        if (clickedItem.productQuantityToOrder > clickedItem.productMinimumOrderQuantity) {
            clickedItem.productQuantityToOrder = clickedItem.productQuantityToOrder - 1
            basketViewModel.updateProduct(clickedItem)
        }

    }

    override fun onCheckBoxClick(v: View?, position: Int) {

        // Thread sleep
        Handler(Looper.getMainLooper()).postDelayed({

            val clickedItem: ProductEntity = basketAdapter.getProductAt(position)

            if(clickedItem.isCheckedToBeOrdered == 0) {
                clickedItem.isCheckedToBeOrdered = 1
            } else {
                clickedItem.isCheckedToBeOrdered = 0
            }

            //Log.e(TAG, "onCheckBoxClick: $position")

            basketViewModel.updateProduct(clickedItem)

        }, 400)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }

}