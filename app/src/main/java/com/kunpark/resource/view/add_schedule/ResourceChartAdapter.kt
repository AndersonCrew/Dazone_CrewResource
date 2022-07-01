package com.kunpark.resource.view.add_schedule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.kunpark.resource.R
import com.kunpark.resource.model.Organization

class ResourceChartAdapter(
    private val list: List<Organization>,
    private val onChosen: (Organization) -> Unit
) : RecyclerView.Adapter<ResourceChartAdapter.OrganizationViewHolder>() {

    class OrganizationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         var llParent: ConstraintLayout? = null
         var tvTitle: TextView? = null
         var rvChild: RecyclerView? = null
         var imgIcon: ImageView? = null
         var ckChoose: CheckBox? = null

        init {
            llParent = itemView.findViewById(R.id.llParent)
            tvTitle = itemView.findViewById(R.id.tvTitle)
            rvChild = itemView.findViewById(R.id.rvChild)
            imgIcon = itemView.findViewById(R.id.imgIcon)
            ckChoose = itemView.findViewById(R.id.ckChoose)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrganizationViewHolder {
        return OrganizationViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_resource_list, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: OrganizationViewHolder, position: Int) {
        if (!list.isNullOrEmpty()) {
            val organization = list[position]
            if (!organization.resourceTrees.isNullOrEmpty()) {
                holder.rvChild?.adapter = ResourceChartAdapter(organization.resourceTrees) {
                    onChosen.invoke(it)
                }
            }

            //holder.ckChoose?.visibility = if(organization.resourceTrees.isNullOrEmpty() && organization.parentID?: "" != "#") View.VISIBLE else View.GONE
            holder.ckChoose?.visibility = View.GONE

            holder.tvTitle?.text = organization.title ?: "-"

            holder.itemView.setOnClickListener {
                if(organization.resourceTrees.isNullOrEmpty() && organization.parentID?: "" != "#") {
                    onChosen.invoke(organization)
                }
            }

            var icon: Int?
            var colorIcon: Int?
            when (organization.resourceNo) {
                0 -> {
                    icon = R.drawable.ic_baseline_computer_24
                    colorIcon = R.color.colorPrimaryDark
                }

                else -> {
                    if (organization.parentID ?: "" == "#") {
                        icon = R.drawable.ic_folder
                        colorIcon = R.color.colorPrimaryDark
                    } else {
                        icon = R.drawable.ic_organization
                        colorIcon = R.color.colorGray
                    }
                }
            }

            holder.imgIcon?.setImageResource(icon)
            holder.imgIcon?.setColorFilter(ContextCompat.getColor( holder.itemView.context, colorIcon), android.graphics.PorterDuff.Mode.MULTIPLY)
        }
    }

    fun getOrganizationChosen(): Organization? {
        return list.findLast { it.isChosen }
    }
}