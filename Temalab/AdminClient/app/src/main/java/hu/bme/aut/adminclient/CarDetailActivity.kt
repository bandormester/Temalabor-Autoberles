package hu.bme.aut.adminclient

import android.icu.text.StringPrepParseException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toolbar
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.GsonBuilder
import hu.bme.aut.adminclient.fragment.AddCarDialog
import hu.bme.aut.adminclient.model.Car
import hu.bme.aut.adminclient.model.EngineType
import hu.bme.aut.adminclient.model.State
import hu.bme.aut.adminclient.model.Station
import hu.bme.aut.adminclient.retrofit.RetroEnable
import hu.bme.aut.adminclient.retrofit.RetroListCars
import kotlinx.android.synthetic.main.activity_car_detail.*
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CarDetailActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener, OnMapReadyCallback{
    private lateinit var mMap : GoogleMap

    override fun onMapReady(p0: GoogleMap) {
        mMap = p0

        val sy = LatLng(detailedCar.station!!.latitude,detailedCar.station!!.longitude)
        mMap.addMarker(MarkerOptions().position(sy).title(detailedCar.station!!.name))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sy))
    }


    companion object{
        const val DETAILED_CAR = "detailed_car"
        const val USERNAME = "username"
        const val PASSWORD = "password"

    }

    private lateinit var retroCarStatus : RetroListCars
    private lateinit var header : String
    private lateinit var detailedCar : Car
    private lateinit var list_of_stations : List<Station>
    private var initSpinner = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_detail)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mvCarMap) as SupportMapFragment
        mapFragment.getMapAsync(this)

        detailedCar  = intent.getSerializableExtra(DETAILED_CAR) as Car
        title = detailedCar.brand+" "+detailedCar.model


        val gson = GsonBuilder().setLenient().create()
        val builder : Retrofit.Builder = Retrofit.Builder()
            .baseUrl("http://ec2-3-14-28-216.us-east-2.compute.amazonaws.com")
            .addConverterFactory(GsonConverterFactory.create(gson))
        val retrofit : Retrofit = builder.build()
         retroCarStatus = retrofit.create(RetroListCars::class.java)

        val username = intent.getStringExtra(CarDetailActivity.USERNAME)
        val password = intent.getStringExtra(CarDetailActivity.PASSWORD)
        val loginDetails = "$username:$password"
         header = "Basic " + Base64.encodeToString(loginDetails.toByteArray(), Base64.NO_WRAP)


        Log.d("retrofit",detailedCar.carId.toString())

        var call = retroCarStatus.getCarStateValues().enqueue(object : Callback<List<String>>{
            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                val list_of_items = response.body()?: listOf<String>()
                val adapter = ArrayAdapter(this@CarDetailActivity, android.R.layout.simple_spinner_item, list_of_items)
                adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
                spCarState.adapter = adapter
                spCarState.setSelection(adapter.getPosition(detailedCar.state))
                spCarState.onItemSelectedListener=this@CarDetailActivity
            }

        })


         call = retroCarStatus.getStations().enqueue(object : Callback<List<Station>>{
            override fun onFailure(call: Call<List<Station>>, t: Throwable) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onResponse(call: Call<List<Station>>, response: Response<List<Station>>) {
                list_of_stations = response.body()?: listOf()
                val list_of_items = mutableListOf<String>()
                for(s in list_of_stations) list_of_items.add(s.name)
                val adapter = ArrayAdapter(this@CarDetailActivity, android.R.layout.simple_spinner_item, list_of_items)
                adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
                spCarLocation.adapter = adapter
                spCarLocation.setSelection(adapter.getPosition(detailedCar.station!!.name))
                spCarLocation.onItemSelectedListener=this@CarDetailActivity
            }

        })

        tvCarColor.text = detailedCar.color
        tvCarKm.text = (detailedCar.currentKm.toString()+" Km")
        tvCarPrice.text = (detailedCar.price.toString()+" Ft/day")
        tvCarLicencePlate.text = detailedCar.licencePlate

        when(detailedCar.engineType){
            EngineType.DIESEL.toString() -> ivCarDetailPic.setImageResource(R.mipmap.ic_disel)
            EngineType.BENZINE.toString() -> ivCarDetailPic.setImageResource(R.mipmap.ic_benzine)
            EngineType.ELECTRIC.toString() -> ivCarDetailPic.setImageResource(R.mipmap.ic_electric)
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Log.d("retrofit", spCarState.selectedItem.toString())
        Log.d("retrofit",initSpinner.toString())
        if(initSpinner<2) initSpinner++
            else
            when(parent){
            spCarState -> {
                Log.d("retrofit","reached")
                val call = retroCarStatus.changeCarState(header,detailedCar.carId.toString(),spCarState.selectedItem.toString())
                call.enqueue(object : Callback<Car> {
                    override fun onResponse(call: Call<Car>, response: Response<Car>) {
                        Log.d("retrofit","call succeeded")
                        //Log.d("retrofit",response.code().toString())
                        Log.d("retrofit",response.body()!!.state)
                    }

                    override fun onFailure(call: Call<Car>, t: Throwable) {
                        Log.d("retrofit","call failed")
                    }

                })
            }

            spCarLocation -> {
                val id = list_of_stations.get(position).stationId
                val call = retroCarStatus.changeCarStation(header,detailedCar.carId.toString(),id.toString())
                call.enqueue(object : Callback<Car> {
                    override fun onResponse(call: Call<Car>, response: Response<Car>) {
                        Log.d("retrofit","call succeeded")
                        Log.d("retrofit", response.body()!!.station!!.name)
                        Log.d("retrofit", response.message())
                        Log.d("retrofit", response.code().toString())
                    }

                    override fun onFailure(call: Call<Car>, t: Throwable) {
                        Log.d("retrofit","call failed")
                    }

                })
            }
            else -> Log.d("retrofit","szar")
        }
    }
}
