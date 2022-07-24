package com.kunpark.resource.view.add_schedule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kunpark.resource.R
import com.kunpark.resource.databinding.ItemResourceListBinding
import com.kunpark.resource.model.Organization
import com.kunpark.resource.model.ResourceTree
import com.kunpark.resource.model.User

class ResourceChartAdapter(
    private val onChosen: (ResourceTree) -> Unit
) : ListAdapter<ResourceTree, ResourceChartAdapter.ResourceChartViewHolder>(
    object : DiffUtil.ItemCallback<ResourceTree>() {
        override fun areItemsTheSame(oldItem: ResourceTree, newItem: ResourceTree): Boolean {
            return oldItem.resourceNo == newItem.resourceNo
        }

        override fun areContentsTheSame(oldItem: ResourceTree, newItem: ResourceTree): Boolean {
            return oldItem.isChosen == newItem.isChosen
        }
    }
) {

    class ResourceChartViewHolder(var binding: ItemResourceListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResourceChartViewHolder {
        return ResourceChartViewHolder(
            ItemResourceListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ResourceChartViewHolder, position: Int) {
            val resourceTree = getItem(position)
            if (!resourceTree.resourceTrees.isNullOrEmpty()) {
                val childAdapter = ResourceChartAdapter() {
                    onChosen.invoke(it)
                }
                holder.binding.rvChild.adapter = childAdapter
                childAdapter.submitList(resourceTree.resourceTrees)
            }

            holder.binding.ckChoose.visibility = View.GONE

            holder.binding.tvTitle.text = resourceTree.title ?: "-"

            holder.itemView.setOnClickListener {
                if(resourceTree.resourceTrees.isNullOrEmpty() && resourceTree.parentID?: "" != "#") {
                    onChosen.invoke(resourceTree)
                }
            }

            var icon: Int?
            var colorIcon: Int?
            when (resourceTree.resourceNo) {
                0 -> {
                    icon = R.drawable.ic_baseline_computer_24
                    colorIcon = R.color.colorPrimaryDark
                }

                else -> {
                    if (resourceTree.parentID ?: "" == "#") {
                        icon = R.drawable.ic_folder
                        colorIcon = R.color.colorPrimaryDark
                    } else {
                        icon = R.drawable.ic_organization
                        colorIcon = R.color.colorGray
                    }
                }
            }

            holder.binding.imgIcon.setImageResource(icon)
            holder.binding.imgIcon.setColorFilter(ContextCompat.getColor( holder.itemView.context, colorIcon), android.graphics.PorterDuff.Mode.MULTIPLY)
    }
}