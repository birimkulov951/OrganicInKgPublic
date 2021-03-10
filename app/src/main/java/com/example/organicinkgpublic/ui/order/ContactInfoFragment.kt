package com.example.organicinkgpublic.ui.order

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.organicinkgpublic.MainActivity
import com.example.organicinkgpublic.R
import com.example.organicinkgpublic.retrofit.ApiClient
import com.example.organicinkgpublic.retrofit.SessionManager
import kotlinx.android.synthetic.main.fragment_contact_info.*
import kotlinx.android.synthetic.main.fragment_contact_info.view.*

class ContactInfoFragment : Fragment() {

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView: View = inflater.inflate(R.layout.fragment_contact_info, container, false)
        apiClient = ApiClient()
        sessionManager = SessionManager(requireContext())
        (activity as MainActivity).hideBottomNavBar()

        // Bundle data receiver
        // Received data from sub fragments
        paymentMethodData = sessionManager.fetchPaymentMethod()!!
        paymentMethodNumberData = sessionManager.fetchPaymentMethodNumber()!!
        shipmentMethodData = sessionManager.fetchShipmentMethod()!!
        shipmentAddressData = sessionManager.fetchShipmentMethodAddress()!!
        desiredDateData = sessionManager.fetchDesiredDate()!!
        contactName = sessionManager.fetchContactName()!!
        contactSurname = sessionManager.fetchContactSurname()!!

        if (sessionManager.fetchContactPhoneNumber() != "empty") {

            contactPhoneNumber = sessionManager.fetchContactPhoneNumber()!!

        } else {

            if (sessionManager.fetchPhoneNumber() != "null") {
                contactPhoneNumber = sessionManager.fetchPhoneNumber()!!
            } else {
                sessionManager.saveOrderContacts(contactName,contactSurname,contactPhoneNumber)
            }

        }



        if (contactName != "empty") {
            rootView.contact_info_fragment_contact_name.setText(contactName, TextView.BufferType.EDITABLE)
            rootView.contact_info_fragment_contact_name.setBackgroundResource(R.drawable.edit_text_frame_background_edited)
        }
        if (contactSurname != "empty") {
            rootView.contact_info_fragment_contact_surname.setText(contactSurname, TextView.BufferType.EDITABLE)
            rootView.contact_info_fragment_contact_surname.setBackgroundResource(R.drawable.edit_text_frame_background_edited)
        }
        if (contactPhoneNumber != "empty") {

            rootView.contact_info_fragment_contact_phone_number.setText(contactPhoneNumber, TextView.BufferType.EDITABLE)
            rootView.contact_info_fragment_contact_phone_number.setBackgroundResource(R.drawable.edit_text_frame_background_edited)

        } else if (sessionManager.fetchPhoneNumber() != "empty") {

            rootView.contact_info_fragment_contact_phone_number.setText(contactPhoneNumber, TextView.BufferType.EDITABLE)

        }

        // Contact Name EditText listener
        rootView.contact_info_fragment_contact_name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString() != "") {
                    contactName = s.toString()
                    rootView.contact_info_fragment_contact_name.setBackgroundResource(R.drawable.edit_text_frame_background_edited)
                } else {
                    contactName = "empty"
                    rootView.contact_info_fragment_contact_name.setBackgroundResource(R.drawable.edit_text_frame_background)
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        // Contact Surname EditText listener
        rootView.contact_info_fragment_contact_surname.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString() != "") {
                    contactSurname = s.toString()
                    rootView.contact_info_fragment_contact_surname.setBackgroundResource(R.drawable.edit_text_frame_background_edited)
                } else {
                    contactSurname = "empty"
                    rootView.contact_info_fragment_contact_surname.setBackgroundResource(R.drawable.edit_text_frame_background)
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        // Contact phone number EditText listener
        rootView.contact_info_fragment_contact_phone_number.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString() != "") {
                    contactPhoneNumber = s.toString()
                    rootView.contact_info_fragment_contact_phone_number.setBackgroundResource(R.drawable.edit_text_frame_background_edited)
                    Log.d("ContactInfoFragment", "onCreateView: $contactName - $contactSurname - $contactPhoneNumber")
                } else {
                    contactPhoneNumber = "empty"
                    rootView.contact_info_fragment_contact_phone_number.setBackgroundResource(R.drawable.edit_text_frame_background)
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        rootView.contact_info_fragment_radio_btn.setOnCheckedChangeListener{ group, _ ->
            hideKeyBoard()
            if (group.isChecked) { isChecked = true }
        }

        rootView.contact_info_fragment_next_btn.setOnClickListener{
            hideKeyBoard()
            if (rootView.contact_info_fragment_contact_name.text.toString() == "" || rootView.contact_info_fragment_contact_surname.text.toString() == "" || rootView.contact_info_fragment_contact_phone_number.text.toString() == "" || !isChecked) {
                // shows hidden alert message
                showHiddenAlert()

            } else if (contactPhoneNumber.length != 13) {

                showHiddenAlert()
                rootView.contact_info_fragment_hidden_alert_text.text = getString(R.string.phone_number_format)

            }  else if (contactPhoneNumber[0] != '+') {

                showHiddenAlert()
                rootView.contact_info_fragment_hidden_alert_text.text = getString(R.string.phone_number_format_2)

            } else {

                // opens PaymentFragment and send all needed vars
              //  val orderFragment = OrderPaymentFragment()
                sessionManager.saveOrderDetails(paymentMethodData,paymentMethodNumberData,shipmentMethodData,shipmentAddressData,desiredDateData)
                sessionManager.saveOrderContacts(contactName,contactSurname,contactPhoneNumber)

                navController.navigate(R.id.action_contactInfoFragment_to_summaryFragment)

            }
        }

        // Back Button listener
        rootView.contact_info_fragment_back_button.setOnClickListener{
            //sessionManager.saveOrderDetails(paymentMethodData,paymentMethodNumberData,shipmentMethodData,shipmentAddressData,desiredDateData)
            (activity as MainActivity).onBackPressed()
        }

        Log.d("PaymentFragment", "onCreateView: $paymentMethodData - $paymentMethodNumberData - $shipmentMethodData - $shipmentAddressData - $desiredDateData")


        return rootView
    }


    private fun showHiddenAlert() {
        contact_info_fragment_hidden_alert_icon.visibility = View.VISIBLE
        contact_info_fragment_hidden_alert_text.visibility = View.VISIBLE
    }

    private fun hideKeyBoard() {
        val view = activity?.currentFocus
        if (view != null) {
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm!!.hideSoftInputFromWindow(requireView().windowToken, 0)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }
}