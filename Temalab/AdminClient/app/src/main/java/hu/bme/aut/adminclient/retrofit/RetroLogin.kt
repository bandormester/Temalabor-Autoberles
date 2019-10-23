package hu.bme.aut.adminclient.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface RetroLogin {

    @GET("/login/admin")
    fun tryLogin(@Header("Authorization") authHeade : String) : Call<String>

}