package com.example.organicinkgpublic.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.marginEnd
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.organicinkgpublic.R
import com.example.organicinkgpublic.database.basket.ProductEntity
import kotlinx.android.synthetic.main.basket_list_item.view.*
import java.lang.NullPointerException

class BasketAdapter(
    private val listener: OnItemClickListener, val context: Context
) :
    RecyclerView.Adapter<BasketAdapter.BasketViewHolder>(){

    private var basketItem = emptyList<ProductEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasketViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.basket_list_item, parent, false)

        return BasketViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: BasketViewHolder, position: Int) {
        val currentItem = basketItem[position]

        val input: String = currentItem.productImage

        try {

            if (input.contains("&")) {
                val result: List<String> = input.split("&")
                Glide.with(context)
                    .load(result[0])
                    //.placeholder(R.drawable.placeholder_image)
                    .dontAnimate()
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade(400))
                    .into(holder.itemImage)

            } else {

                if(input.length > 5) {
                    Glide.with(context)
                        .load(input)
                        //.placeholder(R.drawable.placeholder_image)
                        .dontAnimate()
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade(400))
                        .into(holder.itemImage)
                } else {
                    holder.itemImage.setImageResource(R.drawable.placeholder_image)
                }


            }
        } catch (e: IndexOutOfBoundsException) {
            Log.e("BasketAdapter", "onBindViewHolder: IndexOutOfBoundsException")
            holder.itemImage.setImageResource(R.drawable.placeholder_image)
        }


        holder.itemTitle.text = currentItem.productName
        holder.itemLocation.text = currentItem.productProductionPlace

        holder.itemPrice.text = currentItem.productPrice.toInt().toString()

        holder.itemCurrency.text = " ${currentItem.productCurrency}"
        holder.itemMeasureUnit.text = "/${currentItem.productMeasureUnit}"


        if (currentItem.productRating == null || currentItem.productRating == -1.0f){
            holder.itemRating.text = ""
            holder.itemRating.marginEnd.plus(5)
        }else {
            holder.itemRating.text = currentItem.productRating.toString()
        }

        try {
            when(currentItem.productRating!!) {
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
                in 0.0..1.99 -> {
                    holder.itemRatingImage.setImageResource(R.drawable.ic_star_one)
                }
                else -> {
                    holder.itemRatingImage.setImageResource(R.drawable.ic_star_zero)
                }
            }
        } catch (e: NullPointerException) {
            holder.itemRatingImage.setImageResource(R.drawable.ic_star_zero)
        }


        holder.itemIsChecked.isChecked = currentItem.isCheckedToBeOrdered == 1


        holder.itemQuantityToBeOrdered.text = currentItem.productQuantityToOrder.toString()

        // Added button listeners
        holder.deleteButton.setOnClickListener{
            listener.onDeleteClick(holder.itemView.rootView,position)
        }
        holder.plusButton.setOnClickListener{
            listener.onPlusClick(holder.itemView.rootView,position)
        }
        holder.minusButton.setOnClickListener{
            listener.onMinusClick(holder.itemView.rootView,position)
        }
        holder.checkBox.setOnClickListener{
            listener.onCheckBoxClick(holder.itemView.rootView,position)
        }

    }

    override fun getItemCount() = basketItem.size

    internal fun setBasketProducts(basketProducts: List<ProductEntity>) {
        this.basketItem = basketProducts
    }

    fun getProductAt(position: Int): ProductEntity {
        return basketItem[position]
    }

    //**********************************************************************************************

    inner class BasketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        val itemImage: ImageView = itemView.basket_product_image
        val itemTitle: TextView = itemView.basket_product_title
        val itemLocation: TextView = itemView.basket_product_location
        val itemPrice: TextView = itemView.basket_product_price

        val itemRating: TextView = itemView.basket_product_rating
        val itemRatingImage: ImageView = itemView.basket_product_rating_image

        val itemIsChecked: CheckBox = itemView.basket_product_is_checked
        val itemQuantityToBeOrdered: TextView = itemView.basket_product_quantity_to_be_ordered

        val deleteButton: ImageView = itemView.basket_product_delete
        val plusButton: ImageButton = itemView.basket_product_add_one
        val minusButton: ImageButton = itemView.basket_product_subtract_one
        val checkBox: CheckBox = itemView.basket_product_is_checked

        val itemCurrency: TextView = itemView.basket_product_price_currency
        val itemMeasureUnit: TextView = itemView.basket_product_measure_unit


        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            //val position = bindingAdapterPosition
            listener.onItemClick(v,this.bindingAdapterPosition)

        }

    }

    interface OnItemClickListener {
        fun onItemClick(v: View?,position: Int)
        fun onDeleteClick(v: View?, position: Int)
        fun onPlusClick(v: View?, position: Int)
        fun onMinusClick(v: View?, position: Int)
        fun onCheckBoxClick(v: View?, position: Int)
    }

}