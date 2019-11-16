package hu.bme.aut.adminclient.model

import java.io.Serializable

class Car : Serializable {
    var carId : Int = 0
    var licencePlate : String = ""
    var currentKm : Int = 0
    var brand : String = ""
    var engineType : EngineType = EngineType.DIESEL
    var model : String = ""
    var color : String = ""
    var state : State = State.RENTABLE
    var price : Int = 1
    var station : Station = Station()
}