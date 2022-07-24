package com.kunpark.resource.view.organization

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kunpark.resource.R
import com.kunpark.resource.databinding.ItemOrganizationNewBinding
import com.kunpark.resource.model.Organization
import com.kunpark.resource.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by BM Anderson on 02/07/2022.
 */
class OrganizationAdapter(private val onCheckedDone: (Organization, Boolean) -> Unit, private val onMemberCheckedChange: (Organization?, User, Boolean) -> Unit):
    ListAdapter<Organization, OrganizationAdapter.OrganizationViewHolder>(
        object : DiffUtil.ItemCallback<Organization>() {
            override fun areItemsTheSame(oldItem: Organization, newItem: Organization): Boolean {
                return oldItem.departNo == newItem.departNo
            }

            override fun areContentsTheSame(oldItem: Organization, newItem: Organization): Boolean {
                return oldItem.departNo == newItem.departNo
            }
        }
    ) {

    class OrganizationViewHolder(val binding: ItemOrganizationNewBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrganizationViewHolder {
        val binding = ItemOrganizationNewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrganizationViewHolder(binding)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: OrganizationViewHolder, position: Int) {
        val organization = getItem(position)

        holder.binding.tvTitle.text = organization.name
        holder.binding.icCheck.isChecked = organization.isChosen

        if(!organization.childDepartments.isNullOrEmpty()) {
            val departmentAdapter = OrganizationAdapter({ organization, isChecked ->
                onCheckedDone.invoke(organization, isChecked)
            }, { department, member, isChecked ->
                onMemberCheckedChange.invoke(department, member, isChecked)
            })

            holder.binding.rvChildOrganization.adapter = departmentAdapter
            departmentAdapter.submitList(organization.childDepartments)
        }

        if(!organization.childMembers.isNullOrEmpty()) {
            val departmentAdapter = AdapterMember(organization) { department, member, isChecked ->
                onMemberCheckedChange.invoke(department, member, isChecked)
            }

            holder.binding.rvMembers.adapter = departmentAdapter
            departmentAdapter.submitList(organization.childMembers)
        }

        holder.binding.icCheck.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked != organization.isChosen) {
                onCheckedDone.invoke(getItem(position), isChecked)
            }
        }

        holder.binding.imgFolder.setOnClickListener {
            if(holder.binding.rvMembers.visibility == View.GONE) {
                holder.binding.rvMembers.visibility = View.VISIBLE
                holder.binding.rvChildOrganization.visibility = View.VISIBLE
                holder.binding.imgFolder.setBackgroundResource(R.drawable.ic_folder_open)
            } else {
                holder.binding.rvMembers.visibility = View.GONE
                holder.binding.rvChildOrganization.visibility = View.GONE
                holder.binding.imgFolder.setBackgroundResource(R.drawable.ic_folder_close_new)
            }
        }
    }

    suspend fun getSelected(): ArrayList<User> {
        val scope = CoroutineScope(Dispatchers.IO)
        var selected: ArrayList<User> = arrayListOf()
        val job = scope.launch {
            for(i in 0 until itemCount) {
                Log.d("RECHECK", "getSelected for i = $i")
                val personData: Organization = getItem(i)

                selected.addAll(personData.childMembers.filter { it.isChosen })

                if(!personData.childDepartments.isNullOrEmpty()) {
                    withContext(Dispatchers.IO) {
                        checkDepartment(personData.childDepartments, selected)
                    }

                }
            }
        }

        job.join()
        Log.d("RECHECK", "getSelected DONE size = ${selected.size}")
        val result : ArrayList<User> = ArrayList(selected)
        return result
    }

    private suspend fun checkDepartment(departments: ArrayList<Organization>, selected: ArrayList<User>) {
        departments.forEach {
            Log.d("RECHECK", "checkDepartment forEach size = ${departments.size}")
            selected.addAll(it.childMembers.filter { member -> member.isChosen })

            if(!it.childDepartments.isNullOrEmpty()) {
                withContext(Dispatchers.IO) {
                    checkDepartment(it.childDepartments, selected)
                }

            }
        }
    }
}