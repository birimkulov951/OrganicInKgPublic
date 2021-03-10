package com.example.organicinkgandroid.ui.auth

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.organicinkgandroid.MainActivity
import com.example.organicinkgandroid.R
import com.example.organicinkgandroid.model.network.ServerResponse
import com.example.organicinkgandroid.retrofit.ApiClient
import com.example.organicinkgandroid.retrofit.SessionManager
import com.example.organicinkgandroid.utils.Constants
import kotlinx.android.synthetic.main.fragment_code_authorization.*
import kotlinx.android.synthetic.main.fragment_code_authorization.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CodeAuthorizationFragment: Fragment() {

    private val TAG = "RegistrationFragment"

    private lateinit var navController: NavController

    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient

    private var smsCode = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView : View = inflater.inflate(R.layout.fragment_code_authorization, container, false)

        apiClient = ApiClient()
        sessionManager = SessionManager(requireContext())
        (activity as MainActivity).hideBottomNavBar()
        /** Button listener*/
        rootView.code_authorization_back_button.setOnClickListener {
            (activity as MainActivity).onBackPressed()
            //navController.navigate(R.id.action_codeAuthorizationFragment_to_registrationFragment)
        }

        /** Button listener*/
        //rootView.code_authorization_navigate_to_login_fragment.setOnClickListener { navController.navigate(R.id.action_codeAuthorizationFragment_to_authorizationFragment) }

        /** EditText listener*/
        rootView.code_authorization_edit_text_enter_code.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {
                if (s.toString() != "") {
                    smsCode = s.toString()
                    rootView.code_authorization_edit_text_enter_code.setBackgroundResource(R.drawable.edit_text_frame_background_edited)
                } else {
                    smsCode = ""
                    rootView.code_authorization_edit_text_enter_code.setBackgroundResource(R.drawable.edit_text_frame_background)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })

        /** Button listener*/
        rootView.code_authorization_button_confirm.setOnClickListener {
            hideKeyboard(rootView)
            if (smsCode.length < 4) {

                warning(getString(R.string.too_short_string))

            } else {
                verifyCode(smsCode)
            }

        }


        return rootView
    }

    private fun verifyCode(code: String) {

        hideViews()

        val call  = apiClient.getApiService().verifySMSCode(code)

        call.enqueue(object : Callback<ServerResponse> {

            override fun onResponse(call: Call<ServerResponse>, response: Response<ServerResponse>) {

                if (response.isSuccessful) {

                    if (response.body()?.resultCode == Constants.SUCCESS && response.body()?.result?.statusCode == Constants.OK) {

                        Log.e(TAG, "onResponse: ${response.body()}")

                        (activity as MainActivity).customToastTwo(getString(R.string.successful_register), false)

                        navController.navigate(R.id.action_codeAuthorizationFragment_to_authorizationFragment)

                    } else if (response.body()?.resultCode == Constants.NOT_FOUND) {

                        showViews()
                        warning(getString(R.string.code_is_invalid))

                    } else {

                        showViews()
                        (activity as MainActivity).customToastTwo(getString(R.string.unknown_error), true)

                    }
                    Log.e(TAG, "onResponse: ${response.body()}")

                } else {
                    showViews()
                    (activity as MainActivity).customToastTwo(getString(R.string.unknown_error), true)
                    Log.e(TAG, "onResponse: ${response.body()}")
                }

            }

            override fun onFailure(call: Call<ServerResponse>, t: Throwable) {
                showViews()
                (activity as MainActivity).customToastTwo(getString(R.string.unknown_error), true)
            }

        })

    }

    private fun warning(message: String) {
        code_authorization_warning_text.text = message
        code_authorization_warning.visibility = View.VISIBLE
    }

    private fun showViews() {
        code_authorization_progressbar.visibility = View.GONE
        code_authorization_text_view_code_authorization.visibility = View.VISIBLE
        code_authorization_edit_text_enter_code.visibility = View.VISIBLE
        code_authorization_text_sms_send.visibility = View.VISIBLE
        code_authorization_button_confirm.visibility = View.VISIBLE
    }

    private fun hideViews() {
        code_authorization_progressbar.visibility = View.VISIBLE
        code_authorization_text_view_code_authorization.visibility = View.GONE
        code_authorization_edit_text_enter_code.visibility = View.GONE
        code_authorization_text_sms_send.visibility = View.GONE
        code_authorization_button_confirm.visibility = View.GONE
        code_authorization_warning.visibility = View.GONE
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