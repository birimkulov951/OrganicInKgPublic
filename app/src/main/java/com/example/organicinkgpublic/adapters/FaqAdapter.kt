package com.example.organicinkgpublic.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.organicinkgpublic.R
import com.example.organicinkgpublic.model.network.faq.Faq
import kotlinx.android.synthetic.main.faq_list_item.view.*

class FaqAdapter (
    private val listener: OnItemClickListener
)
    : RecyclerView.Adapter<FaqAdapter.FaqViewHolder>(){

    private var questionItem = emptyList<Faq>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FaqViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.faq_list_item, parent, false)
        
        return FaqViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FaqViewHolder, position: Int) {
        val currentQuestion = questionItem[position]
        holder.question.text = currentQuestion.question
        holder.answer.text = currentQuestion.answer
    }

    override fun getItemCount(): Int {
        return questionItem.size
    }

    internal fun setQuestionItems(questions: ArrayList<Faq>) {
        this.questionItem = questions
    }

    inner class FaqViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener{
        
        val question: TextView = itemView.faq_question
        val answer = itemView.faq_answer

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {

            listener.onItemClick(v,this.bindingAdapterPosition)

        }


    }

    interface OnItemClickListener {

        fun onItemClick(v: View?,position: Int)

    }


}