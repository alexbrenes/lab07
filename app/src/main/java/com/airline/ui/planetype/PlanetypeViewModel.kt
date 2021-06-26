package com.airline.ui.planetype

import androidx.lifecycle.ViewModel
import com.lab04.logic.PlaneType
import com.lab04.persistance.PlaneTypes

class PlanetypeViewModel : ViewModel() {

    var planeTypes: PlaneTypes

    init {
        planeTypes = PlaneTypes.instance
    }

    fun add(year: String, capacity: String, sprows: String, rows: String, model: String, manufacturer: String) {
        val p = PlaneType(
            manufacturer,
            year.toInt(),
            model,
            capacity.toInt(),
            rows.toInt(),
            sprows.toInt())
        planeTypes.add(p)
    }

}