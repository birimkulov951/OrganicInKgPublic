package com.example.organicinkgandroid.ui.auth

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.organicinkgandroid.MainActivity
import com.example.organicinkgandroid.R
import com.example.organicinkgandroid.model.network.ServerResponse
import com.example.organicinkgandroid.model.network.auth.AuthenticationBody
import com.example.organicinkgandroid.retrofit.ApiClient
import com.example.organicinkgandroid.retrofit.SessionManager
import com.example.organicinkgandroid.utils.Constants
import kotlinx.android.synthetic.main.fragment_authorization.*
import kotlinx.android.synthetic.main.fragment_authorization.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthorizationFragment: Fragment() {

    private val TAG = "AuthorizationFragment"
    private lateinit var navController: NavController

    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient

    // Helper vars
    private var password = ""
    private var userName = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView : View = inflater.inflate(R.layout.fragment_authorization, container, false)

        (activity as MainActivity).hideBottomNavBar()

        apiClient = ApiClient()
        sessionManager = SessionManager(requireContext())


        /** Button listener*/
        rootView.authorization_back_button.setOnClickListener {
            (activity as MainActivity).onBackPressed()
            //navController.navigate(R.id.action_authorizationFragment_to_profileFragment)
        }

        /** Button listener*/
        rootView.authorization_navigate_to_registration_fragment.setOnClickListener {
            //(activity as MainActivity).onBackPressed()
            navController.navigate(R.id.action_authorizationFragment_to_registrationFragment)
        }

        /** EditText listener*/
        if (sessionManager.fetchPhoneNumber() != "null") {
            userName = sessionManager.fetchPhoneNumber()!!
            rootView.authorization_edit_text_field_phone_number.setText(userName, TextView.BufferType.EDITABLE)
            rootView.authorization_edit_text_field_phone_number.setBackgroundResource(R.drawable.edit_text_frame_background_edited)
        }

        rootView.authorization_edit_text_field_phone_number.addTextChangedListener(object :
            TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString() != "") {
                    userName = s.toString()
                    rootView.authorization_edit_text_field_phone_number.setBackgroundResource(R.drawable.edit_text_frame_background_edited)
                } else {
                    userName = ""
                    rootView.authorization_edit_text_field_phone_number.setBackgroundResource(R.drawable.edit_text_frame_background)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })

        /** EditText listener*/
        rootView.authorization_edit_text_field_password.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString() != "") {
                    password = s.toString()
                    rootView.authorization_edit_text_field_password.setBackgroundResource(R.drawable.edit_text_frame_background_edited)
                } else {
                    password = ""
                    rootView.authorization_edit_text_field_password.setBackgroundResource(R.drawable.edit_text_frame_background)
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        /** Toggle listener*/
        rootView.authorization_password_toggle.setOnClickListener {
            updatePasswordVisibility(rootView.authorization_edit_text_field_password)
        }

        /** Login Button listener*/
        rootView.authorization_button_login.setOnClickListener{
            if (userName == "" || password == "") {
                warning(getString(R.string.not_all_fields_filled))
            } else if (password.length < 6) {
                warning(getString(R.string.password_format))
            } else if(userName.length != 13) {

                warning(getString(R.string.phone_number_format))
            } else if (userName[0].toString() != "+") {
                warning(getString(R.string.phone_number_format_2))
            } else {
                hideKeyboard(rootView)
                login(userName,password)
                hideViews()
            }

        }

        return rootView
    }


    private fun login(userName: String,password: String) {

        hideViews()

        val call  = apiClient.getApiService().login(AuthenticationBody(password, userName))

        call.enqueue(object : Callback<ServerResponse> {

            override fun onResponse(call: Call<ServerResponse>, response: Response<ServerResponse>) {

                Log.e(TAG, "onResponse: ${response.body()}")

                if (response.isSuccessful) {

                    if (response.body()?.resultCode == Constants.SUCCESS) {

                        if (response.body()?.result?.statusCode == Constants.BAD_REQUEST) {
                            showViews()
                            warning(getString(R.string.wrong_password))
                        } else if (response.body()?.result?.statusCode == Constants.ACCEPTED) {

                            Log.e(TAG, "onResponse: ${response.body()}")

                            // Saving tokens to refs
                            response.body()?.result?.body?.id?.let {
                                sessionManager.saveAuthTokens(
                                    it,
                                    response.body()?.result?.body?.accessToken!!,
                                    response.body()?.result?.body?.tokenExpirationTime!!,
                                    response.body()?.result?.body?.refreshToken!!,
                                    response.body()?.result?.body?.refreshExpirationTime!!)
                            }

                            sessionManager.savePhoneNumber(userName)

                            (activity as MainActivity).showBottomNavBar(getString(R.string.successful_login),false)
                            Log.e("token", sessionManager.fetchAccessToken().toString())

                            navController.navigate(R.id.action_authorizationFragment_to_homeFragment)

                        }  else {
                            showViews()
                            (activity as MainActivity).customToastTwo(getString(R.string.unknown_error),true)

                        }

                    } else if (response.body()?.resultCode == Constants.NOT_FOUND) {
                        showViews()
                        warning(getString(R.string.user_not_found))

                    } else {
                        showViews()
                        (activity as MainActivity).customToastTwo(getString(R.string.unknown_error),true)

                    }

                } else {

                    showViews()
                    (activity as MainActivity).customToastTwo(getString(R.string.unknown_error),true)

                }

            }

            override fun onFailure(call: Call<ServerResponse>, t: Throwable) {
                showViews()
                (activity as MainActivity).customToastTwo(getString(R.string.unknown_error),true)
            }

        })

    }



    //show and hide option for entering password
    private fun updatePasswordVisibility(editText: EditText) {
        if (editText.transformationMethod is PasswordTransformationMethod) {
            editText.transformationMethod = null
            authorization_password_toggle.setImageResource(R.drawable.ic_baseline_remove_red_eye_24)
        } else {
            editText.transformationMethod = PasswordTransformationMethod()
            authorization_password_toggle.setImageResource(R.drawable.ic_baseline_visibility_off_24)
        }
        editText.setSelection(editText.length())
    }

    private fun showViews() {
        authorization_progressbar.visibility = View.GONE
        authorization_text_view.visibility = View.VISIBLE
        authorization_edit_text_field_phone_number.visibility = View.VISIBLE
        authorization_edit_text_field_password.visibility = View.VISIBLE
        authorization_button_login.visibility = View.VISIBLE
        authorization_password_toggle.visibility = View.VISIBLE
        authorization_button_login.visibility = View.VISIBLE
    }

    private fun hideViews() {
        authorization_progressbar.visibility = View.VISIBLE
        authorization_text_view.visibility = View.GONE
        authorization_edit_text_field_phone_number.visibility = View.GONE
        authorization_edit_text_field_password.visibility = View.GONE
        authorization_button_login.visibility = View.GONE
        authorization_password_toggle.visibility = View.GONE
        authorization_button_login.visibility = View.GONE
        authorization_warning.visibility = View.GONE
    }

    private fun warning(message: String) {
        authorization_warning.visibility = View.VISIBLE
        authorization_warning_text.text = message
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