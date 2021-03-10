package com.example.organicinkgandroid.ui.order

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.organicinkgandroid.MainActivity
import com.example.organicinkgandroid.R
import com.example.organicinkgandroid.retrofit.SessionManager
import com.example.organicinkgandroid.utils.Constants.CASH
import com.example.organicinkgandroid.utils.Constants.ELSOM
import kotlinx.android.synthetic.main.fragment_payment_method.*
import kotlinx.android.synthetic.main.fragment_payment_method.view.*

class PaymentMethodFragment : Fragment() {

    lateinit var navController: NavController
    private lateinit var sessionManager: SessionManager

    private var paymentMethodData: String = "empty"
    private var paymentMethodNumberData: String = "empty"
    private var shipmentMethodData: String = "empty"
    private var shipmentAddressData: String = "empty"
    private var desiredDateData: String = "empty"
    private var isCashChecked = false
    private var isElsomChecked = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView: View =  inflater.inflate(R.layout.fragment_payment_method, container, false)
        (activity as MainActivity).hideBottomNavBar()

        sessionManager = SessionManager(requireContext())

        paymentMethodData = sessionManager.fetchPaymentMethod()!!
        paymentMethodNumberData = sessionManager.fetchPaymentMethodNumber()!!
        shipmentMethodData = sessionManager.fetchShipmentMethod()!!
        shipmentAddressData = sessionManager.fetchShipmentMethodAddress()!!
        desiredDateData = sessionManager.fetchDesiredDate()!!

        if (paymentMethodData != "empty") {
            if (paymentMethodData == CASH) {
                rootView.payment_method_fragment_radioButton_cash.isChecked = true
                isCashChecked = true
                paymentMethodNumberData = ""
            } else {
                rootView.payment_method_fragment_radioButton_elsom.isChecked = true
                isElsomChecked = true
            }

        }

        if (paymentMethodNumberData != "empty") {
            rootView.payment_method_fragment_edit_text.setText(paymentMethodNumberData, TextView.BufferType.EDITABLE)
        }


        // If user did not select SOM currency
        if ((activity as MainActivity).currencyString != "cом") {
            rootView.payment_method_fragment_radioButton_cash.isChecked = true
            paymentMethodData = CASH
            paymentMethodNumberData = ""

            isCashChecked = true
            isElsomChecked = false

            showHiddenAlert(rootView)
            rootView.payment_method_fragment_hidden_alert_text.text = getString(R.string.elsom_number_format_3)

        }



        // RadioButton listener
        rootView.payment_method_fragment_radio_group.setOnCheckedChangeListener { _, checkedId ->

            if (checkedId == R.id.payment_method_fragment_radioButton_cash) {

                paymentMethodData = CASH
                isCashChecked = true
                isElsomChecked = false
                paymentMethodNumberData = ""

            } else {

                if ((activity as MainActivity).currencyString != "cом") {

                    paymentMethodData = CASH
                    isCashChecked = true
                    isElsomChecked = false
                    paymentMethodNumberData = ""
                    rootView.payment_method_fragment_radioButton_cash.isChecked = true

                } else {

                    paymentMethodData = ELSOM
                    isCashChecked = false
                    isElsomChecked = true

                }

            }

        }

        // EditText listener
        rootView.payment_method_fragment_edit_text.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString() != "") {
                    paymentMethodNumberData = s.toString()
                    rootView.payment_method_fragment_edit_text.setBackgroundResource(R.drawable.edit_text_frame_background_edited)
                } else {
                    paymentMethodNumberData = "empty"
                    rootView.payment_method_fragment_edit_text.setBackgroundResource(R.drawable.edit_text_frame_background)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        // Save Button listener
        rootView.payment_method_fragment_save_button.setOnClickListener{

            if (isElsomChecked && paymentMethodNumberData == "") {

                showHiddenAlert()

            } else if (!isCashChecked && !isElsomChecked) {

                showHiddenAlert()

            }  else if (isElsomChecked && paymentMethodNumberData.length != 10) {

                showHiddenAlert()
                rootView.payment_method_fragment_hidden_alert_text.text = getString(R.string.elsom_number_format)

            }  else if (isElsomChecked && paymentMethodNumberData[0] != '0') {

                showHiddenAlert()
                rootView.payment_method_fragment_hidden_alert_text.text = getString(R.string.elsom_number_format_2)

            } else {
                // Closing this fragment
                sessionManager.saveOrderDetails(paymentMethodData,paymentMethodNumberData,shipmentMethodData,shipmentAddressData,desiredDateData)

                (activity as MainActivity).onBackPressed()

            }
        }

        // Back Button listener
        rootView.payment_method_fragment_back_button.setOnClickListener{
            (activity as MainActivity).onBackPressed()
        }

        Log.d("PaymentFragment", "onCreateView: $paymentMethodData - $paymentMethodNumberData - $shipmentMethodData - $shipmentAddressData - $desiredDateData")

        return rootView
    }

    private fun showHiddenAlert() {
        payment_method_fragment_hidden_alert_icon.visibility = View.VISIBLE
        payment_method_fragment_hidden_alert_text.visibility = View.VISIBLE
    }

    private fun showHiddenAlert(rootView: View) {
        rootView.payment_method_fragment_hidden_alert_icon.visibility = View.VISIBLE
        rootView.payment_method_fragment_hidden_alert_text.visibility = View.VISIBLE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }

}