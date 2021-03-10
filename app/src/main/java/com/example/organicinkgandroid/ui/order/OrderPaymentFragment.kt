package com.example.organicinkgandroid.ui.order

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.organicinkgandroid.MainActivity
import com.example.organicinkgandroid.R
import com.example.organicinkgandroid.retrofit.SessionManager
import com.example.organicinkgandroid.utils.Constants.CASH
import kotlinx.android.synthetic.main.exit_alert_dialog.view.*
import kotlinx.android.synthetic.main.fragment_order_payment.view.*

class OrderPaymentFragment : Fragment() {

    lateinit var navController: NavController
    private lateinit var sessionManager: SessionManager

    // Global vars for order payment fragments
    private var paymentMethodData: String = "empty"
    private var paymentMethodNumberData: String = "empty"
    private var shipmentMethodData: String = "empty"
    private var shipmentAddressData: String = "empty"
    private var desiredDateData: String = "empty"

    // Contact Info Vars
    private var contactName: String = "empty"
    private var contactSurname: String = "empty"
    private var contactPhoneNumber: String = "empty"

    // ELSOM
    private var mMSISDN: String = ""
    private var mPartnerTrnID: String = "" // 20 length? is 10 ok?
    private var mAmount: String = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView: View = inflater.inflate(R.layout.fragment_order_payment, container, false)
        (activity as MainActivity).hideBottomNavBar()
        sessionManager = SessionManager(requireContext())

        rootView.order_payment_fragment_back_btn.setOnClickListener{
            closeFragmentFun()
        }

        paymentMethodData = sessionManager.fetchPaymentMethod()!!
        paymentMethodNumberData = sessionManager.fetchPaymentMethodNumber()!!
        shipmentMethodData = sessionManager.fetchShipmentMethod()!!
        shipmentAddressData = sessionManager.fetchShipmentMethodAddress()!!
        desiredDateData = sessionManager.fetchDesiredDate()!!
        contactName =  sessionManager.fetchContactName()!!
        contactSurname = sessionManager.fetchContactSurname()!!
        contactPhoneNumber =  sessionManager.fetchContactPhoneNumber()!!

        val url = arguments?.getString("ELSOM_URL","null")

        // Elsom vars
        mMSISDN = paymentMethodNumberData
        if(shipmentMethodData == CASH) {
            mAmount = "${sessionManager.fetchTotalPrice() + sessionManager.fetchCourierPrice()}"
        } else {
            mAmount = sessionManager.fetchTotalPrice().toString()
        }

        mPartnerTrnID = sessionManager.fetchOrderNumber()
        (activity as MainActivity).customToastTwo(mPartnerTrnID,false)
        rootView.order_payment_fragment_product_payment_transaction_code.setText(mPartnerTrnID,TextView.BufferType.EDITABLE)
        rootView.order_payment_fragment_product_payment_transaction_code.setBackgroundResource(R.drawable.edit_text_frame_background_edited)



        /** Order cancel button in Toolbar*/
        rootView.order_payment_fragment_cancel_btn.setOnClickListener{
            // Custom Alert Dialog
            val alertDialog = LayoutInflater.from(rootView.context).inflate(R.layout.exit_alert_dialog, null)
            val alertTitle = alertDialog.findViewById<TextView>(R.id.text_view_want_to_exit)
            val alertText = alertDialog.findViewById<TextView>(R.id.text_view_want_to_exit_2)
            alertTitle.text = getString(R.string.sure_to_cancel_order)
            alertText.text = getString(R.string.sure_to_cancel_order)
            alertText.visibility = View.VISIBLE

            val builder = AlertDialog.Builder(rootView.context)
                .setView(alertDialog)
                .show()
            alertDialog.stay_button.setOnClickListener {
                builder.dismiss()
            }
            alertDialog.exit_button.setOnClickListener {
                builder.dismiss()

                navController.navigate(R.id.action_orderPaymentFragment_to_basketFragment)

            }
        }

       /* *//** EditText listener fro Elsom Wallet Number*//*
        rootView.order_payment_fragment_elsom_wallet_number.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString() != "") {
                    mMSISDN = s.toString()
                    rootView.order_payment_fragment_elsom_wallet_number.setBackgroundResource(R.drawable.edit_text_frame_background_edited)
                } else {
                    mMSISDN = "0"
                    rootView.order_payment_fragment_elsom_wallet_number.setBackgroundResource(R.drawable.edit_text_frame_background)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })*/






        /** Post-запрос в сторону  Элсом*/
        rootView.order_payment_fragment_next_btn.setOnClickListener{
            hideKeyboard(rootView)
            if(url != "null") {
                val bundle = Bundle()
                bundle.putString("ELSOM_URL",url)
                navController.navigate(R.id.action_orderPaymentFragment_to_webViewFragment,bundle)
            }

        }

        Log.d("OrderPaymentFragment", "Received data: " +
                "Contact name: $contactName" +
                "Contact surname $contactSurname" +
                "Contact phone number: $contactPhoneNumber" +
                "Payment method: $paymentMethodData" +
                "Payment number: $paymentMethodNumberData" +
                "Shipment method: $shipmentMethodData" +
                "Shipment address: $shipmentAddressData" +
                "Shipment desired date: $desiredDateData ")

        return rootView
    }



    private fun closeFragmentFun() {

        sessionManager.saveOrderDetails(paymentMethodData,paymentMethodNumberData,shipmentMethodData,shipmentAddressData,desiredDateData)
        sessionManager.saveOrderContacts(contactName,contactSurname,contactPhoneNumber)

        navController.navigate(R.id.action_orderPaymentFragment_to_basketFragment)

    }

    private fun hideKeyboard(rootView: View) {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(rootView.windowToken, 0)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }

}