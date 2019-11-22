package hu.bme.aut.adminclient.model

import java.io.Serializable

class Station : Serializable{
    var stationId : Int = 0
    var longitude : Double = 0.0
    var latitude : Double = 0.0
    var name : String = ""
}