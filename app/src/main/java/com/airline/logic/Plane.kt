package com.airline.logic

import com.lab04.logic.PlaneType
import java.io.Serializable

class Plane : Serializable {

    var idplane: Int
    var identifier: String
    var planetypeIdplanetype: PlaneType

    constructor()
    constructor(idplane: Int) {
        this.idplane = idplane
    }

    constructor(idplane: Int, identifier: String, planetypeIdplanetype: PlaneType) {
        this.idplane = idplane
        this.identifier = identifier
        this.planetypeIdplanetype = planetypeIdplanetype
    }

    init {
        idplane = 0
        identifier = ""
        planetypeIdplanetype = PlaneType()
    }


}