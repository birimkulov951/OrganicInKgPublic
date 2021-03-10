//profile
package com.example.organicinkgpublic.ui.profile

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.cardview.widget.CardView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.organicinkgpublic.MainActivity
import com.example.organicinkgpublic.R
import com.example.organicinkgpublic.model.network.client.ClientRequestResponse
import com.example.organicinkgpublic.retrofit.ApiClient
import com.example.organicinkgpublic.retrofit.SessionManager
import kotlinx.android.synthetic.main.exit_alert_dialog.view.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProfileFragment : Fragment(), View.OnClickListener {

    private val TAG = "ProfileFragment"

    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient

    lateinit var navController : NavController

    private var isAuthorized = false
    private lateinit var progressBar: ProgressBar
    var bundle: Bundle = Bundle()


    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        // to fix toast bug
        (activity as MainActivity).hideBasketToast()
        (activity as MainActivity).showBottomNavBar("",false)

        val rootView: View = inflater.inflate(R.layout.fragment_profile, container, false)
        apiClient = ApiClient()



        // to open authorization fragment if user is authorized
        sessionManager = SessionManager(requireContext())
        //Log.e("token", sessionManager.fetchAccessToken().toString())
        progressBar = rootView.progress_bar_user_name


        if (sessionManager.fetchImageUri() != "null") {
            Glide.with(rootView)
                .load(sessionManager.fetchImageUri())
                .circleCrop()
                .placeholder(R.drawable.ic_profile_image)
                .into(rootView.image_view_profile)
        }
        isAuthorized = sessionManager.fetchAccessToken() != "null"
        Log.e("isAuthorized:", isAuthorized.toString())



        val exitCardView = rootView.findViewById<CardView>(R.id.profile_fragment_card_view_exit)


        if (!isAuthorized) {

            rootView.profile_fragment_text_view_exit.text = getString(R.string.login)
            rootView.profile_fragment_text_view_exit.setTextColor(Color.parseColor("#009B00"))
            rootView.text_view_user_name.text = "ФИО"
            progressBar.visibility = View.GONE

        }
        exitCardView.setOnClickListener {

            if (!isAuthorized) {

                navController.navigate(R.id.action_profileFragment_to_registrationFragment)

            } else {

                //rootView.profile_fragment_text_view_exit.setTextColor(Color.parseColor("#FF0303"))

                //rootView.image_view_exit_arrow.visibility = View.GONE
                val alertDialog = LayoutInflater.from(rootView.context).inflate(R.layout.exit_alert_dialog, null)
                val builder = AlertDialog.Builder(rootView.context)
                    .setView(alertDialog)
                    .show()
                alertDialog.stay_button.setOnClickListener {
                    builder.dismiss()
                    //rootView.profile_fragment_text_view_exit.setTextColor(Color.parseColor("#000000"))
                }
                alertDialog.exit_button.setOnClickListener {

                    sessionManager.saveAuthTokens(0,"null","null","null","null")
                    sessionManager.savePhoneNumber("null")
                    sessionManager.deleteInfo()

                    navController.navigate(R.id.action_profileFragment_to_registrationFragment)

                    builder.dismiss()

                }

            }

        }




        return rootView
    }

    private fun getUserName(view: View) {
        val call = sessionManager.fetchUserId()?.let {
            apiClient.getApiService().getClientById(
                it
            ).enqueue(object : Callback<ClientRequestResponse>{
                override fun onFailure(call: Call<ClientRequestResponse>, t: Throwable) {
                    Log.e("error", t.message.toString())
                }

                override fun onResponse(call: Call<ClientRequestResponse>, response: Response<ClientRequestResponse>) {
                    if (response.isSuccessful && response.body()!!.result.firstName.toString() != "null") {
                      val user = response.body()!!.result
                       // Log.e("user: ", user.firstName.toString())
                        sessionManager.saveUserInfo(
                            user.email,
                            user.firstName,
                            user.lastName,
                            user.middleName
                        )

                        Handler(Looper.getMainLooper()).postDelayed({
                            progressBar.visibility = View.GONE
                            view.text_view_user_name.visibility = View.VISIBLE
                            view.text_view_user_name.text = "${user.firstName} ${user.lastName}"

                        }, 150)

                    }
                    else {
                        progressBar.visibility = View.GONE
                        view.text_view_user_name.visibility = View.VISIBLE
                        view.text_view_user_name.text = getString(R.string.navigate_to_settings)
                    }
                }

                }
              )
        }
    }


    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.image_view_settings -> navController.navigate(R.id.action_profileFragment_to_profileSettingsFragment, bundle)
            R.id.card_view_current_order -> navController.navigate(R.id.action_profileFragment_to_currentOrdersFilledFragment)
            R.id.card_view_history -> navController.navigate(R.id.action_profileFragment_to_historyFragment)
            R.id.card_view_faq -> navController.navigate(R.id.action_profileFragment_to_faqFragment)
            R.id.card_view_contacts -> navController.navigate(R.id.action_profileFragment_to_contactsFragment)
            R.id.card_view_about_us -> navController.navigate(R.id.action_profileFragment_to_aboutUsFragment)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        if (isAuthorized){
            view.findViewById<ImageView>(R.id.image_view_settings).setOnClickListener(this)
        }
        view.findViewById<CardView>(R.id.card_view_current_order).setOnClickListener(this)
        view.findViewById<CardView>(R.id.card_view_history).setOnClickListener(this)
        view.findViewById<CardView>(R.id.card_view_faq).setOnClickListener(this)
        view.findViewById<CardView>(R.id.card_view_contacts).setOnClickListener(this)
        view.findViewById<CardView>(R.id.card_view_about_us).setOnClickListener(this)

        if (isAuthorized) {
            bundle.putInt("id", sessionManager.fetchUserId()!!)
            Log.e("name", sessionManager.fetchUserName().toString())
            if (sessionManager.fetchUserName() != "null") {
                Log.e("me" , "meeee")
                progressBar.visibility = View.GONE
                view.text_view_user_name.text = "${sessionManager.fetchUserName()} ${sessionManager.fetchUserLastName()}"
                view.text_view_user_name.textSize = 18.0f
            }
            else if (sessionManager.fetchUserName() == "null"){
                text_view_user_name.visibility = View.GONE
                getUserName(view)

                Log.e("me" , "no meeeee")
            }
            else{
                progressBar.visibility = View.GONE
                view.text_view_user_name.visibility = View.VISIBLE
                view.text_view_user_name.text = getString(R.string.navigate_to_settings)
            }

        }

    }


}