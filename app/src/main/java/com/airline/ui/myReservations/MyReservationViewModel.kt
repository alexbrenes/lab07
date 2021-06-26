package com.airline.ui.myReservations

import androidx.lifecycle.ViewModel
import com.airline.logic.Journey
import com.airline.logic.Reservation
import com.airline.persistance.Journeys
import com.airline.persistance.Reservations

class MyReservationViewModel : ViewModel() {

    var reservations: Reservations

    init {
        reservations = Reservations.instance
    }

    fun at(idx: Int): Reservation {
        return reservations.items.value?.get(idx)!!
    }
}