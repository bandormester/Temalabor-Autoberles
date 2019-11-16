package hu.bme.aut.adminclient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import com.google.gson.GsonBuilder
import hu.bme.aut.adminclient.adapter.CarAdapter
import hu.bme.aut.adminclient.adapter.CostumerAdapter
import hu.bme.aut.adminclient.model.Car
import hu.bme.aut.adminclient.model.Costumer
import hu.bme.aut.adminclient.retrofit.RetroListCars
import hu.bme.aut.adminclient.retrofit.RetroListUsers
import kotlinx.android.synthetic.main.activity_list_cars.*
import kotlinx.android.synthetic.main.activity_list_users.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ListCarsActivity : AppCompatActivity(), CarAdapter.CarItemClickListener {


    private lateinit var carAdapter: CarAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_cars)
        carAdapter = CarAdapter()
        RecyclerViewCars.adapter = carAdapter
    }

    lateinit var username : String
    lateinit var password : String

    override fun onStart() {
        super.onStart()

        val gson = GsonBuilder().setLenient().create()
        val builder : Retrofit.Builder = Retrofit.Builder()
            .baseUrl("http://ec2-3-14-28-216.us-east-2.compute.amazonaws.com")
            .addConverterFactory(GsonConverterFactory.create(gson))
        val retrofit : Retrofit = builder.build()
        val retroListCars = retrofit.create(RetroListCars::class.java)
        username = intent.getStringExtra("username")?:""
        password = intent.getStringExtra("password")?:""
        val loginDetails = "$username:$password"
        val header : String = "Basic " + Base64.encodeToString(loginDetails.toByteArray(), Base64.NO_WRAP)
        val call = retroListCars.getCarList(header)

        call.enqueue(object : Callback<List<Car>> {
            override fun onFailure(call: Call<List<Car>>, t: Throwable) {
                Log.d("retrofit", "Listing failed")
            }

            override fun onResponse(
                call: Call<List<Car>>,
                response: Response<List<Car>>
            ) {
                Log.d("retrofit", "Listing succeeded")
                var Cars = response.body()?: listOf<Car>()
                setupRecyclerView(Cars)

                Log.d("retrofit", Cars[1].model)
            }
        })
    }
    private fun setupRecyclerView(CarList : List<Car>) {

        carAdapter.itemClickListener = this
        carAdapter.clear()
        carAdapter.addAll(CarList)
        RecyclerViewCars.adapter = carAdapter
    }

    override fun onCarSelected(car: Car) {
        Log.d("detview","car clicked")
        val intent = Intent(this, CarDetailActivity::class.java)
        //intent.putExtra(CarDetailActivity.CAR_ID, car.carId)
        //intent.putExtra(CarDetailActivity.CAR_MODEL, car.model)
        intent.putExtra(CarDetailActivity.USERNAME, username)
        intent.putExtra(CarDetailActivity.PASSWORD, password)
        intent.putExtra(CarDetailActivity.DETAILED_CAR,car)
        startActivity(intent)
    }
}
