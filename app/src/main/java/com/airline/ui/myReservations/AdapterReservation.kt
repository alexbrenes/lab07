package com.airline.ui.myReservations

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.airline.logic.Journey
import com.airline.logic.Reservation
import com.example.lab_04_app.SeatSelection
import com.lab04.databinding.ItemReservationBinding

class AdapterReservation
    : RecyclerView.Adapter<AdapterReservation.ViewHolder>() {

    var items = listOf<Reservation>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder(val binding: ItemReservationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun render(reservation: Reservation) {
            binding.destinationRes.text = reservation.journey.destination.countryIdcountry.name
            binding.originRes.text = reservation.journey.origin.countryIdcountry.name
            binding.idreservation.text = ("" + reservation.idreservation)
            binding.seatsRes.text = ("" + reservation.seats)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemReservationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.render(items[position])
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.binding.seatsRes.context, SeatSelection::class.java)
            intent.putExtra("reservation", items[position])
            holder.binding.seatsRes.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = items.size
}