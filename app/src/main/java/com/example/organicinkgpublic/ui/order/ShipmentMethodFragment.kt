package com.example.organicinkgpublic.ui.order

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
import com.example.organicinkgpublic.MainActivity
import com.example.organicinkgpublic.R
import com.example.organicinkgpublic.model.network.ordersettings.OrderSettingsResponse
import com.example.organicinkgpublic.retrofit.ApiClient
import com.example.organicinkgpublic.retrofit.SessionManager
import com.example.organicinkgpublic.utils.Constants.COURIER
import com.example.organicinkgpublic.utils.Constants.STOCK
import kotlinx.android.synthetic.main.fragment_shipment_method.*
import kotlinx.android.synthetic.main.fragment_shipment_method.view.*
import retrofit2.Call
import retrofit2.Response

class ShipmentMethodFragment : Fragment() {

    lateinit var navController: NavController
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient

    private var paymentMethodData: String = "empty"
    private var paymentMethodNumberData: String = "empty"
    private var shipmentMethodData: String = "empty"
    private var shipmentAddressData: String = "empty"
    private var desiredDateData: String = "empty"
    private var isRadioButtonChecked = false

    private var isDeliveryNotWorks = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val rootView: View = inflater.inflate(R.layout.fragment_shipment_method, container, false)
        (activity as MainActivity).hideBottomNavBar()

        apiClient = ApiClient()
        sessionManager = SessionManager(requireContext())

        // Received data from sub fragments
        paymentMethodData = sessionManager.fetchPaymentMethod()!!
        paymentMethodNumberData = sessionManager.fetchPaymentMethodNumber()!!
        shipmentMethodData = sessionManager.fetchShipmentMethod()!!
        shipmentAddressData = sessionManager.fetchShipmentMethodAddress()!!
        desiredDateData = sessionManager.fetchDesiredDate()!!


        if (shipmentMethodData != "empty") {
            isRadioButtonChecked = true
            if (shipmentMethodData == STOCK) {
                rootView.shipment_method_fragment_radioButton_stock.isChecked = true
            } else {
                rootView.shipment_method_fragment_radioButton_courier.isChecked = true
            }

        }
        if (shipmentAddressData != "empty") {
            rootView.shipment_method_fragment_edit_text.setText(shipmentAddressData, TextView.BufferType.EDITABLE)
        }


        // RadioButton listener
        rootView.shipment_method_fragment_radio_group.setOnCheckedChangeListener { _, checkedId ->

            if (checkedId == R.id.shipment_method_fragment_radioButton_stock) {
                shipmentMethodData = STOCK
                isRadioButtonChecked = true
                shipmentAddressData = ""

            }else {
                if (isDeliveryNotWorks) {
                    rootView.shipment_method_fragment_radioButton_stock.isChecked = true
                    shipmentMethodData = STOCK
                    shipmentAddressData = ""
                    isRadioButtonChecked = true
                } else {
                    shipmentMethodData = COURIER
                    isRadioButtonChecked = true
                }

            }

        }

        // EditText listener
        rootView.shipment_method_fragment_edit_text.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString() != "") {
                    shipmentAddressData = s.toString()
                    rootView.shipment_method_fragment_edit_text.setBackgroundResource(R.drawable.edit_text_frame_background_edited)
                } else {
                    shipmentAddressData = "empty"
                    rootView.shipment_method_fragment_edit_text.setBackgroundResource(R.drawable.edit_text_frame_background)
                }
                //Toast.makeText(context, s.toString(), Toast.LENGTH_SHORT).show()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        // Save Button listener
        rootView.shipment_method_fragment_save_button.setOnClickListener{
            if (/*rootView.shipment_method_fragment_edit_text.text.toString() == "" ||*/ !isRadioButtonChecked) {

                showHiddenAlert()

            } else {

                // Closing this Fragment
                sessionManager.saveOrderDetails(paymentMethodData,paymentMethodNumberData,shipmentMethodData,shipmentAddressData,desiredDateData)
                (activity as MainActivity).onBackPressed()
                //navController.navigate(R.id.action_shipmentMethodFragment_to_orderFragment)

            }
        }

        // Back Button listener
        rootView.shipment_method_fragment_back_button.setOnClickListener{
            // if back button is pressed do not save any data
            (activity as MainActivity).onBackPressed()
        }


        getParentCategoryList(rootView)



        Log.d("ShipmentFragment", "onCreateView: $paymentMethodData - $paymentMethodNumberData - $shipmentMethodData - $shipmentAddressData - $desiredDateData")

        return rootView
    }

    private fun getParentCategoryList(rootView: View) {

        val call = apiClient.getApiService().getOrderSettingList()

        call.enqueue(object : retrofit2.Callback<OrderSettingsResponse> {

            override fun onResponse(call: Call<OrderSettingsResponse>, response: Response<OrderSettingsResponse>) {

                if (response.isSuccessful) {
                    if(response.body()!!.result!!.isEmpty()) {

                        rootView.shipment_method_fragment_radioButton_stock.isChecked = true
                        shipmentMethodData = STOCK
                        (activity as MainActivity).customToastTwo(getString(R.string.shipment_is_restricted),true)
                        isDeliveryNotWorks = true

                    } else {
                        // todo let Erkin know that -price == shipment_is_restricted
                        if (response.body()!!.result!![0].deliveryPrice!! < 0) {

                            rootView.shipment_method_fragment_radioButton_stock.isChecked = true
                            shipmentMethodData = STOCK
                            (activity as MainActivity).customToastTwo(getString(R.string.shipment_is_restricted),true)
                            isDeliveryNotWorks = true

                        } else {
                            shipment_method_fragment_shipment_price.text = response.body()!!.result!![0].deliveryPrice!!.toString()
                            // Saving courier delivery price to use it in total price count in elsom payment method
                            sessionManager.saveCourierPrice(response.body()!!.result!![0].deliveryPrice!!)
                        }

                    }

                } else {
                    (activity as MainActivity).customToastTwo(getString(R.string.unknown_error),true)
                }
                Log.e("ShipmentMethodFragment", "onResponse: ${response.body()}")
            }

            override fun onFailure(call: Call<OrderSettingsResponse>, t: Throwable) {
                (activity as MainActivity).customToastTwo(getString(R.string.unknown_error),true)
                Log.e("ShipmentMethodFragment", t.message.toString())
            }

        })
    }

    private fun showHiddenAlert() {
        shipment_method_fragment_hidden_alert_icon.visibility = View.VISIBLE
        shipment_method_fragment_hidden_alert_text.visibility = View.VISIBLE

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }
}