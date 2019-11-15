package hu.bme.aut.adminclient.retrofit

import hu.bme.aut.adminclient.model.Car
import hu.bme.aut.adminclient.model.Costumer
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface RetroListCars {
    @GET("/cars")
    fun getCarList(@Header("Authorization") authHeader : String) : Call<List<Car>>
}