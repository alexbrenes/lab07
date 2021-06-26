package com.lab04.persistance

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.airline.logic.Journey
import com.lab04.logic.PlaneType
import java.util.ArrayList

class PlaneTypes {

    private var sequence: Int = 0
    val items = MutableLiveData<List<PlaneType>>()

    private object HOLDER {
        val INSTANCE = PlaneTypes()
    }

    companion object {
        val instance: PlaneTypes by lazy {
            HOLDER.INSTANCE
        }
    }

    init {
        loadItems()
    }

    private fun loadItems() {
        items.value = listOf(
            PlaneType(++sequence, "Manufacturer 1", 2007, "LMAO-BOX", 150, 45, 6),
            PlaneType(++sequence, "Manufacturer 2", 2007, "PDA-11", 150, 45, 6),
            PlaneType(++sequence, "Manufacturer 3", 2007, "SKU-32", 150, 45, 6)
        )
    }

    fun delete(planeType: PlaneType){
        items.value = items.value!!.filter { it.idplanetype != planeType.idplanetype }
    }

    fun add(planeType: PlaneType, position: Int = items.value?.size!!) {
        items.value = items.value?.slice(0..(position - 1))!! + listOf(planeType) +
                items.value?.slice(position..(items.value?.size!! - 1))!!
    }

}