package com.airline.persistance

import androidx.lifecycle.MutableLiveData
import com.airline.logic.Paymentmethod

class Paymentmethods {
    var sequence: Int
    var items = MutableLiveData<List<Paymentmethod>>()

    init{
        sequence = 0
        items.value = listOf<Paymentmethod>(
            Paymentmethod(++sequence),
            Paymentmethod(++sequence),
            Paymentmethod(++sequence)
        )
    }

    private object HOLDER {
        val INSTANCE = Paymentmethods()
    }

    companion object {
        val instance: Paymentmethods by lazy {
            HOLDER.INSTANCE
        }
    }

    fun add(paymentmethod: Paymentmethod) {
        items.value = items.value!! + listOf(paymentmethod)
    }

    fun delete(id: Int) {
        items.value = items.value!!.filter { it.idpaymentmethod != id }
    }
}