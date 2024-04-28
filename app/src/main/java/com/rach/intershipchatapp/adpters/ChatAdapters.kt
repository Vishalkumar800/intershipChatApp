package com.rach.intershipchatapp.adpters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rach.intershipchatapp.activity.ChatActivity
import com.rach.intershipchatapp.databinding.ChatItemLayoutBinding
import com.rach.intershipchatapp.model.userModel

class ChatAdapters(private val context: Context, val list: ArrayList<userModel>) :
    RecyclerView.Adapter<ChatAdapters.ChatViewHolder>() {

    inner class ChatViewHolder(val binding: ChatItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = ChatItemLayoutBinding.inflate(LayoutInflater.from(context),parent,false)
        return ChatViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val data = list[position]


        Glide.with(context).load(data.imageUrl).into(holder.binding.userImage)
        holder.binding.userName.text = data.name

        holder.itemView.setOnClickListener {
            val intent = Intent(context , ChatActivity::class.java)
            intent.putExtra("uid",data.uid)
            context.startActivity(intent)
        }


    }

}