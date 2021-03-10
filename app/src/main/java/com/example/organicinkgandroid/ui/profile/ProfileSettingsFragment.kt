//profile settings
package com.example.organicinkgandroid.ui.profile

import android.app.Activity
import android.content.ClipData
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.organicinkgandroid.MainActivity
import com.example.organicinkgandroid.R
import com.example.organicinkgandroid.model.network.client.ClientRequestResponse
import com.example.organicinkgandroid.model.network.orders.com.example.organicinkgandroid.model.network.client.Info
import com.example.organicinkgandroid.retrofit.ApiClient
import com.example.organicinkgandroid.retrofit.SessionManager
import kotlinx.android.synthetic.main.fragment_profile_settings.*
import kotlinx.android.synthetic.main.fragment_profile_settings.view.*
import kotlinx.android.synthetic.main.fragment_profile_settings.view.back_button
import kotlinx.android.synthetic.main.fragment_profile_settings.view.edit_text_change_name
import kotlinx.android.synthetic.main.fragment_profile_settings.view.edit_text_lastname
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ProfileSettingsFragment: Fragment() {
    private val TAG = "ProfileSettingsFragment"
    private lateinit var sessionManager: SessionManager
    private var isAuthorized = false
    lateinit var navController : NavController
    lateinit var token : String
    var bundle = this.arguments
    lateinit var progressBar: ProgressBar

    var photo: String =""
    private lateinit var apiClient: ApiClient
    companion object {
        private const val IMAGE_PICK_CODE = 1000
        private const val PERMISSION_CODE = 1001
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView =
            LayoutInflater.from(context).inflate(R.layout.fragment_profile_settings, null)
        rootView.back_button.setOnClickListener {
            activity?.onBackPressed()
        }
        apiClient = ApiClient()
        sessionManager = SessionManager(requireContext())
        token = sessionManager.fetchAccessToken().toString()
        isAuthorized = token != "null"
        progressBar = rootView.profile_settings_progress_bar
        progressBar.visibility = View.VISIBLE
        Log.e("name", sessionManager.fetchUserName().toString())
        if (sessionManager.fetchUserName().toString() != "null") {
            setView(rootView)
        }
        else {
            progressBar.visibility = View.GONE
            rootView.profile_setting_layout.visibility = View.VISIBLE


        }
        rootView.text_view_tap.setOnClickListener {
            requestPermission()
        }
        if (sessionManager.fetchImageUri() != "null") {
            context?.let {
                photo = sessionManager.fetchImageUri()
                Glide.with(it)
                    .load(sessionManager.fetchImageUri().toString())
                    .placeholder(R.drawable.ic_profile_settings_image)
                    .circleCrop()
                    .into(rootView.image_view_profile)
            }
        }
        val saveButton = rootView.findViewById<TextView>(R.id.profile_settings_save_button)
        saveButton.setOnClickListener {
            var name = rootView.edit_text_change_name.text.toString()
            var lastName = rootView.edit_text_lastname.text.toString()
            var middleName = rootView.edit_text_change_middle_name.text.toString()
            var email = rootView.edit_text_enter_email.text.toString()

            if (validCheck(name, lastName, middleName, email)) {
                sessionManager.fetchUserId()
                    ?.let { it1 -> updateInfo(it1, Info(email, name, lastName, middleName))
                        rootView.profile_setting_layout.visibility = View.GONE
                        progressBar.visibility = View.VISIBLE}
                Log.e("This", name + lastName + middleName + email)
            }
            else{
                (activity as MainActivity).customToast("Hе все поля заполнены",false)
            }
        }

            return rootView
        }

        private fun requestPermission() {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        android.Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                ) {
                }
                else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_CODE
                    )
                    pickImageFromGallery()
                }
            } else {
                pickImageFromGallery()
            }

        }

    private fun pickImageFromGallery() {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            startActivityForResult(Intent.createChooser(intent,"Добавить фото"), IMAGE_PICK_CODE)
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            try {
                val clipData: ClipData = data?.clipData!!
                for (i in 0 until clipData.itemCount) {
                    val item = clipData.getItemAt(i)
                    val uri: Uri = item.uri
                }
            } catch (e: NullPointerException) {
                Log.e(TAG, "catch: $e")
                val path: String = File(data?.data?.path!!).absolutePath;

                 photo = data.data!!.toString()
                Glide.with(requireContext())
                    .load(photo)
                    .circleCrop()
                    .into(image_view_profile)
            }
        }
    }


    private fun updateInfo(id: Int, info: Info) {
        val call =  apiClient.getApiService().updateClientInfo(sessionManager.fetchAccessToken().toString(), id, info)
        call.enqueue(object : retrofit2.Callback<ClientRequestResponse> {
            override fun onFailure(call: Call<ClientRequestResponse>, t: Throwable) {
                (activity as MainActivity).customToast(getString(R.string.unknown_error), true)
                Log.e("ErrorUpdate", " ${t.message}")
            }

            override fun onResponse(
                call: Call<ClientRequestResponse>,
                response: Response<ClientRequestResponse>
            ) {
                if (response.isSuccessful) {
                    val user = response.body()?.result
                    if (user != null) {
                        sessionManager.deleteInfo()
                        sessionManager.saveUserInfo(user.email, user.firstName, user.lastName, user.middleName)
                        if (photo.isNotEmpty()) {
                            sessionManager.saveProfileImage(photo)
                        }
                    }
                    requireActivity().onBackPressed()
                }
                else{
                    (activity as MainActivity).customToast("Произошла ошибка, попробуйте еще раз",true)
                }
            }

        })

    }

    private fun validCheck(name: String, lastName: String, middleName: String, email: String) : Boolean{
            return !(name.isEmpty() || lastName.isEmpty())

    }


    private fun setView(rootView: View) {
//        if (!sessionManager.fetchUserName().equals("null")) {
//            rootView.edit_text_change_name.setText(sessionManager.fetchUserName())
//            rootView.edit_text_lastname.setText(sessionManager.fetchUserLastName())
//            rootView.edit_text_change_middle_name.setText(sessionManager.fetchUserMiddleName())
//            rootView.edit_text_enter_email.setText(sessionManager.fetchUserEmail())
//        }
        val call = sessionManager.fetchUserId()?.let {
            apiClient.getApiService().getClientById(
                it
            ).enqueue(object : Callback<ClientRequestResponse> {
                override fun onFailure(call: Call<ClientRequestResponse>, t: Throwable) {
                    Log.e("failed to retrieve", t.message.toString())
                }

                override fun onResponse(call: Call<ClientRequestResponse>, response: Response<ClientRequestResponse>) {
                    progressBar.visibility = View.GONE
                    rootView.profile_setting_layout.visibility = View.VISIBLE
                        // Log.e("onresponse", response.body()!!.result.phoneNumber)
                    if (response.isSuccessful) {
                        val user = response.body()!!.result
                            rootView.edit_text_change_name.setText(user.firstName)
                            rootView.edit_text_lastname.setText(user.lastName)
                            rootView.edit_text_change_middle_name.setText(user.middleName)
                            rootView.edit_text_enter_email.setText(user.email)
                        }
                    else {
                        (activity as MainActivity).customToast(getString(R.string.unknown_error),true)
                    }
                }
            })
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }
}