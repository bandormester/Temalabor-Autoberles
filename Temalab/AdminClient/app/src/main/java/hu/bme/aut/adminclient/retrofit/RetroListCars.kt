package hu.bme.aut.adminclient.retrofit

import hu.bme.aut.adminclient.model.Car
import hu.bme.aut.adminclient.model.Costumer
import hu.bme.aut.adminclient.model.EngineType
import retrofit2.Call
import retrofit2.http.*

interface RetroListCars {
    @GET("/cars")
    fun getCarList(@Header("Authorization") authHeader : String) : Call<List<Car>>

    @POST("/cars/{carid}/change/state")
    fun changeCarState(@Header("Authorization") authHeader: String,
                       @Path("carid") carid: String,
                       @Query("state") newState: String) : Call<Car>

    @POST("/cars/register")
    fun registerCar(@Header("Authorization") authHeader: String,
                    @Query("licencePlate") licencePlate: String,
                    @Query("brand") brand: String,
                    @Query("engineType") engineType: EngineType,
                    @Query("model") model: String,
                    @Query("color") color: String,
                    @Query("price") price: Int,
                    @Query("kmHour") kmHour: Int,
                    @Query("stationId") stationId: Int
                    ) : Call<Void>

}