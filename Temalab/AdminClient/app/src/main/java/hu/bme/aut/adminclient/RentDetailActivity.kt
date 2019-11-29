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

class RentDetailActivity : AppCompatActivity() {

    private lateinit var header : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rent_detail)

        val username = intent.getStringExtra("username")
        val password = intent.getStringExtra("password")
        val loginDetails = "$username:$password"
        header  = "Basic " + Base64.encodeToString(loginDetails.toByteArray(), Base64.NO_WRAP)

        val rent = intent.getSerializableExtra("rent") as Rent
        tvRentActualStart.text = rent.actualStartTime
        tvRentPlannedStart.text = rent.plannedStartTime
        tvRentActualEnd.text = rent.actualEndTime
        tvRentPlannedEnd.text = rent.plannedEndTime
        tvRentStartStation.append(rent.startStationId.toString())
        tvRendEndStation.append(rent.endStationId.toString())

        if(rent.imageIdsAfter.size!=0){
            val pictureUrl =
                "http://ec2-3-14-28-216.us-east-2.compute.amazonaws.com/images/"+rent.imageIdsAfter[0].toString()
            val glideUrl = GlideUrl(pictureUrl, LazyHeaders.Builder().addHeader("Authorization",header).build())

            val option = RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)

            Glide.with(this)
                .load(glideUrl)
                .apply(option)
                .into(ivRentAfter)
        } else Log.d("retrofit", rent.imageIdsAfter.size.toString())

        if(rent.imageIdsBefore.size!=0){
            val pictureUrl =
                "http://ec2-3-14-28-216.us-east-2.compute.amazonaws.com/images/"+rent.imageIdsBefore[0].toString()
            val glideUrl = GlideUrl(pictureUrl, LazyHeaders.Builder().addHeader("Authorization",header).build())

            val option = RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)

            Glide.with(this)
                .load(glideUrl)
                .apply(option)
                .into(ivRentBefore)
        } else Log.d("retrofit", rent.imageIdsBefore.size.toString())

        btCloseRent.setOnClickListener {
            val gson = GsonBuilder().setLenient().create()
            val builder : Retrofit.Builder = Retrofit.Builder()
                .baseUrl("http://ec2-3-14-28-216.us-east-2.compute.amazonaws.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
            val retrofit : Retrofit = builder.build()
            val retroListRents = retrofit.create(RetroListRents::class.java)
            val call = retroListRents.closeRent(header, rent.rentId.toString())

            call.enqueue(object : Callback<Void> {
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.d("retrofit",t.message)
                }

                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    Log.d("retrofit",response.code().toString())
                }

            })
        }
    }
}
