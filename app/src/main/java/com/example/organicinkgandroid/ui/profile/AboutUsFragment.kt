package com.example.organicinkgandroid.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.organicinkgandroid.adapters.AboutUsAdapter
import com.example.organicinkgandroid.R
import com.example.organicinkgandroid.model.network.aboutus.AboutUsResponse
import com.example.organicinkgandroid.retrofit.ApiClient
import com.example.organicinkgandroid.retrofit.SessionManager
import kotlinx.android.synthetic.main.fragment_about_us.*
import kotlinx.android.synthetic.main.fragment_about_us.view.*
import retrofit2.Call
import retrofit2.Response

class AboutUsFragment : Fragment() {

    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient
    private var aboutUs: ArrayList<AboutUsResponse> = ArrayList()
    private lateinit  var adapter: AboutUsAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_about_us, container, false)
        rootView.about_us_back_button.setOnClickListener {
            requireActivity().onBackPressed()
        }


        apiClient = ApiClient()
        sessionManager = SessionManager(requireContext())
        getAboutUs()





        return rootView

    }

    private fun getAboutUs() {
        val call = apiClient.getApiService().getAboutUsInfo().enqueue(object:
            retrofit2.Callback<ArrayList<AboutUsResponse>> {
            override fun onFailure(call: Call<ArrayList<AboutUsResponse>>, t: Throwable) {
                Log.e("AboutUs", t.message.toString())
            }

            override fun onResponse(
                call: Call<ArrayList<AboutUsResponse>>,
                response: Response<ArrayList<AboutUsResponse>>
            ) {
                if (response.isSuccessful && response.body()!!.isNotEmpty()){
                    aboutUs = response.body()!!
                    adapter =
                        AboutUsAdapter(
                            aboutUs
                        )
                    adapter.setInfo(aboutUs)
                    about_us_recycler_view.adapter = adapter
                }
                else{
                    about_us_scroll_view.visibility = View.VISIBLE
                }
            }

        })
    }


}