package com.example.organicinkgpublic.ui.auth

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
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.organicinkgpublic.MainActivity
import com.example.organicinkgpublic.R
import com.example.organicinkgpublic.model.network.ServerResponse
import com.example.organicinkgpublic.model.network.auth.UserEntityBody
import com.example.organicinkgpublic.retrofit.ApiClient
import com.example.organicinkgpublic.retrofit.SessionManager
import com.example.organicinkgpublic.utils.Constants.DUPLICATE
import com.example.organicinkgpublic.utils.Constants.NOT_ALLOWED
import com.example.organicinkgpublic.utils.Constants.SUCCESS
import com.example.organicinkgpublic.utils.Constants.USER
import kotlinx.android.synthetic.main.fragment_registration.*
import kotlinx.android.synthetic.main.fragment_registration.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrationFragment: Fragment() {

    private val TAG = "RegistrationFragment"
    lateinit var navController: NavController

    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient

    // Helper vars
    private var password = ""
    private var userName = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val rootView : View = inflater.inflate(R.layout.fragment_registration, container, false)

        apiClient = ApiClient()
        sessionManager = SessionManager(requireContext())
        (activity as MainActivity).hideBottomNavBar()
        (activity as MainActivity).hideBasketToastTwo()

        rootView.registration_back_button.setOnClickListener {
            (activity as MainActivity).onBackPressed()
            //navController.navigate(R.id.action_registrationFragment_to_authorizationFragment)
        }
        rootView.registration_login_option.setOnClickListener {
            navController.navigate(R.id.action_registrationFragment_to_authorizationFragment)
        }
        rootView.if_want_to_verify_sms.setOnClickListener{
            navController.navigate(R.id.action_registrationFragment_to_codeAuthorizationFragment)
        }

        /** EditText listener*/
        rootView.edit_text_registration_field_phone_number.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString() != "") {
                    userName = s.toString()
                    rootView.edit_text_registration_field_phone_number.setBackgroundResource(R.drawable.edit_text_frame_background_edited)
                } else {
                    userName = ""
                    rootView.edit_text_registration_field_phone_number.setBackgroundResource(R.drawable.edit_text_frame_background)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })

        /** EditText listener*/
        rootView.edit_text_registration_field_password.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString() != "") {
                    password = s.toString()
                    rootView.edit_text_registration_field_password.setBackgroundResource(R.drawable.edit_text_frame_background_edited)
                } else {
                    password = ""
                    rootView.edit_text_registration_field_password.setBackgroundResource(R.drawable.edit_text_frame_background)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        /** Toggle listener*/
        rootView.registration_password_toggle.setOnClickListener {
            updatePasswordVisibility(rootView.edit_text_registration_field_password)
        }

        /** Next Button listener*/
        rootView.registration_next_button.setOnClickListener{
            if (userName == "" || password == "" || !rootView.registration_radio_button.isChecked) {
                warning(getString(R.string.not_all_fields_filled))
            } else if (password.length < 6) {
                warning(getString(R.string.password_format))
            } else if (userName[0].toString() != "+") {
                warning(getString(R.string.phone_number_format_2))
            } else if(userName.length != 13) {

                warning(getString(R.string.phone_number_format))
            } else if(!isValidPassword(password)){
                warning(getString(R.string.password_security_warning))
            }
            else {
                hideKeyboard(rootView)
                register(userName,password)
                sessionManager.savePhoneNumber(userName)
                hideViews()
            }


        }

        return rootView
    }

    private fun isValidPassword(password: String): Boolean {
        password.let {
            val passwordPattern = "^(?=.*[0-9])(?=.*[а-яА-Яa-zA-Z])(?=\\S+$).{4,}$"
            val passwordMatcher = Regex(passwordPattern)
            Log.e(TAG, (passwordMatcher.find(password) != null).toString())
            return passwordMatcher.find(password) != null
        }

    }

    private fun register(userName: String,password: String) {

        hideViews()

        val call  = apiClient.getApiService().register(UserEntityBody(listOf(USER), password, userName))

        call.enqueue(object : Callback<ServerResponse> {

            override fun onResponse(call: Call<ServerResponse>, response: Response<ServerResponse>) {

                Log.e(TAG, "onResponse: ${response.body()}")

                if (response.isSuccessful) {

                    if (response.body()?.resultCode == SUCCESS) {

                        Log.e(TAG, "onResponse: ${response.body()}")

                        navController.navigate(R.id.action_registrationFragment_to_codeAuthorizationFragment)

                    } else if (response.body()?.resultCode == DUPLICATE) {
                        showViews()
                        warning(getString(R.string.duplicated))

                    }
                    else if (response.body()?.resultCode == NOT_ALLOWED) {
                        showViews()
                        warning(getString(R.string.password_must_contain))

                    }else {
                        showViews()
                        (activity as MainActivity).customToastTwo(getString(R.string.unknown_error), true)
                    }

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

    //show and hide option for entering password
    private fun updatePasswordVisibility(editText: EditText) {
        if (editText.transformationMethod is PasswordTransformationMethod) {
            editText.transformationMethod = null
            registration_password_toggle.setImageResource(R.drawable.ic_baseline_remove_red_eye_24)
        } else {
            editText.transformationMethod = PasswordTransformationMethod()
            registration_password_toggle.setImageResource(R.drawable.ic_baseline_visibility_off_24)
        }
        editText.setSelection(editText.length())
    }

    private fun showViews() {
        registration_progressbar.visibility = View.GONE
        text_view_registration.visibility = View.VISIBLE
        edit_text_registration_field_phone_number.visibility = View.VISIBLE
        edit_text_registration_field_password.visibility = View.VISIBLE
        registration_radio_button.visibility = View.VISIBLE
        registration_password_toggle.visibility = View.VISIBLE
        registration_next_button.visibility = View.VISIBLE
        if_want_to_verify_sms.visibility = View.VISIBLE
    }

    private fun hideViews() {
        registration_progressbar.visibility = View.VISIBLE
        text_view_registration.visibility = View.GONE
        edit_text_registration_field_phone_number.visibility = View.GONE
        edit_text_registration_field_password.visibility = View.GONE
        registration_radio_button.visibility = View.GONE
        registration_password_toggle.visibility = View.GONE
        registration_next_button.visibility = View.GONE
        registration_warning.visibility = View.GONE
        if_want_to_verify_sms.visibility = View.GONE
    }

    private fun warning(message: String) {
        registration_warning.visibility = View.VISIBLE
        registration_warning_text.text = message
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
