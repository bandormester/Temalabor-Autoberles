package hu.bme.aut.adminclient.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface RetroEnable {
    @POST("/register/accept/{id}")
    fun enableUser(@Header("Authorization") authHeader : String,
                   @Path("id") userId : String) : Call<Void>

    @POST("/register/decline/{id}")
    fun disableUser(@Header("Authorization") authHeader : String,
                    @Path("id") userId : String) : Call<Void>
}