package hu.bme.aut.adminclient.model

class Car {
    var carId : Int = 0
    var licencePlate : String = ""
    var currentKm : Int = 0
    var brand : String = ""
    var engineType : EngineType = EngineType.DIESEL
    var model : String = ""
    var color : String = ""
    var state : String = ""
    var price : Int = 1
    var station : Station = Station()
}