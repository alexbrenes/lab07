package com.airline.logic

import java.io.Serializable
import java.util.*

class Flight : Serializable {
    
    var idflight: Int? = null
    var day: String? = null
    var hour: Date? = null
    var journeyList: List<Journey>? = null
    var destination: City? = null
    var origin: City? = null
    var planeIdplane: Plane? = null
    
    constructor(){
        origin = City(0,"San José",Country())
        destination = City(1, "Berlín", Country())
    }
    constructor(
        idflight: Int,
        day: String,
        hour: Date,
        destination: City,
        origin: City,
        planeIdplane: Plane
    ) {
        this.idflight = idflight
        this.day = day
        this.hour = hour
        this.destination = destination
        this.origin = origin
        this.planeIdplane = planeIdplane
    }

    constructor(idflight: Int) {
        this.idflight = idflight
    }

    constructor(
        idflight: Int?,
        day: String?,
        hour: Date?,
        journeyList: List<Journey>?,
        destination: City?,
        origin: City?,
        planeIdplane: Plane?
    ) {
        this.idflight = idflight
        this.day = day
        this.hour = hour
        this.journeyList = journeyList
        this.destination = destination
        this.origin = origin
        this.planeIdplane = planeIdplane
    }

    init {
        idflight = 0
        day = ""
        hour = Date()
        destination = City()
        origin = City()
        planeIdplane = Plane()
    }
}