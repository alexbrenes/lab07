package com.airline.logic

import java.io.Serializable

class Country : Serializable {

    var idcountry: Int? = null
    var abbreviation: String? = null
    var name: String? = null
    var cityList: List<City>? = null

    constructor(abbreviation: String, name: String) {
        idcountry = 0
        this.abbreviation = abbreviation
        this.name = name
    }

    constructor(idcountry: Int, abbreviation: String, name: String) {
        this.idcountry = idcountry
        this.abbreviation = abbreviation
        this.name = name
    }

    constructor()
    constructor(idcountry: Int?, abbreviation: String?, name: String?, cityList: List<City>?) {
        this.idcountry = idcountry
        this.abbreviation = abbreviation
        this.name = name
        this.cityList = cityList
    }

    init {
        idcountry = 0
        abbreviation = ""
        name = ""
    }
//    private val cityList: List<City>? = null


}