package com.airline.logic

import com.lab04.logic.User
import java.io.Serializable


class Reservation : Serializable {

    var idreservation: Int
    var seats: Int
    var journey: Journey
    var paymentMethod: Paymentmethod
    var user: User

    constructor(
        idreservation: Int,
        seats: Int,
        journey: Journey,
        paymentMethod: Paymentmethod,
        user: User
    ) {
        this.idreservation = idreservation
        this.seats = seats
        this.journey = journey
        this.paymentMethod = paymentMethod
        this.user = user
    }

    constructor(idreservation: Int) {
        this.idreservation = idreservation
    }

    constructor()

    init {
        idreservation = 0
        seats = 0
        journey = Journey()
        paymentMethod = Paymentmethod()
        user = User()
    }



}