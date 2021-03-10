package com.example.organicinkgandroid.ui.order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.organicinkgandroid.MainActivity
import com.example.organicinkgandroid.R
import com.example.organicinkgandroid.retrofit.ApiClient
import com.example.organicinkgandroid.retrofit.SessionManager
import kotlinx.android.synthetic.main.fragment_order_info.view.*


class OrderInfoFragment : Fragment() {

    private val TAG = "OrderConfirmationFragment"

    lateinit var navController: NavController
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient
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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView: View = inflater.inflate(R.layout.fragment_order_info, container, false)
        apiClient = ApiClient()
        sessionManager = SessionManager(requireContext())

        (activity as MainActivity).hideBottomNavBar()

        rootView.order_confirmation_fragment_product_payment_code.text = sessionManager.fetchOrderNumber()

        (activity as MainActivity).customToastTwo("Ваш номер заказа: ${sessionManager.fetchOrderNumber()}",false)

        paymentMethodData = sessionManager.fetchPaymentMethod()!!
        paymentMethodNumberData = sessionManager.fetchPaymentMethodNumber()!!
        shipmentMethodData = sessionManager.fetchShipmentMethod()!!
        shipmentAddressData = sessionManager.fetchShipmentMethodAddress()!!
        desiredDateData = sessionManager.fetchDesiredDate()!!
        contactName =  sessionManager.fetchContactName()!!
        contactSurname = sessionManager.fetchContactSurname()!!
        contactPhoneNumber =  sessionManager.fetchContactPhoneNumber()!!



        rootView.order_confirmation_fragment_next_btn.setOnClickListener{
            navController.navigate(R.id.action_orderConfirmationFragment_to_homeFragment)
        }




        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }

}