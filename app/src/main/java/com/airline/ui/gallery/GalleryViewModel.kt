package com.lab04.ui.gallery

import androidx.lifecycle.ViewModel
import com.lab04.logic.PlaneType
import com.lab04.persistance.PlaneTypes

class GalleryViewModel : ViewModel() {

    var archived: ArrayList<PlaneType>
    var planeTypes: PlaneTypes

    init {
        archived = ArrayList()
        planeTypes = PlaneTypes.instance
    }

    fun delete(planeType: PlaneType, position: Int) {
        archived.add(planeType)
        planeTypes.delete(planeType)
    }

    fun undo(position: Int) {
        if (archived.size <= 0)
            return
        planeTypes.add(archived.get(archived.size - 1), position)
    }

    fun at(idx: Int): PlaneType {
        return planeTypes.items.value?.get(idx)!!
    }

}