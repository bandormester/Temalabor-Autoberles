package hu.bme.aut.adminclient

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.google.gson.GsonBuilder
import hu.bme.aut.adminclient.fragment.AddCarDialog
import hu.bme.aut.adminclient.model.Car
import hu.bme.aut.adminclient.retrofit.RetroListCars
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AddCarActivity : FragmentActivity() {

    private lateinit var newCar : Car

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_car)

        newCar = Car()

        val fm = supportFragmentManager.beginTransaction()
        fm.add(R.id.flFragmentPlace, AddCarDialog())
        fm.commit()


    }

    public fun createCar(){
        val gson = GsonBuilder().setLenient().create()
        val builder : Retrofit.Builder = Retrofit.Builder()
            .baseUrl("http://ec2-3-14-28-216.us-east-2.compute.amazonaws.com")
            .addConverterFactory(GsonConverterFactory.create(gson))
        val retrofit : Retrofit = builder.build()
        val retroCarStatus = retrofit.create(RetroListCars::class.java)

        val username = intent.getStringExtra("username")
        val password = intent.getStringExtra("password")
        val loginDetails = "$username:$password"
        val header = "Basic " + Base64.encodeToString(loginDetails.toByteArray(), Base64.NO_WRAP)

        val call = retroCarStatus.registerCar(header,newCar.licencePlate,newCar.brand,newCar.engineType, newCar.model, newCar.color, newCar.price, newCar.currentKm, 1)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Log.d("retrofit","creating succeeded")
                Log.d("retrofit",response.code().toString())
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d("retrofit","create failed")
            }

    })
    }

    public fun passInfos(color : String, licencePlate : String, km : String, price : String) {
        newCar.color = color
        newCar.licencePlate = licencePlate
        newCar.currentKm = Integer.parseInt(km)
        newCar.price = Integer.parseInt(price)
        newCar.station!!.stationId = 1
    }

    public fun passDefaults(engType : String, model : String, brand : String){
        newCar.engineType = engType
        newCar.model = model
        newCar.brand = brand
    }
}
