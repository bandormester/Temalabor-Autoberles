package hu.bme.aut.adminclient

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.Toast
import com.google.gson.GsonBuilder
import hu.bme.aut.adminclient.retrofit.RetroEnable
import hu.bme.aut.adminclient.retrofit.RetroListUsers
import kotlinx.android.synthetic.main.activity_costumer_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

class CostumerDetailActivity : AppCompatActivity() {


    companion object{
        const val DETAIL_ID = "detail.id"
        const val DETAIL_FIRST = "detail.first"
        const val DETAIL_LAST = "detail.last"
        const val DETAIL_EMAIL = "detail.email"
        const val DETAIL_PHONE = "detail.phone"
        const val DETAIL_LICENCE = "detail.licence"
        const val DETAIL_EXPIRATION = "detail.expiration"
        const val DETAIL_ENABLED = "detail.enabled"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_costumer_detail)

        var gson = GsonBuilder().setLenient().create()
        var builder : Retrofit.Builder = Retrofit.Builder()
            .baseUrl("https://penzfeldobas.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))
        var retrofit : Retrofit = builder.build()
        val retroEnableUser = retrofit.create(RetroEnable::class.java)

        val loginDetails = "admin:admin"
        val header : String = "Basic " + Base64.encodeToString(loginDetails.toByteArray(), Base64.NO_WRAP)

        val detailedCostumerID = intent.getIntExtra(DETAIL_ID,0)


        tvFirstName.text = intent.getStringExtra(DETAIL_FIRST)
        tvLastName.text = intent.getStringExtra(DETAIL_LAST)
        tvEmail.text = intent.getStringExtra(DETAIL_EMAIL)
        tvPhone.text = intent.getStringExtra(DETAIL_PHONE)
        tvLicenceCard.text = intent.getStringExtra(DETAIL_LICENCE)
        tvExpirationDate.text = intent.getStringExtra(DETAIL_EXPIRATION)

        cbApproved.isChecked = intent.getBooleanExtra(DETAIL_ENABLED, false)
        cbApproved.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if(cbApproved.isChecked){
                    val call = retroEnableUser.enableUser(header,detailedCostumerID.toString())

                    call.enqueue(object : Callback<Void>{
                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Log.d("retrofit","call failed")
                        }

                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            Log.d("retrofit","call succeeded")
                        }

                    })
                }else {
                    val call = retroEnableUser.disableUser(header,detailedCostumerID.toString())

                    call.enqueue(object : Callback<Void>{
                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Log.d("retrofit","call failed")
                        }

                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            Log.d("retrofit","call succeeded")
                        }

                    })
                }
            }

        })

    }
}
