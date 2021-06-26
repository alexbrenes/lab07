package com.airline.persistance

import androidx.lifecycle.MutableLiveData
import com.airline.logic.Country
import com.airline.logic.Flight

class Flights {
    var sequence: Int
    var items = MutableLiveData<List<Flight>>()

    init {
        sequence = 0
        items.value = listOf(
            Flight(++sequence),
            Flight(++sequence),
            Flight(++sequence)
        )
    }

    private object HOLDER {
        val INSTANCE = Flights()
    }

    companion object {
        val instance: Flights by lazy {
            HOLDER.INSTANCE
        }
    }

    fun add(flight: Flight) {
        items.value = items.value!! + listOf(flight)
    }

    fun delete(id: Int) {
        items.value = items.value!!.filter { it.idflight != id }
    }
}