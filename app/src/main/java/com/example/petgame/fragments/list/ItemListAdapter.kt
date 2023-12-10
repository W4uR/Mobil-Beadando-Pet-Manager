package com.example.petgame.fragments.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.petgame.data.Model.Pet
import com.example.petgame.databinding.PetItemBinding

class ItemListAdapter(private val onItemClicked: (Pet) -> Unit) :
    ListAdapter<Pet, ItemListAdapter.ItemViewHolder>(DiffCallback) {

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

        fun bind(pet: Pet) {
            binding.petName.text = pet.petName
            binding.petImage.setImageBitmap(pet.petImage)
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Pet>() {
            override fun areItemsTheSame(oldItem: Pet, newItem: Pet): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Pet, newItem: Pet): Boolean {
                return oldItem.petName == newItem.petName
            }
        }
    }
}