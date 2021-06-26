package com.airline.logic

import java.io.Serializable
import java.util.*

class Flight : Serializable {
    var idflight: Int
    var day: String
    var hour: Date
    var destination: City
    var origin: City
    var planeIdplane: Plane

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

    init {
        idflight = 0
        day = ""
        hour = Date()
        destination = City()
        origin = City()
        planeIdplane = Plane()
    }
}