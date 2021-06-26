package com.airline.logic

import java.io.Serializable

class Ticket : Serializable {

    var idticket: Int
    var idseat: String
    var reservation: Reservation

    constructor(idticket: Int, idseat: String, reservation: Reservation) {
        this.idticket = idticket
        this.idseat = idseat
        this.reservation = reservation
    }

    constructor()
    constructor(idticket: Int) {
        this.idticket = idticket
    }

    init{
        idseat = ""
        idticket = 0
        reservation = Reservation()
    }

}