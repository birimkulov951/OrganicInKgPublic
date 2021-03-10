package com.example.organicinkgpublic.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.organicinkgpublic.R
import com.example.organicinkgpublic.model.network.aboutus.AboutUsResponse
import kotlinx.android.synthetic.main.about_us_item.view.*

class AboutUsAdapter(private var aboutUs : ArrayList<AboutUsResponse>) : RecyclerView.Adapter<AboutUsAdapter.AboutUsViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AboutUsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.about_us_item, parent,false)
        return AboutUsViewHolder(
            itemView
        )
    }

    override fun getItemCount(): Int {
        return aboutUs.size

    }
    fun setInfo(about: ArrayList<AboutUsResponse>){
        aboutUs = about

    }

    override fun onBindViewHolder(holder: AboutUsViewHolder, position: Int) {
        val currentItem = aboutUs[position]
        holder.header.text = currentItem.header
        holder.paragraph.text = currentItem.paragraph

    }

    class AboutUsViewHolder(itemView : View): RecyclerView.ViewHolder(itemView) {
        var header = itemView.about_us_item_header
        var paragraph = itemView.about_us_item_paragraph




    }


}