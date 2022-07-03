package com.kunpark.resource.view.add_schedule

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kunpark.resource.databinding.ItemOrganizationBinding
import com.kunpark.resource.model.Organization
import com.kunpark.resource.model.User
import kotlinx.coroutines.*

/**
 * Created by BM Anderson on 02/07/2022.
 */
class OrganizationAdapter: ListAdapter<Organization, OrganizationAdapter.OrganizationViewHolder>(
    object : DiffUtil.ItemCallback<Organization>() {
        override fun areItemsTheSame(oldItem: Organization, newItem: Organization): Boolean {
            return oldItem.isChosen == newItem.isChosen
        }

        override fun areContentsTheSame(oldItem: Organization, newItem: Organization): Boolean {
            return oldItem.name == newItem.name
        }
    }
) {

    class OrganizationViewHolder(val binding: ItemOrganizationBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrganizationViewHolder {
        val binding = ItemOrganizationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrganizationViewHolder(binding)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: OrganizationViewHolder, position: Int) {
        val organization = currentList[position]

        holder.binding.tvTitle.text = organization.name
        holder.binding.icCheck.isChecked = organization.isChosen

        if(!organization.childDepartments.isNullOrEmpty()) {
            val departmentAdapter = OrganizationAdapter()
            holder.binding.rvChildOrganization.adapter = departmentAdapter
            departmentAdapter.submitList(organization.childDepartments)
        }

        if(!organization.childMembers.isNullOrEmpty()) {
            val departmentAdapter = AdapterMember()
            holder.binding.rvMembers.adapter = departmentAdapter
            departmentAdapter.submitList(organization.childMembers)
        }

        holder.binding.icCheck.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked != organization.isChosen) {
                GlobalScope.launch {
                    Log.d("CHECKCHECK", "$isChecked")
                    checkChange(isChecked, organization)
                }
            }
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private suspend fun checkChange(checked: Boolean, organization: Organization) {
        val scope = CoroutineScope(Dispatchers.IO)
        val job = scope.launch {
            organization.isChosen = checked
            organization.isChanging = true
            if(!organization.childMembers.isNullOrEmpty()) {
                val iterator = organization.childMembers.iterator()
                while(iterator.hasNext()) {
                    iterator.next().apply {
                        isChosen = checked
                        isChanging = true
                    }
                }
            }

            if(!organization.childDepartments.isNullOrEmpty()) {
                withContext(Dispatchers.IO) {
                    organization.childDepartments.forEach { child ->
                        checkChange(checked, child)
                    }
                }
            }
        }

        job.join()

        withContext(Dispatchers.Main) {
            notifyDataSetChanged()
        }

    }
}