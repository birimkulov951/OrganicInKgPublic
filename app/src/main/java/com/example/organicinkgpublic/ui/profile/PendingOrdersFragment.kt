package com.example.organicinkgpublic.ui.profile

import android.graphics.Color
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.organicinkgpublic.MainActivity
import com.example.organicinkgpublic.R
import com.example.organicinkgpublic.adapters.OrdersAdapter
import com.example.organicinkgpublic.model.network.orders.closedorders.ClosedOrdersResponse
import com.example.organicinkgpublic.model.ClosedOrder
import com.example.organicinkgpublic.retrofit.ApiClient
import com.example.organicinkgpublic.retrofit.SessionManager
import kotlinx.android.synthetic.main.fragment_pending_orders.*
import kotlinx.android.synthetic.main.fragment_pending_orders.view.*
import kotlinx.android.synthetic.main.order_list_item.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PendingOrdersFragment: Fragment(), OrdersAdapter.OnItemClickListener {

    private val TAG = "PendingOrdersFragment"

    lateinit var navController: NavController
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient
    private lateinit var ordersAdapter: OrdersAdapter

    private var pendingOrders = ArrayList<ClosedOrder>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_pending_orders, container, false)

        apiClient = ApiClient()
        sessionManager = SessionManager(requireContext())

        ordersAdapter = OrdersAdapter(this@PendingOrdersFragment, requireContext())

        rootView.current_orders_filled_back_button.setOnClickListener{
            activity?.onBackPressed()
        }

        rootView.current_orders_is_empty_button.setOnClickListener{
            /** Opens main Fragment */
            navController.navigate(R.id.action_currentOrdersFilledFragment_to_homeFragment)
        }

        if(sessionManager.fetchAccessToken() != "null") {
            getUserPendingOrders(rootView)
        } else {
            rootView.current_orders_is_empty_text.text = getString(R.string.you_are_not_logged_in)
            rootView.current_orders_is_empty_image.visibility = View.VISIBLE
            rootView.current_orders_is_empty_text.visibility = View.VISIBLE
            rootView.current_orders_is_empty_button.visibility = View.VISIBLE
        }



        return rootView
    }



    private fun getUserPendingOrders(rootView: View) {
        rootView.current_orders_fragment_progressbar.visibility = View.VISIBLE

        // Pass the token as parameter
        val call  = apiClient.getApiService().getUserPendingOrders("Bearer ${sessionManager.fetchAccessToken()}")

        call.enqueue(object : Callback<ClosedOrdersResponse> {

            override fun onResponse(
                call: Call<ClosedOrdersResponse>,
                response: Response<ClosedOrdersResponse>
            ) {
                // Handle function to display posts
                Log.e(TAG, "onResponse: ${response.body()}")

                if (response.isSuccessful) {
                    rootView.current_orders_fragment_progressbar.visibility = View.INVISIBLE
                    if (response.body()!!.result!!.isEmpty()) {

                        rootView.current_orders_is_empty_image.visibility = View.VISIBLE
                        rootView.current_orders_is_empty_text.visibility = View.VISIBLE
                        rootView.current_orders_is_empty_button.visibility = View.VISIBLE

                    } /*else if (response.body()!!.result!!.isEmpty()) {

                    } */ else {
                        pendingOrders.clear()
                        for (i in response.body()!!.result!!.indices) {
                            pendingOrders.add(
                                ClosedOrder(
                                    response.body()!!.result!![i].orderNumber,
                                    "null",
                                    null, null, null, "null", "null",
                                    null, null, null, "null", "null",
                                    null, null, null, "null", "null",
                                    null, null, null, "null", "null",
                                    null, null, null, "null", "null",
                                    null, null, null, "null", "null",
                                    null, null, null, "null", "null",
                                    null, null, null, "null", "null",
                                    null, null, null, "null", "null",
                                    null, null, null, "null", "null",
                                    false
                                )
                            )
                            for (product in response.body()!!.result!![i].products!!.indices) {
                                try {
                                    if (response.body()!!.result!![i].products!![0].product!!.currency != null) {
                                        pendingOrders[i].productCurrency = response.body()!!.result!![i].products!![0].product!!.currency!!
                                    } else {
                                        pendingOrders[i].productCurrency = getString(R.string.not_defined)
                                    }
                                    when {
                                        pendingOrders[i].product1Name == null -> {
                                            pendingOrders[i].product1Name =
                                                response.body()!!.result!![i].products!![0].product!!.name!!
                                            pendingOrders[i].product1Price =
                                                response.body()!!.result!![i].products!![0].totalPrice.toString()
                                            pendingOrders[i].product1ImageUrl =
                                                response.body()!!.result!![i].products!![0].product!!.productImages!![0].imageUrl!!
                                            pendingOrders[i].product1Location =
                                                response.body()!!.result!![i].products!![0].product!!.supplier!!.placeOfProduction!!.region
                                        }
                                        pendingOrders[i].product2Name == null -> {
                                            pendingOrders[i].product2Name =
                                                response.body()!!.result!![i].products!![1].product!!.name!!
                                            pendingOrders[i].product2Price =
                                                response.body()!!.result!![i].products!![1].totalPrice.toString()
                                            pendingOrders[i].product2ImageUrl =
                                                response.body()!!.result!![i].products!![1].product!!.productImages!![0].imageUrl!!
                                            pendingOrders[i].product2Location =
                                                response.body()!!.result!![i].products!![1].product!!.supplier!!.placeOfProduction!!.region
                                        }
                                        pendingOrders[i].product3Name == null -> {
                                            pendingOrders[i].product3Name =
                                                response.body()!!.result!![i].products!![2].product!!.name!!
                                            pendingOrders[i].product3Price =
                                                response.body()!!.result!![i].products!![2].totalPrice.toString()
                                            pendingOrders[i].product3ImageUrl =
                                                response.body()!!.result!![i].products!![2].product!!.productImages!![0].imageUrl!!
                                            pendingOrders[i].product3Location =
                                                response.body()!!.result!![i].products!![2].product!!.supplier!!.placeOfProduction!!.region
                                        }
                                        pendingOrders[i].product4Name == null -> {
                                            pendingOrders[i].product4Name =
                                                response.body()!!.result!![i].products!![3].product!!.name!!
                                            pendingOrders[i].product4Price =
                                                response.body()!!.result!![i].products!![3].totalPrice.toString()
                                            pendingOrders[i].product4ImageUrl =
                                                response.body()!!.result!![i].products!![3].product!!.productImages!![0].imageUrl!!
                                            pendingOrders[i].product4Location =
                                                response.body()!!.result!![i].products!![3].product!!.supplier!!.placeOfProduction!!.region
                                        }
                                        pendingOrders[i].product5Name == null -> {
                                            pendingOrders[i].product5Name =
                                                response.body()!!.result!![i].products!![4].product!!.name!!
                                            pendingOrders[i].product5Price =
                                                response.body()!!.result!![i].products!![4].totalPrice.toString()
                                            pendingOrders[i].product5ImageUrl =
                                                response.body()!!.result!![i].products!![4].product!!.productImages!![0].imageUrl!!
                                            pendingOrders[i].product5Location =
                                                response.body()!!.result!![i].products!![4].product!!.supplier!!.placeOfProduction!!.region
                                        }
                                        pendingOrders[i].product6Name == null -> {
                                            pendingOrders[i].product6Name =
                                                response.body()!!.result!![i].products!![5].product!!.name!!
                                            pendingOrders[i].product6Price =
                                                response.body()!!.result!![i].products!![5].totalPrice.toString()
                                            pendingOrders[i].product6ImageUrl =
                                                response.body()!!.result!![i].products!![5].product!!.productImages!![0].imageUrl!!
                                            pendingOrders[i].product6Location =
                                                response.body()!!.result!![i].products!![5].product!!.supplier!!.placeOfProduction!!.region
                                        }
                                        pendingOrders[i].product7Name == null -> {
                                            pendingOrders[i].product7Name =
                                                response.body()!!.result!![i].products!![6].product!!.name!!
                                            pendingOrders[i].product7Price =
                                                response.body()!!.result!![i].products!![6].totalPrice.toString()
                                            pendingOrders[i].product7ImageUrl =
                                                response.body()!!.result!![i].products!![6].product!!.productImages!![0].imageUrl!!
                                            pendingOrders[i].product7Location =
                                                response.body()!!.result!![i].products!![6].product!!.supplier!!.placeOfProduction!!.region
                                        }
                                        pendingOrders[i].product8Name == null -> {
                                            pendingOrders[i].product8Name =
                                                response.body()!!.result!![i].products!![7].product!!.name!!
                                            pendingOrders[i].product8Price =
                                                response.body()!!.result!![i].products!![7].product!!.price!!.toString()
                                            pendingOrders[i].product8ImageUrl =
                                                response.body()!!.result!![i].products!![7].product!!.productImages!![0].imageUrl!!
                                            pendingOrders[i].product8Location =
                                                response.body()!!.result!![i].products!![7].product!!.supplier!!.placeOfProduction!!.region
                                        }
                                        pendingOrders[i].product9Name == null -> {
                                            pendingOrders[i].product9Name =
                                                response.body()!!.result!![i].products!![8].product!!.name!!
                                            pendingOrders[i].product9Price =
                                                response.body()!!.result!![i].products!![8].totalPrice.toString()
                                            pendingOrders[i].product9ImageUrl =
                                                response.body()!!.result!![i].products!![8].product!!.productImages!![0].imageUrl!!
                                            pendingOrders[i].product9Location =
                                                response.body()!!.result!![i].products!![8].product!!.supplier!!.placeOfProduction!!.region
                                        }
                                        pendingOrders[i].product10Name == null -> {
                                            pendingOrders[i].product10Name =
                                                response.body()!!.result!![i].products!![9].product!!.name!!
                                            pendingOrders[i].product10Price =
                                                response.body()!!.result!![i].products!![9].totalPrice.toString()
                                            pendingOrders[i].product10ImageUrl =
                                                response.body()!!.result!![i].products!![9].product!!.productImages!![0].imageUrl!!
                                            pendingOrders[i].product10Location =
                                                response.body()!!.result!![i].products!![9].product!!.supplier!!.placeOfProduction!!.region
                                        }
                                    }

                                } catch (e: IndexOutOfBoundsException) {
                                    Log.e(TAG, "onResponse: ${response.body()!!.result!![i].products!![product].product!!.id} has no image!")
                                } catch (e: KotlinNullPointerException) {
                                    Log.e(TAG, "KotlinNullPointerException: products have been deleted")
                                    (activity as MainActivity).customToast(getString(R.string.some_products_have_been_deleted),true)
                                }

                            }
                        }

                        /*val lac: LayoutAnimationController = AnimationUtils.loadLayoutAnimation(activity, R.anim.layout_fall_down)
                        rootView.current_orders_recycler_view.layoutAnimation = lac*/
                        rootView.current_orders_recycler_view.adapter = ordersAdapter
                        ordersAdapter.setItems(pendingOrders)
                        rootView.current_orders_recycler_view.startLayoutAnimation()

                    }
                } else {
                    (activity as MainActivity).customToast(getString(R.string.unknown_error), true)
                }
            }

            override fun onFailure(call: Call<ClosedOrdersResponse>, t: Throwable) {
                (activity as MainActivity).customToast(getString(R.string.unknown_error), true)

                Log.e(TAG, "onFailure: $t")
            }

        })
    }
    


    override fun onItemClick(position: Int, v: View) {
        val clickedItem: ClosedOrder = pendingOrders[position]

        val cardViewInvisible = v.findViewById<CardView>(R.id.current_orders_product_card_view_invisible)
        val orderNumber = v.findViewById<TextView>(R.id.current_orders_order_number_text)
        val orderNumberSymbol = v.findViewById<TextView>(R.id.order_id_symbol)

        val arrowIcon = v.findViewById<ImageView>(R.id.current_orders_filled_arrow)

        val order1  = v.findViewById<LinearLayout>(R.id.current_orders_invisible_layout_1)
        val order2 = v.findViewById<LinearLayout>(R.id.current_orders_invisible_layout_2)
        val order3 = v.findViewById<LinearLayout>(R.id.current_orders_invisible_layout_3)
        val order4 = v.findViewById<LinearLayout>(R.id.current_orders_invisible_layout_4)
        val order5 = v.findViewById<LinearLayout>(R.id.current_orders_invisible_layout_5)
        val order6 = v.findViewById<LinearLayout>(R.id.current_orders_invisible_layout_6)
        val order7 = v.findViewById<LinearLayout>(R.id.current_orders_invisible_layout_7)
        val order8 = v.findViewById<LinearLayout>(R.id.current_orders_invisible_layout_8)
        val order9 = v.findViewById<LinearLayout>(R.id.current_orders_invisible_layout_9)
        val order10 = v.findViewById<LinearLayout>(R.id.current_orders_invisible_layout_10)

        val rotateRight = AnimationUtils.loadAnimation(activity, R.anim.rotate_right)
        val rotateLeft = AnimationUtils.loadAnimation(activity, R.anim.rotate_left)

        if (!clickedItem.isExpanded) {
            // Animates when item is clicked
            TransitionManager.beginDelayedTransition(cardViewInvisible, AutoTransition())
            arrowIcon?.startAnimation(rotateRight)
            arrowIcon?.setColorFilter(Color.parseColor("#000000"))
            orderNumber?.setTextColor(Color.parseColor("#000000"))
            orderNumberSymbol?.setTextColor(Color.parseColor("#000000"))

            clickedItem.isExpanded = true

            if (clickedItem.product1Name != null) {
                order1?.visibility = View.VISIBLE
            }
            if (clickedItem.product2Name != null) {
                order2?.visibility = View.VISIBLE
            }
            if (clickedItem.product3Name != null) {
                order3?.visibility = View.VISIBLE
            }
            if (clickedItem.product4Name != null) {
                order4?.visibility = View.VISIBLE
            }
            if (clickedItem.product5Name != null) {
                order5?.visibility = View.VISIBLE
            }
            if (clickedItem.product6Name != null) {
                order6?.visibility = View.VISIBLE
            }
            if (clickedItem.product7Name != null) {
                order7?.visibility = View.VISIBLE
            }
            if (clickedItem.product8Name != null) {
                order8?.visibility = View.VISIBLE
            }
            if (clickedItem.product9Name != null) {
                order9?.visibility = View.VISIBLE
            }
            if (clickedItem.product10Name != null) {
                order10?.visibility = View.VISIBLE
            }
        } else{
            arrowIcon?.startAnimation(rotateLeft)
            arrowIcon?.setColorFilter(Color.parseColor("#8E8E8E"))
            orderNumber?.setTextColor(Color.parseColor("#8E8E8E"))
            orderNumberSymbol?.setTextColor(Color.parseColor("#8E8E8E"))

            order1.visibility = View.GONE
            order2.visibility = View.GONE
            order3.visibility = View.GONE
            order4.visibility = View.GONE
            order5.visibility = View.GONE
            order6.visibility = View.GONE
            order7.visibility = View.GONE
            order8.visibility = View.GONE
            order9.visibility = View.GONE
            order10.visibility = View.GONE
            clickedItem.isExpanded = false
        }
        ordersAdapter.notifyItemRangeChanged(ordersAdapter.itemCount,position+1)
    }

    override fun onItemOne(position: Int, v: View?) {
        Log.d(TAG, "onItemOne: $position")
    }

    override fun onItemTwo(position: Int, v: View?) {
        Log.d(TAG, "onItemOne: $position")
    }

    override fun onItemThree(position: Int, v: View?) {
        Log.d(TAG, "onItemOne: $position")
    }

    override fun onItemFour(position: Int, v: View?) {
        Log.d(TAG, "onItemOne: $position")
    }

    override fun onItemFive(position: Int, v: View?) {
        Log.d(TAG, "onItemOne: $position")
    }

    override fun onItemSix(position: Int, v: View?) {
        Log.d(TAG, "onItemOne: $position")
    }

    override fun onItemSeven(position: Int, v: View?) {
        Log.d(TAG, "onItemOne: $position")
    }

    override fun onItemEight(position: Int, v: View?) {
        Log.d(TAG, "onItemOne: $position")
    }

    override fun onItemNine(position: Int, v: View?) {
        Log.d(TAG, "onItemOne: $position")
    }

    override fun onItemTen(position: Int, v: View?) {
        Log.d(TAG, "onItemOne: $position")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }

}


