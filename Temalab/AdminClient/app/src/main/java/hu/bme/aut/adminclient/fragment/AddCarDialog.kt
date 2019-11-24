package hu.bme.aut.adminclient.fragment

import android.content.Context
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
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
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AddCarDialog : Fragment() {

    lateinit var myView: View
    lateinit var retroCarStatus: RetroListCars

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myView = inflater.inflate(R.layout.fragment_new_car,container, false)
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
        myView.spCarEngine.adapter = adapter
        spCarEngine.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (spCarEngine.getItemAtPosition(position)) {
                    EngineType.ELECTRIC -> myView.ivAddCarPic.setImageResource(R.mipmap.ic_electric)
                    EngineType.DIESEL -> myView.ivAddCarPic.setImageResource(R.mipmap.ic_disel)
                    EngineType.BENZINE -> myView.ivAddCarPic.setImageResource(R.mipmap.ic_benzine)
                }
            }
        })

        btAccept.setOnClickListener {

            if(myView.etCarBrand.text.toString() == "" || myView.etCarModel.text.toString() == "") Toast.makeText(activity!!.baseContext, "Fill out everything", Toast.LENGTH_LONG).show()
            else{
                (activity!! as AddCarActivity).passDefaults(myView.spCarEngine.selectedItem.toString(), myView.etCarModel.text.toString(), myView.etCarBrand.text.toString())
            val fm = activity!!.supportFragmentManager.beginTransaction()
            fm.replace(R.id.flFragmentPlace, AddCarInfosFragment())
            fm.commit()}
        }

        btCancel.setOnClickListener {
            Toast.makeText(activity!!.baseContext,"Cancelled",Toast.LENGTH_LONG).show()
            activity!!.onBackPressed()
        }
    }
}