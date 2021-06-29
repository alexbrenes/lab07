package com.lab04.logic

import java.io.Serializable
import java.util.*

class User : Serializable {
    var iduser: Int
    var email: String
    var password: String
    var name: String
    var lastname: String
    var birthday: String
    var address: String
    var workphone: String
    var phone: String
    var isadmin: Char

    constructor(
        email: String,
        password: String,
        name: String,
        lastname: String,
        birthday: String,
        address: String,
        workphone: String,
        phone: String,
        isadmin: Char
    ) {
        this.email = email
        this.password = password
        this.name = name
        this.lastname = lastname
        this.birthday = birthday
        this.address = address
        this.workphone = workphone
        this.phone = phone
        this.isadmin = isadmin
    }

    constructor(
        email: String,
        password: String,
        name: String,
        lastname: String,
        birthday: String,
        address: String,
        phone: String,
        workphone: String
    ) {
        this.email = email
        this.password = password
        this.name = name
        this.lastname = lastname
        this.birthday = birthday
        this.address = address
        this.workphone = workphone
        this.phone = phone
    }

    constructor(
        iduser: Int,
        email: String,
        password: String,
        name: String,
        lastname: String,
        birthday: String,
        address: String,
        workphone: String,
        phone: String,
        isadmin: Char
    ) {
        this.iduser = iduser
        this.email = email
        this.password = password
        this.name = name
        this.lastname = lastname
        this.birthday = birthday
        this.address = address
        this.workphone = workphone
        this.phone = phone
        this.isadmin = isadmin
    }

    constructor()

    constructor(email: String, password: String) {
        this.email = email
        this.password = password
    }

    init {
        iduser = 0
        email = ""
        password = ""
        name = ""
        lastname = ""
        birthday = "2000-04-12T06:00:00.000Z"
        address = ""
        workphone = ""
        phone = ""
        isadmin = '0'
    }


}