package com.org.dicodingeventapp.ui.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.org.dicodingeventapp.data.local.entity.EventEntity
import com.org.dicodingeventapp.databinding.CardItemDashboardBinding

class FavoriteAdapter(
    private val listEvent : List<EventEntity>
) : RecyclerView.Adapter<FavoriteAdapter.MyViewHolder>() {

    var onItemClickCallback : OnItemClickCallback? = null


    class MyViewHolder(val binding : CardItemDashboardBinding): RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = CardItemDashboardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listEvent.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = listEvent[position]
        holder.binding.tvText.text = event.name
        Glide.with(holder.itemView.context)
            .load(event.mediaCover)
            .into(holder.binding.ivCover)
        holder.binding.tvStatus.text = event.cityName
        holder.itemView.setOnClickListener {
            onItemClickCallback?.onItemClicked(event.id.toString())

        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(id: String)
    }


}