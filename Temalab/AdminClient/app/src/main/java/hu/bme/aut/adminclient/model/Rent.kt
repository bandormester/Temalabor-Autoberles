package hu.bme.aut.adminclient.model

import java.io.Serializable

class Rent : Serializable{
    var rentId : Int = 0
    var carId : Int = 0
    var carBrand : String = ""
    var carModel : String = ""
    var plannedStartTime : String = ""
    var plannedEndTime : String = ""
    var startStationId : Int = 0
    var startStationName : String = ""
    var endStationId : Int = 0
    var endStationName : String = ""
    var actualStartTime : String? = ""
    var actualEndTime : String? = ""
    var state : String = ""
    var mine : Boolean = false
    var imageIdsBefore : MutableList<Int> = mutableListOf()
    var imageIdsAfter : MutableList<Int> = mutableListOf()
    var positionReportRequested : Boolean = false
    var positions : MutableList<CarPosition> = mutableListOf()
}