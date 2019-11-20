package hu.bme.aut.adminclient

import android.app.ActionBar
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.RequestOptions
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
        const val USER_NAME = "user.username"
        const val USER_PASS = "user.password"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_costumer_detail)
        title=intent.getStringExtra(DETAIL_FIRST)!!+" "+intent.getStringExtra(DETAIL_LAST)

        val gson = GsonBuilder().setLenient().create()
        val builder : Retrofit.Builder = Retrofit.Builder()
            .baseUrl("http://ec2-3-14-28-216.us-east-2.compute.amazonaws.com")
            .addConverterFactory(GsonConverterFactory.create(gson))
        val retrofit : Retrofit = builder.build()
        val retroEnableUser = retrofit.create(RetroEnable::class.java)

        val username = intent.getStringExtra(USER_NAME)
        val password = intent.getStringExtra(USER_PASS)
        val loginDetails = "$username:$password"
        val header : String = "Basic " + Base64.encodeToString(loginDetails.toByteArray(), Base64.NO_WRAP)

        val detailedCostumerID = intent.getIntExtra(DETAIL_ID,0)


        tvFirstName.text = intent.getStringExtra(DETAIL_FIRST)
        tvLastName.text = intent.getStringExtra(DETAIL_LAST)
        tvEmail.text = intent.getStringExtra(DETAIL_EMAIL)
        tvPhone.text = intent.getStringExtra(DETAIL_PHONE)
        tvLicenceCard.text = intent.getStringExtra(DETAIL_LICENCE)
        tvExpirationDate.text = intent.getStringExtra(DETAIL_EXPIRATION)


        val pictureUrl =
            "http://ec2-3-14-28-216.us-east-2.compute.amazonaws.com/customers/$detailedCostumerID/profile-image"
        val profileGlideUrl = GlideUrl(pictureUrl, LazyHeaders.Builder().addHeader("Authorization",header).build())
        val licenceFrontUrl =
            "http://ec2-3-14-28-216.us-east-2.compute.amazonaws.com/customers/$detailedCostumerID/driving-licence-front"
        val brontGlideUrl = GlideUrl(licenceFrontUrl, LazyHeaders.Builder().addHeader("Authorization",header).build())
        val licenceBackUrl =
            "http://ec2-3-14-28-216.us-east-2.compute.amazonaws.com/customers/$detailedCostumerID/driving-licence-back"
        val backGlideUrl = GlideUrl(licenceBackUrl, LazyHeaders.Builder().addHeader("Authorization",header).build())
        //val url = GlideUrl("https://upload.wikimedia.org/wikipedia/commons/3/3f/JPEG_example_flower.jpg", LazyHeaders.Builder().addHeader("Authorization",header).build())

         val options = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE)

        Glide.with(this)
            .load(profileGlideUrl)
            .apply(options)
            .into(ivProfilePic)

        Glide.with(this)
            .load(brontGlideUrl)
            .apply(options)
            .into(ivLicenceFront)

        Glide.with(this)
            .load(backGlideUrl)
            .apply(options)
            .into(ivLicenceBack)

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
