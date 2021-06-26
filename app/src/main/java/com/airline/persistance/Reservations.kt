package com.airline.persistance

import androidx.lifecycle.MutableLiveData
import com.airline.logic.Reservation

class Reservations {
    var sequence: Int
    var items = MutableLiveData<List<Reservation>>()

    init {
        sequence = 0
        items.value = listOf<Reservation>(
            Reservation(++sequence),
            Reservation(++sequence),
            Reservation(++sequence)
        )
    }

    private object HOLDER {
        val INSTANCE = Reservations()
    }

    companion object {
        val instance: Reservations by lazy {
            HOLDER.INSTANCE
        }
    }

    fun add(reservation: Reservation) {
        items.value = items.value!! + listOf(reservation)
    }

    fun delete(id: Int) {
        items.value = items.value!!.filter { it.idreservation != id }
    }
}