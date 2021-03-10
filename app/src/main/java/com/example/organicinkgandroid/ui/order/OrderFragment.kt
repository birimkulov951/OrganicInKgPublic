package com.example.organicinkgandroid.ui.order

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.organicinkgandroid.MainActivity
import com.example.organicinkgandroid.R
import com.example.organicinkgandroid.retrofit.SessionManager
import com.example.organicinkgandroid.utils.Constants.CASH
import com.example.organicinkgandroid.utils.Constants.STOCK
import kotlinx.android.synthetic.main.fragment_order.*
import kotlinx.android.synthetic.main.fragment_order.view.*
import java.text.SimpleDateFormat
import java.util.*

class OrderFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    lateinit var navController : NavController
    private lateinit var sessionManager: SessionManager

    // Sent bundle data
    private var paymentMethodData: String = "empty"
    private var paymentMethodNumberData: String = "empty"
    private var shipmentMethodData: String = "empty"
    private var shipmentAddressData: String = "empty"
    private var desiredDateData: String = "empty"

    // Click Listener for all views
    private val clickListener = View.OnClickListener { view ->

        sessionManager.saveOrderDetails(paymentMethodData,paymentMethodNumberData,shipmentMethodData,shipmentAddressData,desiredDateData)

        when (view.id) {

            R.id.order_fragment_back_button -> {

                (activity as MainActivity).onBackPressed()

            }

            R.id.order_fragment_select_payment_method_card_view_1 -> {

                navController.navigate(R.id.action_orderFragment_to_paymentMethodFragment)

            }
            R.id.order_fragment_select_payment_method_card_view_2 -> {

                navController.navigate(R.id.action_orderFragment_to_shipmentMethodFragment)

            }
            R.id.order_fragment_select_payment_method_card_view_3 -> {
                // Show calendar DIALOG here
                showDatePickerDialog()
            }

            R.id.order_fragment_next_button -> {

                Log.e("OrderFragment", "$$$$$$$ $paymentMethodData $paymentMethodNumberData $shipmentMethodData $shipmentAddressData $desiredDateData" )

                if (paymentMethodData == "empty" || paymentMethodNumberData == "empty" || shipmentMethodData == "empty" || shipmentAddressData == "empty" || desiredDateData == "empty") {
                    showHiddenAlert()
                } else {

                    navController.navigate(R.id.action_orderFragment_to_contactInfoFragment)

                }

            }

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView: View = inflater.inflate(R.layout.fragment_order, container, false)
        sessionManager = SessionManager(requireContext())
        (activity as MainActivity).hideBasketToastTwo()
        (activity as MainActivity).hideBottomNavBar()

        // Changing fragment toolbar name
        rootView.order_fragment_name.text = getString(R.string.order_fragment_name)

        // Received data from sub fragments
        paymentMethodData = sessionManager.fetchPaymentMethod()!!
        paymentMethodNumberData = sessionManager.fetchPaymentMethodNumber()!!
        shipmentMethodData = sessionManager.fetchShipmentMethod()!!
        shipmentAddressData = sessionManager.fetchShipmentMethodAddress()!!
        desiredDateData = sessionManager.fetchDesiredDate()!!


        if (paymentMethodData != "empty") {
            if (paymentMethodData == CASH) {
                rootView.order_fragment_select_payment_method.text = getString(R.string.cash)
            } else {
                rootView.order_fragment_select_payment_method.text = getString(R.string.elsom)
            }
            rootView.order_fragment_select_payment_method.setTextColor(Color.parseColor("#009B00"))
            rootView.order_fragment_image_1.setImageResource(R.drawable.ic_baseline_add_32_green)
        }
        if (shipmentMethodData != "empty") {
            if (shipmentMethodData == STOCK) {
                rootView.order_fragment_select_shipment_method.text = getString(R.string.stock)
            } else {
                rootView.order_fragment_select_shipment_method.text = getString(R.string.courier)

            }
            rootView.order_fragment_select_shipment_method.setTextColor(Color.parseColor("#009B00"))
            rootView.order_fragment_image_2.setImageResource(R.drawable.ic_baseline_add_32_green)
        }

        if (desiredDateData != "empty") {
            rootView.order_fragment_desired_date.text = desiredDateData
            rootView.order_fragment_desired_date.setTextColor(Color.parseColor("#009B00"))
            rootView.order_fragment_image_3.setImageResource(R.drawable.ic_baseline_add_32_green)
        }

        // Back Button in toolbar
        rootView.order_fragment_back_button?.setOnClickListener(clickListener)

        // Selectable layouts for data
        rootView.order_fragment_select_payment_method_card_view_1?.setOnClickListener(clickListener)
        rootView.order_fragment_select_payment_method_card_view_2?.setOnClickListener(clickListener)
        rootView.order_fragment_select_payment_method_card_view_3?.setOnClickListener(clickListener)
        // Next button listener
        rootView.order_fragment_next_button.setOnClickListener(clickListener)


        Log.d("OrderFragment", "onCreateView: $paymentMethodData - $paymentMethodNumberData - $shipmentMethodData - $shipmentAddressData - $desiredDateData")

        return rootView
    }

    private fun showHiddenAlert() {
        order_fragment_hidden_alert_icon.visibility = View.VISIBLE
        order_fragment_hidden_alert_text.visibility = View.VISIBLE

    }

    private fun hideHiddenAlert() {
        order_fragment_hidden_alert_icon.visibility = View.GONE
        order_fragment_hidden_alert_text.visibility = View.GONE

    }

    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            this,
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    @SuppressLint("SimpleDateFormat")
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        var monthStr = "${month+1}"
        if (monthStr.length == 1) {
            monthStr = "0$monthStr"
        }

        var dayPfMonthStr = dayOfMonth.toString()
        if (dayPfMonthStr.length == 1) {
            dayPfMonthStr = "0$dayPfMonthStr"
        }

        val date = "$year-$monthStr-$dayPfMonthStr"
        val oneDayMillis = 86400000
        val parseDate = SimpleDateFormat("yyyy-MM-dd").parse(date)!!.time

        if (System.currentTimeMillis() > parseDate + oneDayMillis) {
            (activity as MainActivity).customToastTwo(getString(R.string.date_is_wrong),true)
        } else {
            desiredDateData = date
            order_fragment_desired_date.text = date
            order_fragment_desired_date.setTextColor(Color.parseColor("#009B00"))
            order_fragment_image_3.setImageResource(R.drawable.ic_baseline_add_32_green)
            hideHiddenAlert()
        }

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }

}