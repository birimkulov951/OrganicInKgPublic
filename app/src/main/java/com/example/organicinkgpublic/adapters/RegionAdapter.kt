package com.example.organicinkgpublic.adapters
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.organicinkgpublic.R
import com.example.organicinkgpublic.model.Region
import kotlinx.android.synthetic.main.faq_list_item.view.*

class RegionAdapter (
    private val listener: OnItemClickListener
)
    : RecyclerView.Adapter<RegionAdapter.RegionViewHolder>(){

    private var region = emptyList<Region>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RegionViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.faq_list_item, parent, false)

        return RegionViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RegionViewHolder, position: Int) {
        val currentlocation = region[position]
        holder.region.text = currentlocation.region

    }

    override fun getItemCount(): Int {
        return region.size
    }

    internal fun setQuestionItems(regions: ArrayList<Region>) {
        this.region = regions
    }

    inner class RegionViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener{

        val region: TextView = itemView.faq_question

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