package com.airline.ui.reservation

import androidx.lifecycle.ViewModel
import com.airline.logic.Journey
import com.airline.persistance.Journeys
import com.airline.persistance.Reservations

class ReservationViewModel : ViewModel() {
    var journeys: Journeys
    var journey: Journey

    init {
        journey = Journey()
    }

    init {
        journeys = Journeys.instance
    }


}