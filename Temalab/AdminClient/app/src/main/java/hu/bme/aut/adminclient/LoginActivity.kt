package hu.bme.aut.adminclient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class LoginActivity : AppCompatActivity(){

    val builder : Retrofit.Builder = Retrofit.Builder()
        .baseUrl("https://penzfeldobas.herokuapp.com/")
        .addConverterFactory(GsonConverterFactory.create())

    val retrofit : Retrofit = builder.build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btLogin.setOnClickListener{

            val retroLogin = retrofit.create(RetrofitLogin::class.java)

            val loginDetails = "admin:admin"
            val authHeader = "Basic " + Base64.encodeToString(loginDetails.toByteArray(),Base64.NO_WRAP)

            val call= retroLogin.tryLogin(authHeader)

            call.enqueue(object : Callback<String>{
                override fun onResponse(call: Call<String>?, response: Response<String>?){
                    println("segg")
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.v("retrofit", "call failed")
                }
            })

            println("asd")
            //println(asd)


            val myIntent : Intent = Intent()
            myIntent.setClass(this@LoginActivity, AfterLoginActivity::class.java)
            startActivity(myIntent)
        }
    }
}
