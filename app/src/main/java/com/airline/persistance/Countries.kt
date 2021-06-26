package com.airline.persistance

import androidx.lifecycle.MutableLiveData
import com.airline.logic.Country

class Countries {

    var sequence: Int
    var items = MutableLiveData<List<Country>>()

    init {
        sequence = 0
        items.value = listOf(
            Country(++sequence, "CRI", "Costa Rica"),
            Country(++sequence, "AIA", "Anguila"),
            Country(++sequence, "ATG", "Antigua y Barbuda")
        )
    }

    private object HOLDER {
        val INSTANCE = Countries()
    }

    companion object {
        val instance: Countries by lazy {
            HOLDER.INSTANCE
        }
    }

    fun add(country: Country) {
        items.value = items.value!! + listOf(country)
    }

    fun delete(id: Int) {
        items.value = items.value!!.filter { it.idcountry != id }
    }
}