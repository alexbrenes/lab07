package com.airline.ui.checkout

import androidx.lifecycle.ViewModel
import com.airline.logic.Journey

class CheckoutViewModel : ViewModel() {
    val journey: Journey

    init {
        journey = Journey()
    }
}