package com.kunpark.resource.view.add_schedule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.kunpark.resource.R
import com.kunpark.resource.model.Organization

class OrganizationAdapter(
    private val list: List<Organization>,
    private val itemClick: (organization: Organization) -> Unit
) : RecyclerView.Adapter<OrganizationAdapter.OrganizationViewHolder>() {

    class OrganizationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var llParent: ConstraintLayout? = null
        private var tvTitle: TextView? = null
        private var rvChild: RecyclerView? = null
        private var imgIcon: ImageView? = null

        init {
            llParent = itemView.findViewById(R.id.llParent)
            tvTitle = itemView.findViewById(R.id.tvTitle)
            rvChild = itemView.findViewById(R.id.rvChild)
            imgIcon = itemView.findViewById(R.id.imgIcon)
        }

        fun bind(organization: Organization, itemClick: (organization: Organization) -> Unit) {
            if (organization.resourceTrees.isNullOrEmpty()) {
                rvChild?.visibility = View.GONE
            } else {
                val adapter = OrganizationAdapter(organization.resourceTrees, itemClick)
                rvChild?.adapter = adapter
            }

            tvTitle?.text = organization.title ?: "-"

            llParent?.setOnClickListener {
                if(organization.parentID?: "" == "#") {
                    if (rvChild?.visibility == View.VISIBLE) {
                        rvChild?.visibility = View.GONE
                    } else {
                        rvChild?.visibility = View.VISIBLE
                    }
                } else {
                    itemClick.invoke(organization)
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

            imgIcon?.setImageResource(icon)
            imgIcon?.setColorFilter(ContextCompat.getColor(itemView.context, colorIcon), android.graphics.PorterDuff.Mode.MULTIPLY)
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
            holder.bind(list[position], itemClick)
        }
    }
}