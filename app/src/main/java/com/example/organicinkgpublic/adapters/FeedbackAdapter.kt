package com.example.organicinkgpublic.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.organicinkgpublic.R
import com.example.organicinkgpublic.model.network.raiting.feedbacks.Feedback
import kotlinx.android.synthetic.main.feedback_list_item.view.*

class FeedbackAdapter (private val feedbacks : List<Feedback>)
    : RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder>(){


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FeedbackViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.feedback_list_item, parent, false)
        return FeedbackViewHolder(itemView)
    }


    override fun getItemCount(): Int {
        return feedbacks.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: FeedbackAdapter.FeedbackViewHolder, position: Int) {
        val currentFeedback = feedbacks[position]

        if(currentFeedback.client.contains("null")) {
            holder.name.text = "Анонимный клиент"
        } else {
            holder.name.text = currentFeedback.client
        }
        //holder.date.text = currentFeedback.date
        holder.text.text = currentFeedback.comment

        holder.image.setImageResource(R.drawable.ic_profile_image)
        //holder.num.text = currentFeedback.rating.toString()
//        Glide.with(holder.itemView.context)
//            .load(currentFeedback.ratingImageUrl)
//            .into(holder.star)
//        holder.star.setImageResource(R.drawable.ic_star_three)
   }

    class FeedbackViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView){
        var name = itemView.feedback_user_name
       // var date = itemView.feedback_date
        var image = itemView.feedback_image
        var text = itemView.feedback_text
//        var num = itemView.feedback_num_rating
//        var star = itemView.feedback_image_rating
    }

}