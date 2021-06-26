package com.airline.persistance

import androidx.lifecycle.MutableLiveData
import com.airline.logic.Ticket

class Tickets {
    var sequence: Int
    var items = MutableLiveData<List<Ticket>>()

    init {
        sequence = 0
        items.value = listOf<Ticket>(
            Ticket(++sequence),
            Ticket(++sequence),
            Ticket(++sequence)
        )
    }

    private object HOLDER {
        val INSTANCE = Tickets()
    }

    companion object {
        val instance: Tickets by lazy {
            HOLDER.INSTANCE
        }
    }

    fun add(ticket: Ticket) {
        items.value = items.value!! + listOf(ticket)
    }

    fun delete(id: Int) {
        items.value = items.value!!.filter { it.idticket != id }
    }
}