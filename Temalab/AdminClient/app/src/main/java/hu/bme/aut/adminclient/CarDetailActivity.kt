package hu.bme.aut.adminclient

import android.icu.text.StringPrepParseException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.google.gson.GsonBuilder
import hu.bme.aut.adminclient.model.Car
import hu.bme.aut.adminclient.model.State
import hu.bme.aut.adminclient.retrofit.RetroEnable
import hu.bme.aut.adminclient.retrofit.RetroListCars
import kotlinx.android.synthetic.main.activity_car_detail.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CarDetailActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {


    companion object{
        const val DETAILED_CAR = "detailed_car"
        const val USERNAME = "username"
        const val PASSWORD = "password"

    }

    private lateinit var retroCarStatus : RetroListCars
    private lateinit var header : String
    private lateinit var detailedCar : Car

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_detail)

        val gson = GsonBuilder().setLenient().create()
        val builder : Retrofit.Builder = Retrofit.Builder()
            .baseUrl("http://ec2-3-14-28-216.us-east-2.compute.amazonaws.com")
            .addConverterFactory(GsonConverterFactory.create(gson))
        val retrofit : Retrofit = builder.build()
         retroCarStatus = retrofit.create(RetroListCars::class.java)

        val username = intent.getStringExtra(CostumerDetailActivity.USER_NAME)
        val password = intent.getStringExtra(CostumerDetailActivity.USER_PASS)
        val loginDetails = "$username:$password"
         header = "Basic " + Base64.encodeToString(loginDetails.toByteArray(), Base64.NO_WRAP)

        detailedCar  = intent.getSerializableExtra(DETAILED_CAR) as Car
        Log.d("retrofit",detailedCar.carId.toString())




        val list_of_items = arrayOf(State.RENTABLE, State.MAINTENANCE, State.SHIPPING, State.RENTED)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, list_of_items)
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)

        spCarState.onItemSelectedListener=this
        spCarState.adapter = adapter

        tvCarColor.text = detailedCar.color
        tvCarModel.text = detailedCar.model
        tvCarKm.text = detailedCar.currentKm.toString()

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Log.d("retrofit", (spCarState.selectedItem as State).toString())
        retroCarStatus.changeCarState(header,detailedCar.carId.toString(),(spCarState.selectedItem as State).toString())
    }
}
