package com.example.petgame.fragments.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.petgame.data.Model.Pet
import com.example.petgame.data.Model.PetWithStats
import com.example.petgame.data.Model.getFormatedFoodPercentage
import com.example.petgame.data.Model.getFormatedWaterPercentage
import com.example.petgame.databinding.PetItemBinding
import java.time.Duration
import java.time.LocalDateTime

class ItemListAdapter(private val onItemClicked: (PetWithStats) -> Unit) :
    ListAdapter<PetWithStats, ItemListAdapter.ItemViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            PetItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(current)
        }
        holder.bind(current)
    }

    class ItemViewHolder(private var binding: PetItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(petWithStats: PetWithStats) {
            binding.petName.text = petWithStats.pet.petName
            binding.petImage.setImageBitmap(petWithStats.pet.petImage)
            binding.waterValue.text = petWithStats.getFormatedWaterPercentage()
            binding.foodValue.text = petWithStats.getFormatedFoodPercentage()
        }
    }


    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<PetWithStats>() {
            override fun areItemsTheSame(oldItem: PetWithStats, newItem: PetWithStats): Boolean {
                return oldItem.pet === newItem.pet
            }

            override fun areContentsTheSame(oldItem: PetWithStats, newItem: PetWithStats): Boolean {
                return oldItem.pet.petName == newItem.pet.petName
            }
        }
    }
}