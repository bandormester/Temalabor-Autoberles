package hu.bme.aut.adminclient

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import com.google.gson.GsonBuilder
import hu.bme.aut.adminclient.retrofit.Costumer
import hu.bme.aut.adminclient.retrofit.RetroListUsers
import hu.bme.aut.adminclient.retrofit.RetroLogin
import kotlinx.android.synthetic.main.activity_after_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AfterLoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_after_login)

        var gson = GsonBuilder().setLenient().create()

        var builder : Retrofit.Builder = Retrofit.Builder()
            .baseUrl("https://penzfeldobas.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))

        var retrofit : Retrofit = builder.build()

        val retroListUsers = retrofit.create(RetroListUsers::class.java)

        val call = retroListUsers.getUserList()

        call.enqueue(object : Callback<List<Costumer>> {
            override fun onFailure(call: Call<List<Costumer>>, t: Throwable) {
                Log.d("retrofit", "Listing failed")
            }

            override fun onResponse(
                call: Call<List<Costumer>>,
                response: Response<List<Costumer>>
            ) {
                Log.d("retrofit", "Listing succeeded")
                var Users = response.body()

                var Usernames = arrayOfNulls<String>(Users!!.size)

                for(i in 0 until Users.size){
                    Usernames[i] = Users[i].username
                }

                val adapter = ArrayAdapter(applicationContext, android.R.layout.simple_list_item_1, Usernames)
                lvUsers.adapter = adapter
            }

        })

    }
}
