package com.lab04.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.airline.logic.Journey
import com.airline.persistance.Journeys
import com.lab04.logic.PlaneType
import com.lab04.persistance.PlaneTypes

class HomeViewModel : ViewModel() {

    var archived: ArrayList<Journey>
    var journeys: Journeys

    init {
        archived = ArrayList()
        journeys = Journeys.instance
    }

    fun delete(journey: Journey, position: Int) {
        archived.add(journey)
        journeys.delete(position)
    }

    fun undo(position: Int) {
        if (archived.size <= 0)
            return
        journeys.add(archived.get(archived.size - 1), position)
    }

    fun at(idx: Int): Journey {
        return journeys.items.value?.get(idx)!!
    }

}