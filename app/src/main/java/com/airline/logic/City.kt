package com.airline.logic

import java.io.Serializable

class City : Serializable {
    var idcity: Int
    var name: String
    var countryIdcountry: Country

    constructor()
    constructor(idcity: Int, name: String, countryIdcountry: Country) {
        this.idcity = idcity
        this.name = name
        this.countryIdcountry = countryIdcountry
    }

    constructor(idcity: Int) {
        this.idcity = idcity
    }

    init {
        idcity = 0
        name = ""
        countryIdcountry = Country()
    }

}