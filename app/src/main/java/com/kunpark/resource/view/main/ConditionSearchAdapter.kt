package com.kunpark.resource.view.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kunpark.resource.R
import com.kunpark.resource.model.ConditionSearch

class ConditionSearchAdapter(var listCondition: List<ConditionSearch>, private var onItemClick : ((conditionSearch: ConditionSearch) -> Unit)): RecyclerView.Adapter<ConditionSearchAdapter.ConditionSearchViewHolder>(){

    class ConditionSearchViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private var tvContent: TextView?= null
        private var imgFolder: ImageView?= null
         init {
             tvContent = itemView.findViewById(R.id.tvContent)
             imgFolder = itemView.findViewById(R.id.imgFolder)
         }

        fun bindView(conditionSearch: ConditionSearch) {
            tvContent?.text = conditionSearch.title
            imgFolder?.visibility = if(conditionSearch.isCheck == true) View.VISIBLE else View.INVISIBLE
        }
    }

    fun updateList(list: List<ConditionSearch>) {
        listCondition = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConditionSearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search_condition, parent, false)
        return ConditionSearchViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listCondition.size
    }

    override fun onBindViewHolder(holder: ConditionSearchViewHolder, position: Int) {
        if(!listCondition.isNullOrEmpty()) {
            holder.bindView(listCondition[position])

            holder.itemView.setOnClickListener {
                for(condition in listCondition) {
                    condition.isCheck = listCondition.indexOf(condition) == position
                }

                notifyDataSetChanged()
                onItemClick.invoke(listCondition[position])
            }
        }
    }
}