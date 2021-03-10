package com.example.organicinkgpublic.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.organicinkgpublic.R

class ImageSliderAdapter(private val slides : List<String>,  val context: Context)
    : RecyclerView.Adapter<ImageSliderAdapter.ImageSliderViewHolder>(){


    inner class ImageSliderViewHolder(view : View) : RecyclerView.ViewHolder(view){
        private val image = view.findViewById<ImageView>(R.id.image_slide)

        fun bind(slider : String){

            try {
                Glide.with(context)
                    .load(slider)
                    //.placeholder(R.drawable.placeholder_image)
                    .dontAnimate()
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade(400))
                    .into(image)
            } catch (e: Exception) {
                Log.e("ImageSliderAdapter", "ImageSliderViewHolder: $e")
                image.setImageResource(R.drawable.placeholder_image)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageSliderViewHolder {
        return ImageSliderViewHolder(LayoutInflater.from(parent.context).
        inflate(R.layout.slide_image_container, parent, false))
    }

    override fun getItemCount(): Int {
        return slides.size
    }

    override fun onBindViewHolder(holder: ImageSliderViewHolder, position: Int) {
        holder.bind(slides[position])
    }
}