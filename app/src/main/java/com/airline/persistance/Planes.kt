package com.airline.persistance

import androidx.lifecycle.MutableLiveData
import com.airline.logic.Plane

class Planes {

    var sequence: Int
    var items = MutableLiveData<List<Plane>>()

    init{
        sequence = 0
        items.value = listOf<Plane>(
            Plane(++sequence),
            Plane(++sequence),
            Plane(++sequence)
        )
    }

    private object HOLDER {
        val INSTANCE = Planes()
    }

    companion object {
        val instance: Planes by lazy {
            HOLDER.INSTANCE
        }
    }

    fun add(plane: Plane) {
        items.value = items.value!! + listOf(plane)
    }

    fun delete(id: Int) {
        items.value = items.value!!.filter { it.idplane != id }
    }
    
}