package com.kunpark.resource.view.add_schedule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.kunpark.resource.databinding.ItemMemberBinding
import com.kunpark.resource.model.User
import com.kunpark.resource.utils.Constants
import com.kunpark.resource.utils.DazoneApplication

/**
 * Created by BM Anderson on 03/07/2022.
 */
class AdapterMember: ListAdapter<User, AdapterMember.MemberViewHolder>(
    object : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.isChosen == newItem.isChosen
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.name == newItem.name
        }
    }
) {

    class MemberViewHolder(val binding: ItemMemberBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        val binding = ItemMemberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MemberViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        val user = currentList[position]

        holder.binding.tvName.text = user.name
        holder.binding.tvPosition.text = user.positionName
        holder.binding.ckChoose.isChecked = user.isChosen
        val urlAvatar = DazoneApplication.getInstance().mPref?.getString(Constants.DOMAIN, "") + user.avatarUrl

        var requestOptions = RequestOptions()
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL)
        Glide
            .with(holder.itemView.context)
            .load(urlAvatar)
            .apply(requestOptions)
            .into(holder.binding.imgAvatar)
    }
}