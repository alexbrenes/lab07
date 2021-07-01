package com.airline.logic

import java.io.Serializable

class City : Serializable {

    var idcity: Int? = null
    var name: String? = null
    var countryIdcountry: Country? = null
    var flightList: List<Flight>? = null
    var flightList1: List<Flight>? = null

    constructor()
    constructor(idcity: Int, name: String, countryIdcountry: Country) {
        this.idcity = idcity
        this.name = name
        this.countryIdcountry = countryIdcountry
    }

    constructor(idcity: Int) {
        this.idcity = idcity
    }

    constructor(
        idcity: Int?,
        name: String?,
        countryIdcountry: Country?,
        flightList: List<Flight>?,
        flightList1: List<Flight>?
    ) {
        this.idcity = idcity
        this.name = name
        this.countryIdcountry = countryIdcountry
        this.flightList = flightList
        this.flightList1 = flightList1
    }

    init {
        idcity = 0
        name = ""
        countryIdcountry = Country()
    }

}