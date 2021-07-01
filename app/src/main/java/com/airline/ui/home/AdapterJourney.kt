package com.airline.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.airline.logic.Journey
import com.lab04.databinding.ItemJourneyBinding

class AdapterJourney :
    RecyclerView.Adapter<AdapterJourney.ViewHolder>() {

    var items = listOf<Journey>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder(val binding: ItemJourneyBinding) : RecyclerView.ViewHolder(binding.root) {
        fun render(journey: Journey) {
            binding.destination.text = journey.flightIdflight!!.destination!!.name
            binding.origin.text = journey.flightIdflight!!.origin!!.name
            binding.availability.text = ("" + journey.availability)
            binding.flightPrice.text = ("₡ " + journey.specialPrice)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemJourneyBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.render(items[position])
    }

    override fun getItemCount(): Int = items.size
}