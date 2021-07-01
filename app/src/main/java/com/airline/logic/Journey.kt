package com.airline.logic

import java.io.Serializable
import java.util.*

class Journey : Serializable {

    var idjourney: Int? = null
    var date: Date? = null
    var price = 0.0
    var availability: Int? = null
    var flightIdflight: Flight? = null
    var reservationList: List<Reservation>? = null
    var isSpecialOffer: Char? = null
    var specialPrice = 0f
    var isRoundTrip: Char? = null
    var backdate: Date? = null

    constructor(
        idjourney: Int,
        date: Date,
        price: Double,
        availability: Int,
        flight: Flight,
        isSpecialOffer: Char,
        specialPrice: Float,
        isRoundTrip: Char,
        backdate: Date
    ) {
        this.idjourney = idjourney
        this.date = date
        this.price = price
        this.availability = availability
        this.flightIdflight = flight
        this.isSpecialOffer = isSpecialOffer
        this.specialPrice = specialPrice
        this.isRoundTrip = isRoundTrip
        this.backdate = backdate
    }

    constructor(idjourney: Int) {
        this.idjourney = idjourney
        flightIdflight = Flight()
    }

    constructor()
    constructor(
        idjourney: Int?,
        date: Date?,
        price: Double,
        availability: Int?,
        flightIdflight: Flight?,
        reservationList: List<Reservation>?,
        isSpecialOffer: Char?,
        specialPrice: Float,
        isRoundTrip: Char?,
        backdate: Date?
    ) {
        this.idjourney = idjourney
        this.date = date
        this.price = price
        this.availability = availability
        this.flightIdflight = flightIdflight
        this.reservationList = reservationList
        this.isSpecialOffer = isSpecialOffer
        this.specialPrice = specialPrice
        this.isRoundTrip = isRoundTrip
        this.backdate = backdate
    }

    init {
        idjourney = 0
        date = Date()
        isRoundTrip = '0'
        backdate = Date()
        isSpecialOffer = '0'
        flightIdflight = Flight()
        availability = 0
        price = 0.0
    }

}