package com.kunpark.resource.view.add_schedule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kunpark.resource.databinding.ItemMemberBinding
import com.kunpark.resource.model.User
import com.kunpark.resource.utils.Constants
import com.kunpark.resource.utils.DazoneApplication

/**
 * Created by BM Anderson on 03/07/2022.
 */
class AdapterMember(private val list: ArrayList<User>): RecyclerView.Adapter<AdapterMember.MemberViewHolder>() {

    class MemberViewHolder(val binding: ItemMemberBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        val binding = ItemMemberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MemberViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        val user = list[position]
        holder.binding.tvName.text = user.name
        holder.binding.tvPosition.text = user.positionName
        holder.binding.ckChoose.isChecked = user.isChosen
        val urlAvatar = DazoneApplication.getInstance().mPref?.getString(Constants.DOMAIN, "") + user.avatarUrl

        Glide.with(holder.itemView.context).load(urlAvatar).into(holder.binding.imgAvatar)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}