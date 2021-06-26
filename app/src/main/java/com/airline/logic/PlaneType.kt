package com.lab04.logic

import java.io.Serializable

class PlaneType : Serializable {

    var idplanetype: Int
    var manufacturer: String
    var year: Int
    var model: String
    var capacity: Int
    var rows: Int
    var seatsperrow: Int

    constructor() {
        idplanetype = 0
        manufacturer = ""
        year = 0
        model = ""
        capacity = 0
        rows = 0
        seatsperrow = 0
    }

    constructor(
        idplanetype: Int,
        manufacturer: String,
        year: Int,
        model: String,
        capacity: Int,
        rows: Int,
        seatsperrow: Int
    ) {
        this.idplanetype = idplanetype
        this.manufacturer = manufacturer
        this.year = year
        this.model = model
        this.capacity = capacity
        this.rows = rows
        this.seatsperrow = seatsperrow
    }

    constructor(
        manufacturer: String,
        year: Int,
        model: String,
        capacity: Int,
        rows: Int,
        seatsperrow: Int
    ) {
        this.idplanetype = 0
        this.manufacturer = manufacturer
        this.year = year
        this.model = model
        this.capacity = capacity
        this.rows = rows
        this.seatsperrow = seatsperrow
    }

    init {
        idplanetype = 0
        manufacturer = ""
        year = 0
        model = ""
        capacity = 0
        rows = 0
        seatsperrow = 0
    }


}