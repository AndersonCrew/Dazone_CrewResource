package com.kunpark.resource.view.organization

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.kunpark.resource.databinding.ItemMemberNewBinding
import com.kunpark.resource.model.Organization
import com.kunpark.resource.model.User
import com.kunpark.resource.utils.Constants
import com.kunpark.resource.utils.DazoneApplication

/**
 * Created by BM Anderson on 03/07/2022.
 */
class AdapterMember(private val department: Organization?, private val onCheckedChanged: (Organization?, User, Boolean) -> Unit): ListAdapter<User, AdapterMember.MemberViewHolder>(
    object : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.userNo == newItem.userNo
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.userNo == newItem.userNo
        }
    }
) {

    class MemberViewHolder(val binding: ItemMemberNewBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        val binding = ItemMemberNewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MemberViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        val personData = getItem(position)

        holder.binding.tvName.text = personData.name
        holder.binding.tvPosition.text = personData.positionName
        val urlAvatar = DazoneApplication.getInstance().mPref?.getString(Constants.DOMAIN, "") + personData.avatarUrl

        holder.binding.ckChoose.setOnCheckedChangeListener(null)
        holder.binding.ckChoose.isChecked = personData.isChosen
        holder.binding.ckChoose.setOnCheckedChangeListener { compoundButton, b ->
            if(b != personData.isChosen ) {
                if(department == null) {
                    personData.isChosen = b
                }

                onCheckedChanged.invoke(department,  getItem(position), b)
            }
        }


        var requestOptions = RequestOptions()
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL)
        Glide
            .with(holder.itemView.context)
            .load(urlAvatar)
            .apply(requestOptions)
            .into(holder.binding.imgAvatar)
    }
}