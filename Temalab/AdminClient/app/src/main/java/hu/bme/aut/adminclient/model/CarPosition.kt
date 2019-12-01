package hu.bme.aut.adminclient.model

import java.io.Serializable

class CarPosition : Serializable {
    var longitude : Double = 0.0
    var latitude : Double = 0.0
    var reportedTime : String = ""
}