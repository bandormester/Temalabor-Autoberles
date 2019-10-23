package hu.bme.aut.adminclient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import com.google.gson.GsonBuilder
import hu.bme.aut.adminclient.retrofit.RetroLogin
import hu.bme.aut.adminclient.retrofit.RetrofitLogin
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class LoginActivity : AppCompatActivity(){
    var gson = GsonBuilder().setLenient().create()

    var builder : Retrofit.Builder = Retrofit.Builder()
        .baseUrl("https://penzfeldobas.herokuapp.com/")
        .addConverterFactory(GsonConverterFactory.create(gson))

    var retrofit : Retrofit = builder.build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btLogin.setOnClickListener{

            val retroLogin = retrofit.create(RetroLogin::class.java)

            val username = etUsername.text.toString()
            val password = etPassword.text.toString()
            val loginDetails = "$username:$password"

            val authHeader = "Basic " + Base64.encodeToString(loginDetails.toByteArray(),Base64.NO_WRAP)

            val call= retroLogin.tryLogin(authHeader)

            call.enqueue(object : Callback<String>{
                override fun onResponse(call: Call<String>?, response: Response<String>?){
                    Log.d("retrofit","call succeeded")
                    Log.d("retrofit", response?.body()?:"Szar ugy")

                    when(response?.code()){
                        200 -> {
                            Log.d("retrofit","Sikeres bejelentkezes")
                            Log.d("retrofit",response.toString())

                            val myIntent : Intent = Intent()
                            myIntent.setClass(this@LoginActivity, AfterLoginActivity::class.java)
                            startActivity(myIntent)
                        }
                        else -> {
                            Log.d("retrofit","Sikertelen bejelentkezés")
                            Log.d("retrofit",response.toString())
                        }
                    }
                }

                override fun onFailure(call: Call<String>?, t: Throwable) {
                    Log.d("retrofit", "call failed")
                    Log.d("retrofit",t.message)
                }
            })

        }
    }
}
