package com.example.organicinkgandroid.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.organicinkgandroid.MainActivity
import com.example.organicinkgandroid.R
import com.example.organicinkgandroid.model.CategoriesItem
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import kotlinx.android.synthetic.main.categories_list_item.view.*


class CategoriesAdapter(
    private val context: Context,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>(){

    private var categoriesItem = emptyList<CategoriesItem>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.categories_list_item, parent, false)

        return CategoriesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        val currentItem = categoriesItem[position]


       try {
//            Glide.with(context)
//                .`as`(PictureDrawable::class.java)
//                .load(currentItem.itemIcon)
//                .listener(SvgSoftwareLayerSetter())
//                .apply(options)
//                .centerInside()
//                .dontAnimate()
//                .override(24,24)
//              //  .transition(DrawableTransitionOptions.withCrossFade(400))
//                .into(holder.itemIcon)
        GlideToVectorYou.justLoadImage(context as MainActivity, currentItem.itemIcon.toUri(), holder.itemIcon)
        } catch (e: IndexOutOfBoundsException) {
            Log.e("CategoriesAdapter", "onBindViewHolder: IndexOutOfBoundsException")
            holder.itemIcon.setImageResource(R.drawable.ic_ozdorovitelnye_smesi)
        }


        holder.itemTitle.text = currentItem.itemTitle
        holder.itemDescription.text = currentItem.itemDescription

        // Checks if "Ягоды"s contain more than 1 "Ягоды"
        if (currentItem.item1 != null) {
            holder.itemOne.text = currentItem.item1
        }
        if (currentItem.item2 != null) {
            holder.itemTwo.text = currentItem.item2
        }
        if (currentItem.item3 != null) {
            holder.itemThree.text = currentItem.item3
        }
        if (currentItem.item4 != null) {
            holder.itemFour.text = currentItem.item4
        }
        if (currentItem.item5 != null) {
            holder.itemFive.text = currentItem.item5
        }
        if (currentItem.item6 != null) {
            holder.itemSix.text = currentItem.item6
        }
        if (currentItem.item7 != null) {
            holder.itemSeven.text = currentItem.item7
        }
        if (currentItem.item8 != null) {
            holder.itemEight.text = currentItem.item8
        }
        if (currentItem.item9 != null) {
            holder.itemNine.text = currentItem.item9
        }
        if (currentItem.item10 != null) {
            holder.itemTen.text = currentItem.item10
        }
        if (currentItem.item11 != null) {
            holder.itemEleven.text = currentItem.item11
        }
        if (currentItem.item12 != null) {
            holder.itemTwelve.text = currentItem.item12
        }
        if (currentItem.item13 != null) {
            holder.itemThirteen.text = currentItem.item13
        }
        if (currentItem.item14 != null) {
            holder.itemFourteen.text = currentItem.item14
        }
        if (currentItem.item15 != null) {
            holder.itemFiveteen.text = currentItem.item15
        }
        if (currentItem.item16 != null) {
            holder.itemSixteen.text = currentItem.item16
        }
        if (currentItem.item17 != null) {
            holder.itemSeventeen.text = currentItem.item17
        }
        if (currentItem.item18 != null) {
            holder.itemEighteen.text = currentItem.item18
        }
        if (currentItem.item19 != null) {
            holder.itemNineteen.text = currentItem.item19
        }
        if (currentItem.item20 != null) {
            holder.itemTwenty.text = currentItem.item20
        }

        holder.itemOne.setOnClickListener{
            listener.onItemOne(position,holder.itemView.rootView)
        }

        holder.itemTwo.setOnClickListener{
            listener.onItemTwo(position,holder.itemView.rootView)
        }

        holder.itemThree.setOnClickListener{
            listener.onItemThree(position,holder.itemView.rootView)
        }

        holder.itemFour.setOnClickListener{
            listener.onItemFour(position,holder.itemView.rootView)
        }

        holder.itemFive.setOnClickListener{
            listener.onItemFive(position,holder.itemView.rootView)
        }

        holder.itemSix.setOnClickListener{
            listener.onItemSix(position,holder.itemView.rootView)
        }

        holder.itemSeven.setOnClickListener{
            listener.onItemSeven(position,holder.itemView.rootView)
        }

        holder.itemEight.setOnClickListener{
            listener.onItemEight(position,holder.itemView.rootView)
        }

        holder.itemNine.setOnClickListener{
            listener.onItemNine(position,holder.itemView.rootView)
        }

        holder.itemTen.setOnClickListener{
            listener.onItemTen(position,holder.itemView.rootView)
        }

        holder.itemEleven.setOnClickListener{
            listener.onItemEleven(position,holder.itemView.rootView)
        }

        holder.itemTwelve.setOnClickListener{
            listener.onItemTwelve(position,holder.itemView.rootView)
        }

        holder.itemThirteen.setOnClickListener{
            listener.onItemThirteen(position,holder.itemView.rootView)
        }

        holder.itemFourteen.setOnClickListener{
            listener.onItemFourteen(position,holder.itemView.rootView)
        }

        holder.itemFiveteen.setOnClickListener{
            listener.onItemFifteen(position,holder.itemView.rootView)
        }

        holder.itemSixteen.setOnClickListener{
            listener.onItemSixteen(position,holder.itemView.rootView)
        }

        holder.itemSeventeen.setOnClickListener{
            listener.onItemSeventeen(position,holder.itemView.rootView)
        }

        holder.itemEighteen.setOnClickListener{
            listener.onItemEighteen(position,holder.itemView.rootView)
        }

        holder.itemNineteen.setOnClickListener{
            listener.onItemNineteen(position,holder.itemView.rootView)
        }

        holder.itemTwenty.setOnClickListener{
            listener.onItemTwenty(position,holder.itemView.rootView)
        }

    }

    override fun getItemCount() = categoriesItem.size

    internal fun setCategoriesItems(categoryItem: List<CategoriesItem>) {
        this.categoriesItem = categoryItem
    }

    fun getItemAt(position: Int): CategoriesItem {
        return categoriesItem[position]
    }

    //**********************************************************************************************

    inner class CategoriesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        val itemIcon: ImageView = itemView.categories_icon
        val itemTitle: TextView = itemView.categories_text_title
        val itemDescription: TextView = itemView.categories_description

        val itemOne: TextView = itemView.categories_text_one
        val itemTwo: TextView = itemView.categories_text_two
        val itemThree: TextView = itemView.categories_text_three
        val itemFour: TextView = itemView.categories_text_four
        val itemFive: TextView = itemView.categories_text_five
        val itemSix: TextView = itemView.categories_text_six
        val itemSeven: TextView = itemView.categories_text_seven
        val itemEight: TextView = itemView.categories_text_eight
        val itemNine: TextView = itemView.categories_text_nine

        val itemTen: TextView = itemView.categories_text_ten
        val itemEleven: TextView = itemView.categories_text_eleven
        val itemTwelve: TextView = itemView.categories_text_twelve
        val itemThirteen: TextView = itemView.categories_text_thirteen
        val itemFourteen: TextView = itemView.categories_text_fourteen
        val itemFiveteen: TextView = itemView.categories_text_fifteen
        val itemSixteen: TextView = itemView.categories_text_sixteen
        val itemSeventeen: TextView = itemView.categories_text_seventeen
        val itemEighteen: TextView = itemView.categories_text_eighteen
        val itemNineteen: TextView = itemView.categories_text_nineteen
        val itemTwenty: TextView = itemView.categories_text_twenty

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {

            listener.onItemClick(this.bindingAdapterPosition, v)

        }

    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, v: View?)
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
        fun onItemEleven(position: Int, v: View?)
        fun onItemTwelve(position: Int, v: View?)
        fun onItemThirteen(position: Int, v: View?)
        fun onItemFourteen(position: Int, v: View?)
        fun onItemFifteen(position: Int, v: View?)
        fun onItemSixteen(position: Int, v: View?)
        fun onItemSeventeen(position: Int, v: View?)
        fun onItemEighteen(position: Int, v: View?)
        fun onItemNineteen(position: Int, v: View?)
        fun onItemTwenty(position: Int, v: View?)
    }

}