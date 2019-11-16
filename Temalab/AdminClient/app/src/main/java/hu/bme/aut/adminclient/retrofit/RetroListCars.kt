package hu.bme.aut.adminclient.retrofit

import hu.bme.aut.adminclient.model.Car
import hu.bme.aut.adminclient.model.Costumer
import retrofit2.Call
import retrofit2.http.*

interface RetroListCars {
    @GET("/cars")
    fun getCarList(@Header("Authorization") authHeader : String) : Call<List<Car>>

    @POST("/cars/{carid}/change/state")
    fun changeCarState(@Header("Authorization") authHeader: String,
                       @Path("carid") carid: String,
                       @Query("state") newState: String) : Call<Car>

}