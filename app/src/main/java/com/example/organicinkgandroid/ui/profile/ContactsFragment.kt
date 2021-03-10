package com.example.organicinkgandroid.ui.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.organicinkgandroid.R
import com.example.organicinkgandroid.model.network.contacts.ContactsResponse
import com.example.organicinkgandroid.retrofit.ApiClient
import com.example.organicinkgandroid.retrofit.SessionManager
import kotlinx.android.synthetic.main.fragment_contacts.view.*
import retrofit2.Call
import retrofit2.Response


class ContactsFragment : Fragment() {

    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient
    private lateinit var address: TextView
    private lateinit var phoneNumber: TextView
    private lateinit var email: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_contacts, container, false)

        apiClient = ApiClient()
        sessionManager = SessionManager(requireContext())
        address = rootView.contacts_address_street
        phoneNumber = rootView.contacts_number
        email = rootView.contacts_gmail
        getContactsInfo()



        rootView.contacts_back_button.setOnClickListener{
            requireActivity().onBackPressed()
        }


        return rootView
    }

    private fun getContactsInfo() {
        val call = apiClient.getApiService().getContactsInfo().enqueue(object: retrofit2.Callback<ContactsResponse> {
            override fun onFailure(call: Call<ContactsResponse>, t: Throwable) {
                Log.e("contacts: ", t.message.toString())
            }

            override fun onResponse(
                call: Call<ContactsResponse>,
                response: Response<ContactsResponse>
            ) {
                if (response.isSuccessful && response.body()!!.result.size != 0){
                    address.text = response.body()!!.result[0].address
                    phoneNumber.text = response.body()!!.result[0].phoneNumber
                    email.text = response.body()!!.result[0].email
                }
            }

        })
    }


}