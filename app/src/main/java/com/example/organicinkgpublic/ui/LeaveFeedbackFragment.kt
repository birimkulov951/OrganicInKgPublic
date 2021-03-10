package com.example.organicinkgpublic.ui
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.RatingBar
import androidx.fragment.app.Fragment
import com.example.organicinkgpublic.MainActivity
import com.example.organicinkgpublic.R
import com.example.organicinkgpublic.model.network.raiting.RatingCreate
import com.example.organicinkgpublic.model.network.orders.com.example.organicinkgandroid.model.network.raiting.RatingResponse
import com.example.organicinkgpublic.model.network.raiting.feedbacks.Feedback
import com.example.organicinkgpublic.model.network.orders.com.example.organicinkgandroid.model.network.raiting.feedbacks.FeedbackCreate
import com.example.organicinkgpublic.model.network.orders.com.example.organicinkgandroid.model.network.raiting.feedbacks.FeedbackResponse
import com.example.organicinkgpublic.retrofit.ApiClient
import com.example.organicinkgpublic.retrofit.SessionManager
import com.example.organicinkgpublic.utils.Constants
import com.example.organicinkgpublic.utils.Constants.DUPLICATE
import kotlinx.android.synthetic.main.fragment_leave_feedback.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LeaveFeedbackFragment : Fragment()  {
lateinit var sessionManager: SessionManager
    lateinit var apiClient: ApiClient
    lateinit var feedback: Feedback
    lateinit var ratingBar: RatingBar
    lateinit var token: String

    private var productId = -1
    private var productName = "null"
    private var productProductionPlace = "null"
    private var productImages = "null"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_leave_feedback, container, false)
        sessionManager = SessionManager(requireContext())
        apiClient = ApiClient()
        ratingBar = rootView.rating
        token = sessionManager.fetchAccessToken().toString()

        productId = requireArguments().getInt(Constants.PRODUCT_ID, -1)
        productName = requireArguments().getString(Constants.PRODUCT_NAME, "null")
        productProductionPlace = requireArguments().getString(Constants.PRODUCT_PRODUCTION_PLACE, "null")
        productImages = requireArguments().getString(Constants.PRODUCT_IMAGES, "null")

        rootView.feedback_product_name.text = productName
        rootView.feedback_product_location.text = productProductionPlace


        ratingBar.setOnRatingBarChangeListener { ratingBar, rating, _ ->
            var rate = ratingBar.rating.toInt()
                Log.e("rating : ", rate.toString())
                    val call = apiClient.getApiService().putRating(RatingCreate(sessionManager.fetchUserId()!!, productId, rate))


            call.enqueue(object: Callback<RatingResponse>{
                override fun onFailure(call: Call<RatingResponse>, t: Throwable) {
                    Log.e("rating", t.message.toString())
                }
                override fun onResponse(
                    call: Call<RatingResponse>,
                    response: Response<RatingResponse>
                ) {
                    if (response.isSuccessful && response.body()!!.resultCode != DUPLICATE){
                        ratingBar.invalidate()
                        ratingBar.setIsIndicator(true)
                    }
                    else if (response.body()!!.resultCode == DUPLICATE){
                        ratingBar.isEnabled = false
                        (activity as MainActivity).customToast("Вы уже оценили этот товар",false)

                    }
                    else{
                        Log.e("failed", "lol")
                    }
                }

            })
        }

        rootView.leave_feedback_button.setOnClickListener {
            val comment = rootView.leave_feedback_edit_text.text.toString()
            if (comment.isNotEmpty()) {
                hideKeyBoard()
                leaveFeedback(comment)
            }
        }
        rootView.leave_feedback_back_button.setOnClickListener {
            activity?.onBackPressed()
        }
        return rootView

    }

    private fun leaveFeedback(comment: String) {
        var call = apiClient.getApiService().leaveFeedback("Bearer $token",
            FeedbackCreate(
                comment,
                productId
            )
        )
        call.enqueue(object: retrofit2.Callback<FeedbackResponse> {
            override fun onFailure(call: Call<FeedbackResponse>, t: Throwable) {
                Log.e("feedback", t.message.toString())
            }
            override fun onResponse(
                call: Call<FeedbackResponse>,
                response: Response<FeedbackResponse>
            ) {
                if (response.isSuccessful){
                    feedback = response.body()!!.result
                    (activity as MainActivity).customToast("Спасибо!",false)
                    requireActivity().onBackPressed()
                    Log.e("comment: ", feedback.comment)
                }
                else {
                    (activity as MainActivity).customToast(getString(R.string.unknown_error),false)
                    requireActivity().onBackPressed()
                }
            }
        })
    }
    private fun hideKeyBoard() {
        val view = activity?.currentFocus
        if (view != null) {
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm!!.hideSoftInputFromWindow(requireView().windowToken, 0)
        }
        view?.leave_feedback_edit_text?.setText("")
    }
    }