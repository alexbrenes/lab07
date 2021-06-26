package com.airline.persistance

import androidx.lifecycle.MutableLiveData
import com.airline.logic.City
import com.airline.logic.Country

class Cities {

    var sequence: Int
    var items = MutableLiveData<List<City>>()

    init {
        sequence  = 0
        items.value = listOf(
            City(++sequence),
            City(++sequence),
            City(++sequence)
        )
    }

    private object HOLDER {
        val INSTANCE = Cities()
    }

    companion object {
        val instance: Cities by lazy {
            HOLDER.INSTANCE
        }
    }

    fun add(city: City) {
        items.value = items.value!! + listOf(city)
    }

    fun delete(id: Int) {
        items.value = items.value!!.filter { it.idcity != id }
    }
}