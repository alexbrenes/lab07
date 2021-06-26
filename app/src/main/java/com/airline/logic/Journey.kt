package com.airline.logic

import java.io.Serializable
import java.util.*

class Journey : Serializable {

    var idjourney: Int
    var date: Date
    var price = 0.0
    var availability: Int
    var flight: Flight
    var isSpecialOffer: Char
    var specialPrice = 0f
    var isRoundTrip: Char
    var backdate: Date
    var origin: City
    var destination: City
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
        this.flight = flight
        this.isSpecialOffer = isSpecialOffer
        this.specialPrice = specialPrice
        this.isRoundTrip = isRoundTrip
        this.backdate = backdate
    }

    constructor(idjourney: Int) {
        this.idjourney = idjourney
        flight = Flight()
    }

    constructor()

    init {
        destination = City()
        origin = City()
        idjourney = 0
        date = Date()
        isRoundTrip = '0'
        backdate = Date()
        isSpecialOffer = '0'
        flight = Flight()
        availability = 0
        price = 0.0
    }

}