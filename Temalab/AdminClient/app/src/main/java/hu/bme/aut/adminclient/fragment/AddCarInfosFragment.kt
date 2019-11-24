package hu.bme.aut.adminclient.fragment

import android.content.Context
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.google.gson.GsonBuilder
import hu.bme.aut.adminclient.AddCarActivity
import hu.bme.aut.adminclient.CostumerDetailActivity
import hu.bme.aut.adminclient.R
import hu.bme.aut.adminclient.model.EngineType
import hu.bme.aut.adminclient.model.State
import hu.bme.aut.adminclient.retrofit.RetroListCars
import kotlinx.android.synthetic.main.activity_car_detail.*
import kotlinx.android.synthetic.main.fragment_new_car.*
import kotlinx.android.synthetic.main.fragment_new_car.view.*
import kotlinx.android.synthetic.main.fragment_new_car_infos.*
import kotlinx.android.synthetic.main.fragment_new_car_infos.view.*
import kotlinx.android.synthetic.main.fragment_new_car_infos.view.btCreate
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AddCarInfosFragment : Fragment() {

    lateinit var myView: View
    lateinit var retroCarStatus: RetroListCars

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myView = inflater.inflate(R.layout.fragment_new_car_infos,container,false)
        return myView
    }

    override fun onStart() {
        super.onStart()


        val gson = GsonBuilder().setLenient().create()
        val builder : Retrofit.Builder = Retrofit.Builder()
            .baseUrl("http://ec2-3-14-28-216.us-east-2.compute.amazonaws.com")
            .addConverterFactory(GsonConverterFactory.create(gson))
        val retrofit : Retrofit = builder.build()
        retroCarStatus = retrofit.create(RetroListCars::class.java)

        val username = activity!!.intent.getStringExtra(CostumerDetailActivity.USER_NAME)
        val password = activity!!.intent.getStringExtra(CostumerDetailActivity.USER_PASS)
        val loginDetails = "$username:$password"
        val header = "Basic " + Base64.encodeToString(loginDetails.toByteArray(), Base64.NO_WRAP)

        val list_of_items = arrayOf(EngineType.ELECTRIC, EngineType.BENZINE, EngineType.DIESEL)
        val adapter = ArrayAdapter(activity!!.baseContext, android.R.layout.simple_spinner_item, list_of_items)
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        myView.spCarStation.adapter = adapter

        btCreate.setOnClickListener {
            if(myView.etCarColor.text.toString() == "" || myView.etCarLicencePlate.text.toString() == "" || myView.etCarKm.text.toString() == "" || myView.etCarPrice.text.toString() == "") Toast.makeText(activity!!.baseContext, "Fill out everything", Toast.LENGTH_LONG).show()
            else{
            Toast.makeText(activity!!.baseContext,"Car created",Toast.LENGTH_LONG).show()
            activity!!.onBackPressed()
                (activity!! as AddCarActivity).passInfos(myView.etCarColor.text.toString(), myView.etCarLicencePlate.text.toString(), myView.etCarKm.text.toString(), myView.etCarPrice.text.toString())
                (activity!! as AddCarActivity).createCar()
            }
        }

        btBack.setOnClickListener {
            Toast.makeText(activity!!.baseContext,"Cancelled",Toast.LENGTH_LONG).show()
            val t = activity!!.supportFragmentManager.beginTransaction()
            t.replace(R.id.flFragmentPlace, AddCarDialog())
            t.commit()
        }
    }
}