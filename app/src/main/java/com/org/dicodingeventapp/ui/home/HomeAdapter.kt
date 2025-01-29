package com.org.dicodingeventapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.org.dicodingeventapp.databinding.CardItemHomeBinding
import com.org.dicodingeventapp.service.data.response.ListEventsItem

class HomeAdapter(
    private val listEvent : List<ListEventsItem>
) : RecyclerView.Adapter<HomeAdapter.MyViewHolder>() {

    var onItemClickCallback : OnItemClickCallback? = null

    //
    class MyViewHolder(val binding : CardItemHomeBinding): RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = CardItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listEvent.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = listEvent[position]

        holder.binding.tvTitleHome.text = event.name
        holder.binding.tvTitleLocation.text = event.cityName
        Glide.with(holder.itemView.context)
            .load(event.mediaCover)
            .into(holder.binding.ivCoverHome)
        holder.itemView.setOnClickListener {
            onItemClickCallback?.onItemClicked(event.id.toString())

        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(id: String)
    }


}