//faq
package com.example.organicinkgpublic.ui.profile

import android.graphics.Color
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.organicinkgpublic.R
import com.example.organicinkgpublic.adapters.FaqAdapter
import com.example.organicinkgpublic.model.network.faq.Faq
import com.example.organicinkgpublic.retrofit.ApiClient
import kotlinx.android.synthetic.main.fragment_faq.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FaqFragment : Fragment(),FaqAdapter.OnItemClickListener {
    private var listItems : ArrayList<Faq> =  ArrayList()
    lateinit var apiClient: ApiClient
    lateinit var adapter: FaqAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_faq, container, false)

//        listItems.add(Question("1.\tРегистрация для пользователей"))
//        listItems.add(Question("2.\tВыбор продукции, заказ и оплата"))
//        listItems.add(Question("3.\tКоличество товаров за один заказ"))
        apiClient = ApiClient()
        getFaqList(rootView)





        rootView.faq_back_button.setOnClickListener {
            requireActivity().onBackPressed()
        }

        return rootView

    }

    private fun getFaqList(view: View) {
        val call = apiClient.getApiService().getFaqList().enqueue(object:
            Callback<List<Faq>> {
            override fun onFailure(call: Call<List<Faq>>, t: Throwable) {
                Log.e("faq ", t.message.toString())
            }

            override fun onResponse(call: Call<List<Faq>>, response: Response<List<Faq>>) {
                if (response.isSuccessful){
                    listItems.clear()
                    listItems = response.body() as ArrayList<Faq>
                    updateUI(view)
                }
            }

        })
    }

    private fun updateUI(rootView: View) {
        adapter = FaqAdapter(this)
        rootView.faq_recycler_view.layoutManager = LinearLayoutManager(rootView.context)
        rootView.faq_recycler_view.adapter = adapter
        adapter.setQuestionItems(listItems)

    }

    override fun onItemClick(v: View?, position: Int) {

        val clickedItem: Faq = listItems[position]
        val cardViewInvisible = v?.findViewById<CardView>(R.id.faq_card_view_invisible)
        val arrowIcon = v?.findViewById<ImageView>(R.id.faq_icon_arrow)
        val answer = v?.findViewById<TextView>(R.id.faq_answer)


        val rotateRight = AnimationUtils.loadAnimation(activity, R.anim.rotate_right)
        val rotateLeft = AnimationUtils.loadAnimation(activity, R.anim.rotate_left)

        if (answer?.visibility == View.GONE){
        TransitionManager.beginDelayedTransition(cardViewInvisible, AutoTransition())
        arrowIcon?.startAnimation(rotateRight)
        arrowIcon?.setColorFilter(Color.parseColor("#000000"))
        answer?.visibility = View.VISIBLE
        }
        else{
            TransitionManager.beginDelayedTransition(cardViewInvisible, AutoTransition())
            arrowIcon?.setColorFilter(Color.parseColor("#8E8E8E"))
            arrowIcon?.startAnimation(rotateLeft)
            answer?.visibility = View.GONE
        }
    }
}
