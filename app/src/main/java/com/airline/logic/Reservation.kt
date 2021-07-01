package com.airline.logic

import com.lab04.logic.User
import java.io.Serializable


class Reservation : Serializable {

    var idreservation: Int? = null
    var seats: Int? = null
    var journeyIdjourney: Journey? = null
    var paymentmethodIdpaymentmethod: Paymentmethod? = null
    var userIduser: User? = null
    var ticketList: List<Ticket>? = null

    constructor(
        idreservation: Int,
        seats: Int,
        journey: Journey,
        paymentMethod: Paymentmethod,
        user: User
    ) {
        this.idreservation = idreservation
        this.seats = seats
        this.journeyIdjourney = journey
        this.paymentmethodIdpaymentmethod = paymentMethod
        this.userIduser = user
    }

    constructor(idreservation: Int) {
        this.idreservation = idreservation
    }

    constructor()
    constructor(
        idreservation: Int?,
        seats: Int?,
        journeyIdjourney: Journey?,
        paymentmethodIdpaymentmethod: Paymentmethod?,
        userIduser: User?,
        ticketList: List<Ticket>?
    ) {
        this.idreservation = idreservation
        this.seats = seats
        this.journeyIdjourney = journeyIdjourney
        this.paymentmethodIdpaymentmethod = paymentmethodIdpaymentmethod
        this.userIduser = userIduser
        this.ticketList = ticketList
    }

    init {
        idreservation = 0
        seats = 0
        journeyIdjourney = Journey()
        paymentmethodIdpaymentmethod = Paymentmethod()
        userIduser = User()
    }


}