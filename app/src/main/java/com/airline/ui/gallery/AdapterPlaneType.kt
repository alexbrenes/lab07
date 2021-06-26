package com.lab04.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.lab04.R
import com.lab04.databinding.ItemPlanetypeBinding
import com.lab04.logic.PlaneType

class AdapterPlaneType :
    RecyclerView.Adapter<AdapterPlaneType.ViewHolder>() {

    var items = listOf<PlaneType>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder(val binding: ItemPlanetypeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun render(planeType: PlaneType) {
            binding.manufacturerPlaneType.text = planeType.manufacturer
            binding.idPlaneType.text = ("" + planeType.idplanetype)
            binding.planetypeModel.text = ("" + planeType.model)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemPlanetypeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.render(items[position])
        holder.itemView.setOnClickListener {
            val bundle = bundleOf(
                "planetype" to items[position],
                "case" to 0
            )
            holder.itemView.findNavController().navigate(R.id.planetypeFragment, bundle)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}