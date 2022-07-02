package com.kunpark.resource.view.add_schedule

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.kunpark.resource.databinding.ItemOrganizationBinding
import com.kunpark.resource.model.Organization
import kotlinx.coroutines.*

/**
 * Created by BM Anderson on 02/07/2022.
 */
class OrganizationAdapter(private val list: ArrayList<Organization>): RecyclerView.Adapter<OrganizationAdapter.OrganizationViewHolder>() {

    private var isHandling = false
    private var childAdapter : AdapterMember?= null
    class OrganizationViewHolder(val binding: ItemOrganizationBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrganizationViewHolder {
        val binding = ItemOrganizationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrganizationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrganizationViewHolder, position: Int) {
        val organization = list[position]
        holder.binding.tvTitle.text = organization.name
        holder.binding.icCheck.isChecked = organization.isChosen

        if(organization.childDepartments.isNullOrEmpty()) {
            holder.binding.rvChildOrganization.visibility = View.GONE
        } else {
            holder.binding.rvChildOrganization.adapter = OrganizationAdapter(organization.childDepartments)
        }


        if(!organization.childMembers.isNullOrEmpty()) {
            if(childAdapter == null) {
                childAdapter = AdapterMember(organization.childMembers)
                holder.binding.rvMembers.adapter = childAdapter¬
            } else {

            }

        }

        holder.binding.icCheck.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked != organization.isChosen) {
                GlobalScope.launch {
                    checkChange(isChecked, organization, position)
                }
            }
        }
    }

    private suspend fun checkChange(checked: Boolean, organization: Organization, position: Int) {
        val scope = CoroutineScope(Dispatchers.IO)
        val job = scope.launch {
            organization.isChosen = checked
            if(!organization.childMembers.isNullOrEmpty()) {
                val iterator = organization.childMembers.iterator()
                while(iterator.hasNext()) {
                    iterator.next().isChosen = checked
                }
            }

            if(!organization.childDepartments.isNullOrEmpty()) {
                withContext(Dispatchers.IO) {
                    organization.childDepartments.forEach { child ->
                        checkChange(checked, child, position)
                    }
                }
            }
        }

        job.join()

        withContext(Dispatchers.Main) {
            list[position] = organization
            notifyItemChanged(position)
            isHandling = false
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<Organization>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }
}