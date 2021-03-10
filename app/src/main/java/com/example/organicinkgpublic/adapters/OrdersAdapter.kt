package com.example.organicinkgpublic.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.organicinkgpublic.R
import com.example.organicinkgpublic.model.ClosedOrder
import kotlinx.android.synthetic.main.order_list_item.view.*

class OrdersAdapter(
    private val clickListener: OnItemClickListener,
    private val context: Context
) : RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder>() {

    private var orderHistoryList = emptyList<ClosedOrder>()
    private var isFeedbacksAllowed = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.order_list_item, parent, false)
        return OrdersViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: OrdersViewHolder, position: Int) {
        val currentItem = orderHistoryList[position]

        val productCurrency = " ${currentItem.productCurrency}"
        holder.productCurrency1.text = productCurrency
        holder.productCurrency2.text = productCurrency
        holder.productCurrency3.text = productCurrency
        holder.productCurrency4.text = productCurrency
        holder.productCurrency5.text = productCurrency
        holder.productCurrency6.text = productCurrency
        holder.productCurrency7.text = productCurrency
        holder.productCurrency8.text = productCurrency
        holder.productCurrency9.text = productCurrency
        holder.productCurrency10.text = productCurrency


        if (isFeedbacksAllowed) {
            holder.productBtn1.visibility = View.VISIBLE
            holder.productBtn2.visibility = View.VISIBLE
            holder.productBtn3.visibility = View.VISIBLE
            holder.productBtn4.visibility = View.VISIBLE
            holder.productBtn5.visibility = View.VISIBLE
            holder.productBtn6.visibility = View.VISIBLE
            holder.productBtn7.visibility = View.VISIBLE
            holder.productBtn8.visibility = View.VISIBLE
            holder.productBtn9.visibility = View.VISIBLE
            holder.productBtn10.visibility = View.VISIBLE
        }

        if (currentItem.orderNumber != null) {
            holder.orderNumber.text = currentItem.orderNumber
        }

        if (currentItem.product1Name != null) {
            holder.productName1.text = currentItem.product1Name
            holder.productPrice1.text = currentItem.product1Price
            Glide.with(context).load(currentItem.product1ImageUrl).placeholder(R.drawable.placeholder_image).centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade(400)).into(holder.productImageUrl1)
            holder.productLocation1.text = currentItem.product1Location
        }

        if (currentItem.product2Name != null) {
            holder.productName2.text = currentItem.product2Name
            holder.productPrice2.text = currentItem.product2Price
            Glide.with(context).load(currentItem.product2ImageUrl).placeholder(R.drawable.placeholder_image).centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade(400)).into(holder.productImageUrl2)
            holder.productLocation2.text = currentItem.product2Location
        }

        if (currentItem.product3Name != null) {
            holder.productName3.text = currentItem.product3Name
            holder.productPrice3.text = currentItem.product3Price
            Glide.with(context).load(currentItem.product3ImageUrl).placeholder(R.drawable.placeholder_image).centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade(400)).into(holder.productImageUrl3)
            holder.productLocation3.text = currentItem.product3Location
        }

        if (currentItem.product4Name != null) {
            holder.productName4.text = currentItem.product4Name
            holder.productPrice4.text = currentItem.product4Price
            Glide.with(context).load(currentItem.product4ImageUrl).placeholder(R.drawable.placeholder_image).centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade(400)).into(holder.productImageUrl4)
            holder.productLocation4.text = currentItem.product4Location
        }

        if (currentItem.product5Name != null) {
            holder.productName5.text = currentItem.product5Name
            holder.productPrice5.text = currentItem.product5Price
            Glide.with(context).load(currentItem.product5ImageUrl).placeholder(R.drawable.placeholder_image).centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade(400)).into(holder.productImageUrl5)
            holder.productLocation5.text = currentItem.product5Location
        }

        if (currentItem.product6Name != null) {
            holder.productName6.text = currentItem.product6Name
            holder.productPrice6.text = currentItem.product6Price
            Glide.with(context).load(currentItem.product6ImageUrl).placeholder(R.drawable.placeholder_image).centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade(400)).into(holder.productImageUrl6)
            holder.productLocation6.text = currentItem.product6Location
        }

        if (currentItem.product7Name != null) {
            holder.productName7.text = currentItem.product7Name
            holder.productPrice7.text = currentItem.product7Price
            Glide.with(context).load(currentItem.product7ImageUrl).placeholder(R.drawable.placeholder_image).centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade(400)).into(holder.productImageUrl7)
            holder.productLocation7.text = currentItem.product7Location
        }

        if (currentItem.product8Name != null) {
            holder.productName8.text = currentItem.product8Name
            holder.productPrice8.text = currentItem.product8Price
            Glide.with(context).load(currentItem.product8ImageUrl).placeholder(R.drawable.placeholder_image).centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade(400)).into(holder.productImageUrl8)
            holder.productLocation8.text = currentItem.product8Location
        }

        if (currentItem.product9Name != null) {
            holder.productName9.text = currentItem.product9Name
            holder.productPrice9.text = currentItem.product9Price
            Glide.with(context).load(currentItem.product9ImageUrl).placeholder(R.drawable.placeholder_image).centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade(400)).into(holder.productImageUrl9)
            holder.productLocation9.text = currentItem.product9Location
        }

        if (currentItem.product10Name != null) {
            holder.productName10.text = currentItem.product10Name
            holder.productPrice10.text = currentItem.product10Price
            Glide.with(context).load(currentItem.product10ImageUrl).placeholder(R.drawable.placeholder_image).centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade(400)).into(holder.productImageUrl10)
            holder.productLocation10.text = currentItem.product10Location
        }

        holder.productBtn1.setOnClickListener{
            clickListener.onItemOne(position,holder.itemView.rootView)
        }

        holder.productBtn2.setOnClickListener{
            clickListener.onItemTwo(position,holder.itemView.rootView)
        }

        holder.productBtn3.setOnClickListener{
            clickListener.onItemThree(position,holder.itemView.rootView)
        }

        holder.productBtn4.setOnClickListener{
            clickListener.onItemFour(position,holder.itemView.rootView)
        }

        holder.productBtn5.setOnClickListener{
            clickListener.onItemFive(position,holder.itemView.rootView)
        }

        holder.productBtn6.setOnClickListener{
            clickListener.onItemSix(position,holder.itemView.rootView)
        }

        holder.productBtn7.setOnClickListener{
            clickListener.onItemSeven(position,holder.itemView.rootView)
        }

        holder.productBtn8.setOnClickListener{
            clickListener.onItemEight(position,holder.itemView.rootView)
        }

        holder.productBtn9.setOnClickListener{
            clickListener.onItemNine(position,holder.itemView.rootView)
        }

        holder.productBtn10.setOnClickListener{
            clickListener.onItemNine(position,holder.itemView.rootView)
        }

    }

    override fun getItemCount() = orderHistoryList.size


    internal fun setItems(products: List<ClosedOrder>) {
        this.orderHistoryList = products
    }

    internal fun allowFeedbacks() {
        isFeedbacksAllowed = true
    }

    //**********************************************************************************************


    inner class OrdersViewHolder (itemView: View): RecyclerView.ViewHolder(itemView),
        View.OnClickListener{

        val orderNumber: TextView = itemView.current_orders_order_number_text

        val productName1: TextView = itemView.current_orders_product_name_1
        val productPrice1: TextView = itemView.current_orders_product_price_1
        val productImageUrl1: ImageView = itemView.current_orders_image_1
        val productLocation1: TextView = itemView.current_orders_product_location_1

        val productName2: TextView = itemView.current_orders_product_name_2
        val productPrice2: TextView = itemView.current_orders_product_price_2
        val productImageUrl2: ImageView = itemView.current_orders_image_2
        val productLocation2: TextView = itemView.current_orders_product_location_2

        val productName3: TextView = itemView.current_orders_product_name_3
        val productPrice3: TextView = itemView.current_orders_product_price_3
        val productImageUrl3: ImageView = itemView.current_orders_image_3
        val productLocation3: TextView = itemView.current_orders_product_location_3

        val productName4: TextView = itemView.current_orders_product_name_4
        val productPrice4: TextView = itemView.current_orders_product_price_4
        val productImageUrl4: ImageView = itemView.current_orders_image_4
        val productLocation4: TextView = itemView.current_orders_product_location_4

        val productName5: TextView = itemView.current_orders_product_name_5
        val productPrice5: TextView = itemView.current_orders_product_price_5
        val productImageUrl5: ImageView = itemView.current_orders_image_5
        val productLocation5: TextView = itemView.current_orders_product_location_5

        val productName6: TextView = itemView.current_orders_product_name_6
        val productPrice6: TextView = itemView.current_orders_product_price_6
        val productImageUrl6: ImageView = itemView.current_orders_image_6
        val productLocation6: TextView = itemView.current_orders_product_location_6

        val productName7: TextView = itemView.current_orders_product_name_7
        val productPrice7: TextView = itemView.current_orders_product_price_7
        val productImageUrl7: ImageView = itemView.current_orders_image_7
        val productLocation7: TextView = itemView.current_orders_product_location_7

        val productName8: TextView = itemView.current_orders_product_name_8
        val productPrice8: TextView = itemView.current_orders_product_price_8
        val productImageUrl8: ImageView = itemView.current_orders_image_8
        val productLocation8: TextView = itemView.current_orders_product_location_8

        val productName9: TextView = itemView.current_orders_product_name_9
        val productPrice9: TextView = itemView.current_orders_product_price_9
        val productImageUrl9: ImageView = itemView.current_orders_image_9
        val productLocation9: TextView = itemView.current_orders_product_location_9

        val productName10: TextView = itemView.current_orders_product_name_10
        val productPrice10: TextView = itemView.current_orders_product_price_10
        val productImageUrl10: ImageView = itemView.current_orders_image_10
        val productLocation10: TextView = itemView.current_orders_product_location_10

        // Feedback buttons
        val productBtn1: Button = itemView.current_orders_button_1
        val productBtn2: Button = itemView.current_orders_button_2
        val productBtn3: Button = itemView.current_orders_button_3
        val productBtn4: Button = itemView.current_orders_button_4
        val productBtn5: Button = itemView.current_orders_button_5
        val productBtn6: Button = itemView.current_orders_button_6
        val productBtn7: Button = itemView.current_orders_button_7
        val productBtn8: Button = itemView.current_orders_button_8
        val productBtn9: Button = itemView.current_orders_button_9
        val productBtn10: Button = itemView.current_orders_button_10

        val productCurrency1: TextView = itemView.orders_price_currency_1
        val productCurrency2: TextView = itemView.orders_price_currency_2
        val productCurrency3: TextView = itemView.orders_price_currency_3
        val productCurrency4: TextView = itemView.orders_price_currency_4
        val productCurrency5: TextView = itemView.orders_price_currency_5
        val productCurrency6: TextView = itemView.orders_price_currency_6
        val productCurrency7: TextView = itemView.orders_price_currency_7
        val productCurrency8: TextView = itemView.orders_price_currency_8
        val productCurrency9: TextView = itemView.orders_price_currency_9
        val productCurrency10: TextView = itemView.orders_price_currency_10

        init{
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            //val position = bindingAdapterPosition
            clickListener.onItemClick(this.bindingAdapterPosition, v!!)

        }

    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, v : View)
        fun onItemOne(position: Int, v: View?)
        fun onItemTwo(position: Int, v: View?)
        fun onItemThree(position: Int, v: View?)
        fun onItemFour(position: Int, v: View?)
        fun onItemFive(position: Int, v: View?)
        fun onItemSix(position: Int, v: View?)
        fun onItemSeven(position: Int, v: View?)
        fun onItemEight(position: Int, v: View?)
        fun onItemNine(position: Int, v: View?)
        fun onItemTen(position: Int, v: View?)
    }

}





