package hu.bme.aut.adminclient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.GsonBuilder
import hu.bme.aut.adminclient.model.Rent
import hu.bme.aut.adminclient.retrofit.RetroListRents
import kotlinx.android.synthetic.main.activity_rent_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.xml.transform.Templates

class ActiveRentDetailActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap : GoogleMap
    private lateinit var rent : Rent

    override fun onMapReady(p0: GoogleMap) {
        mMap = p0

        for(r in rent.positions){
            val m = LatLng(r.latitude,r.longitude)
            mMap.addMarker(MarkerOptions().position(m).title(r.reportedTime))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(m))
        }
    }

    private lateinit var header : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_active_rent_detail)
        rent = intent.getSerializableExtra("rent") as Rent

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mvRentMap) as SupportMapFragment
        mapFragment.getMapAsync(this)


        val username = intent.getStringExtra("username")
        val password = intent.getStringExtra("password")
        val loginDetails = "$username:$password"
        header  = "Basic " + Base64.encodeToString(loginDetails.toByteArray(), Base64.NO_WRAP)


        tvRentActualStart.text = rent.actualStartTime
        tvRentPlannedStart.text = rent.plannedStartTime
        tvRentActualEnd.text = rent.actualEndTime
        tvRentPlannedEnd.text = rent.plannedEndTime
        tvRentStartStation.append(rent.startStationId.toString())
        tvRendEndStation.append(rent.endStationId.toString())


        btCloseRent.setOnClickListener {//TODO
            val gson = GsonBuilder().setLenient().create()
            val builder : Retrofit.Builder = Retrofit.Builder()
                .baseUrl("http://ec2-3-14-28-216.us-east-2.compute.amazonaws.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
            val retrofit : Retrofit = builder.build()
            val retroListRents = retrofit.create(RetroListRents::class.java)
            val call = retroListRents.requestPosition(header, rent.rentId.toString())
            Log.d("retrofit",rent.rentId.toString())

            call.enqueue(object : Callback<Boolean?>{
                override fun onResponse(call: Call<Boolean?>, response: Response<Boolean?>) {
                    Log.d("retrofit", response.code().toString())
                }

                override fun onFailure(call: Call<Boolean?>, t: Throwable) {
                    Log.d("retrofit", t.message)
                    Log.d("retrofit",t.cause.toString())
                }

            })
        }
    }
}
