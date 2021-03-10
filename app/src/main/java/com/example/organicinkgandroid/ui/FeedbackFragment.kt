package com.example.organicinkgandroid.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.organicinkgandroid.MainActivity
import com.example.organicinkgandroid.R
import com.example.organicinkgandroid.adapters.FeedbackAdapter
import com.example.organicinkgandroid.model.network.product.ProductResponse
import com.example.organicinkgandroid.model.network.raiting.feedbacks.Feedback
import com.example.organicinkgandroid.retrofit.ApiClient
import com.example.organicinkgandroid.utils.Constants.PRODUCT_ID
import kotlinx.android.synthetic.main.feedback_alert_dialog.view.*
import kotlinx.android.synthetic.main.fragment_feedback.view.*
import retrofit2.Call
import retrofit2.Response

class FeedbackFragment : Fragment(){
    private var feedbacks : ArrayList<Feedback> =  ArrayList()
    lateinit var apiClient: ApiClient
    private lateinit var feedbackProgressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_feedback, container, false)
        val bundle = this.arguments
        val productId = bundle?.getInt(PRODUCT_ID)
        feedbackProgressBar = rootView.feedback_progress_bar
        feedbackProgressBar.visibility = View.VISIBLE
        apiClient = ApiClient()
        if (productId != null) {
            getListOfFeedbacks(productId,rootView)
        }

        rootView.toolbar_back_button.setOnClickListener {
            (activity as MainActivity).onBackPressed()
        }
        rootView.button_leave_feedback.setOnClickListener {
            val alertDialog = LayoutInflater.from(rootView.context).inflate(R.layout.feedback_alert_dialog, null)
            val builder = AlertDialog.Builder(rootView.context)
                .setView(alertDialog)
                .show()
            alertDialog.feedback_alert_button.setOnClickListener{
                builder.dismiss()
            }
        }
        return rootView

    }

    private fun getListOfFeedbacks(productId: Int, rootView: View) {
        val call = apiClient.getApiService().getProductById(productId).enqueue(object:retrofit2.Callback<ProductResponse> {
            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                Log.e("feedback", t.message.toString())
            }

            override fun onResponse(
                call: Call<ProductResponse>,
                response: Response<ProductResponse>
            ) {
                feedbackProgressBar.visibility = View.GONE
                if (response.isSuccessful && response.body()!!.result.comments.size != 0){
                    feedbacks = response.body()!!.result.comments
                    updateUI(rootView)
                }
                else {
                    rootView.feedback_fragment_is_empty_text.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun updateUI(rootView: View) {
        rootView.feedback_recycler_view.adapter = FeedbackAdapter(feedbacks)
    }
}