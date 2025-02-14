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
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.appcompat.widget.Toolbar
import com.google.gson.GsonBuilder
import hu.bme.aut.adminclient.retrofit.RetroLogin
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class LoginActivity : AppCompatActivity(){

    lateinit var username : String
    lateinit var password : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "Log in"

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

            username = etUsername.text.toString()
            password = etPassword.text.toString()
            val loginDetails = "$username:$password"


            val authHeader = "Basic " + Base64.encodeToString(loginDetails.toByteArray(),Base64.NO_WRAP)

            val call= retroLogin.tryLogin(authHeader)

            call.enqueue(object : Callback<String>{
                override fun onResponse(call: Call<String>?, response: Response<String>?){
                    when(response?.code()){
                        200 -> {
                            Log.d("retrofit","Login Successful")

                            val myIntent : Intent = Intent()
                            myIntent.setClass(this@LoginActivity, NavigationActivity::class.java)
                            myIntent.putExtra("username",username)
                            myIntent.putExtra("password",password)
                            startActivity(myIntent)
                        }
                        else -> {
                            Log.d("retrofit","Login failed")
                            Toast.makeText(this@LoginActivity,"Wrong username or password",Toast.LENGTH_LONG).show()
                        }
                    }
                }

                override fun onFailure(call: Call<String>?, t: Throwable) {
                    Log.d("retrofit", "call failed")
                }
            })

        }
    }
    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}
