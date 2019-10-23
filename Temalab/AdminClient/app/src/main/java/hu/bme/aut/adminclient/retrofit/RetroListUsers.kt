package hu.bme.aut.adminclient.retrofit

import retrofit2.Call
import retrofit2.http.GET

interface RetroListUsers {

    @GET("/users")
    fun getUserList() : Call<List<Costumer>>
}