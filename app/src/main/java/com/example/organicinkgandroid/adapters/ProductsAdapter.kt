package com.example.organicinkgandroid.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.marginEnd
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.organicinkgandroid.MainActivity
import com.example.organicinkgandroid.R
import com.example.organicinkgandroid.model.network.product.Product
import kotlinx.android.synthetic.main.grid_view_item.view.*
import java.lang.NullPointerException

class ProductsAdapter(
    private var context: Context,
    private val listener: OnItemClickListener
    ) : RecyclerView.Adapter<ProductsAdapter.ProductViewHolder>(){

     private var products = ArrayList<Product>()

    private var positionOfAnimatedButton = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.grid_view_item, parent, false)

        return ProductViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val currentItem = products[position]

        try {
            Glide.with(context)
                .load(currentItem.productImages[0].imageUrl)
                //.placeholder(R.drawable.placeholder_image)
                .centerCrop()
                .dontAnimate()
                .transition(DrawableTransitionOptions.withCrossFade(400))
                .into(holder.itemImage)
        } catch (e: IndexOutOfBoundsException) {
            Log.e("ProductsAdapter", "onBindViewHolder: No image position $position")
            holder.itemImage.setImageResource(R.drawable.placeholder_image)
        }

        holder.itemTitle.text = currentItem.name
        holder.itemLocation.text = currentItem.supplier.placeOfProduction.region


        // To change currencies to "cом"
        if (currentItem.currency.equals("SOM")){
            currentItem.currency = "cом"
            holder.itemCurrentcy.text = " cом"
        } else if(currentItem.currency == "USD"){
            currentItem.currency = "дол"
            holder.itemCurrentcy.text = " дол"
        }

        holder.itemMeasureUnit.text = "/${currentItem.measureUnitResponse.name}"

        holder.itemPrice.text = currentItem.price.toInt().toString()

        if (currentItem.rating == null){
            holder.itemRating.text = ""
            holder.itemRating.marginEnd.plus(5)
        }else {
            holder.itemRating.text = currentItem.rating.toString()
        }


        if((context as MainActivity).productsIdInBasket.contains(currentItem.id)) {
            holder.addButton.setBackgroundResource(R.drawable.rounded_add_background_in_basket)
        } else {
            holder.addButton.setBackgroundResource(R.drawable.rounded_add_background)
        }

        // To animate clicked AddButton
        if (positionOfAnimatedButton > -1 && position == positionOfAnimatedButton) {
            val alphaAnim2 = AnimationUtils.loadAnimation(context, R.anim.alpha_anim_2)
            holder.addButton.startAnimation(alphaAnim2)
        }


        holder.addButton.setOnClickListener{
            listener.onAddItemToBasket(holder.itemView.rootView,position,getItemId(position))
        }


        try {
            when(currentItem.rating!!) {
                in 4.6..5.0 -> {
                    holder.itemRatingImage.setImageResource(R.drawable.ic_star_five)
                }
                in 4.0..4.59 -> {
                    holder.itemRatingImage.setImageResource(R.drawable.ic_star_four)
                }
                in 3.0..3.99 -> {
                    holder.itemRatingImage.setImageResource(R.drawable.ic_star_three)
                }
                in 2.0..2.99 -> {
                    holder.itemRatingImage.setImageResource(R.drawable.ic_star_two)
                }
                in 0.0..1.99 ->{
                    holder.itemRatingImage.setImageResource(R.drawable.ic_star_one)
                }
                else -> {
                    holder.itemRatingImage.setImageResource(R.drawable.ic_star_zero)
                }
            }

        } catch (e: NullPointerException) {
            holder.itemRatingImage.setImageResource(R.drawable.ic_star_zero)
        }


    }

    override fun getItemCount() = products.size

    fun setItems(products : ArrayList<Product>) {
        this.products = products
    }
    fun loadMore(newData: ArrayList<Product>){
        products.addAll(newData)
        notifyDataSetChanged()
    }

    fun getProductAt(position: Int) : Product {
        return products[position]
    }

    fun getPositionOfAnimatedButton(position: Int) {
        positionOfAnimatedButton = position
    }

    //**********************************************************************************************

    inner class ProductViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener{

        val itemImage: ImageView = itemView.image_view_item_image
        val itemTitle: TextView = itemView.grid_view_item_name
        val itemLocation: TextView = itemView.grid_view_item_location
        val itemPrice: TextView = itemView.grid_view_item_price
        val itemCurrentcy = itemView.grid_view_item_currency
        val itemMeasureUnit = itemView.grid_view_item_measure_unit

        val itemRating: TextView = itemView.grid_view_item_rating
        val itemRatingImage: ImageView = itemView.grid_view_item_rating_icon

        val addButton: ImageButton = itemView.image_view_add_item_to_basket

        init{
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.onItemClick(v,this.bindingAdapterPosition, itemId)
        }

    }

    interface OnItemClickListener {
        fun onItemClick( v: View?, position : Int, id: Long)
        fun onAddItemToBasket(v: View?, position: Int, id: Long)
    }

}