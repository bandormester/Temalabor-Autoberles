package hu.bme.aut.adminclient.model

import java.io.Serializable

class Rent : Serializable{
    var rentId : Int = 0
    var carId : Int = 0
    var plannedStartTime : String = ""
    var plannedEndTime : String = ""
    var startStationId : Int = 0
    var endStationId : Int = 0
    var actualStartTime : String = ""
    var actualEndTime : String = ""
    var state : String = State.RENTABLE.toString()
    var mine : Boolean = false
    var imageIdsBefore : MutableList<Int> = mutableListOf()
    var imageIdsAfter : MutableList<Int> = mutableListOf()
}