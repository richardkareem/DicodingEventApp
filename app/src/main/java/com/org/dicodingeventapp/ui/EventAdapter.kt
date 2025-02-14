package com.org.dicodingeventapp.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.org.dicodingeventapp.data.remote.response.ListEventsItem
import com.org.dicodingeventapp.databinding.CardItemDashboardBinding

class EventAdapter : ListAdapter<ListEventsItem, EventAdapter.MyViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback : OnClickCallback

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = CardItemDashboardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val events = getItem(position)
        holder.bind(events)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(events)
        }
    }
    class MyViewHolder(private val binding: CardItemDashboardBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(event: ListEventsItem){
            binding.tvText.text = event.name
            Glide.with(binding.root)
                .load(event.mediaCover)
                .into(binding.ivCover)
            binding.tvStatus.text = event.cityName
        }
    }

    fun setItemOnClickCallback(onItemClickCallback: OnClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnClickCallback {
        fun onItemClicked(data: ListEventsItem)
    }

    companion object{
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>(){
            override fun areItemsTheSame(
                oldItem: ListEventsItem,
                newItem: ListEventsItem
            ): Boolean {
                return  oldItem == newItem
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                oldItem: ListEventsItem,
                newItem: ListEventsItem
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

}