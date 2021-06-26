package com.airline.persistance

import androidx.lifecycle.MutableLiveData
import com.airline.logic.City
import com.airline.logic.Country
import com.airline.logic.Journey
import com.lab04.persistance.PlaneTypes

class Journeys {

    var sequence: Int
    var items = MutableLiveData<List<Journey>>()

    init {
        sequence = 0
        items.value = listOf(
            Journey(++sequence),
            Journey(++sequence),
            Journey(++sequence),
            Journey(++sequence),
            Journey(++sequence),
            Journey(++sequence),
            Journey(++sequence),
            Journey(++sequence),
            Journey(++sequence),
            Journey(++sequence),
            Journey(++sequence),
            Journey(++sequence),
            Journey(++sequence)
        )
    }

    private object HOLDER {
        val INSTANCE = Journeys()
    }

    companion object {
        val instance: Journeys by lazy {
            HOLDER.INSTANCE
        }
    }

    fun add(journey: Journey, position: Int = items.value?.size!!) {
        items.value = items.value?.slice(0..(position - 1))!! + listOf(journey) +
                items.value?.slice(position..(items.value?.size!! - 1))!!
    }

    fun delete(id: Int) {
        items.value = items.value!!.filter { it.idjourney != id }
    }

}