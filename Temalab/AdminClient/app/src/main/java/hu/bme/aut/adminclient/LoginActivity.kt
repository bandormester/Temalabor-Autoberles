package hu.bme.aut.adminclient

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.gson.GsonBuilder
import hu.bme.aut.adminclient.retrofit.RetroLogin
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class LoginActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        relativelayout.setOnTouchListener { v, event ->
            v?.performClick()
            v?.hideKeyboard()
            v?.onTouchEvent(event) ?: true
        }

        val gson = GsonBuilder().setLenient().create()

        val builder : Retrofit.Builder = Retrofit.Builder()
            .baseUrl("http://ec2-3-14-28-216.us-east-2.compute.amazonaws.com")
            .addConverterFactory(GsonConverterFactory.create(gson))

        val retrofit : Retrofit = builder.build()


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

                    when(response?.code()){
                        200 -> {
                            Log.d("retrofit","Login Successful")
                            Log.d("retrofit",response.toString())

                            val myIntent : Intent = Intent()
                            myIntent.setClass(this@LoginActivity, ListCarsActivity::class.java)
                            myIntent.putExtra("username",username)
                            myIntent.putExtra("password",password)
                            startActivity(myIntent)
                        }
                        else -> {
                            //Toast.makeText(this@LoginActivity,"Login failed",Toast.LENGTH_LONG).show()
                            Log.d("retrofit","Login failed")
                            Log.d("retrofit",response.toString())
                        }
                    }
                }

                override fun onFailure(call: Call<String>?, t: Throwable) {
                    //Toast.makeText(this@LoginActivity,     t.message,Toast.LENGTH_LONG).show
                    Log.d("retrofit", "call failed")
                    Log.d("retrofit",t.message)
                }
            })

        }
    }
    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}
