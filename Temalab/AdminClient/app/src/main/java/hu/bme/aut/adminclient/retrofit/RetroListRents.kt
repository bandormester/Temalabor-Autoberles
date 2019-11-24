package hu.bme.aut.adminclient.retrofit

import hu.bme.aut.adminclient.model.Costumer
import hu.bme.aut.adminclient.model.Rent
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface RetroListRents {
    @GET("/rents/unclosed")
    fun getUserList(@Header("Authorization") authHeader : String) : Call<List<Rent>>

    @POST("/rents/{rentid}/accept")
    fun closeRent(@Header("Authorization") authHeader : String,
                  @Path("rentid") rentId : String) : Call<Void>
}