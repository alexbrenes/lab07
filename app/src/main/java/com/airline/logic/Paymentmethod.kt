package com.airline.logic

import java.io.Serializable

class Paymentmethod : Serializable {

    var idpaymentmethod: Int
    var paymentmethod: String

    constructor()
    constructor(idpaymentmethod: Int, paymentmethod: String) {
        this.idpaymentmethod = idpaymentmethod
        this.paymentmethod = paymentmethod
    }

    constructor(idpaymentmethod: Int) {
        this.idpaymentmethod = idpaymentmethod
    }

    init {
        idpaymentmethod = 0
        paymentmethod = ""
    }

}