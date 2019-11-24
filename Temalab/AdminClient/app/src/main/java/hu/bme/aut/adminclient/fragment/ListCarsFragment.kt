package hu.bme.aut.adminclient.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import com.google.gson.GsonBuilder
import hu.bme.aut.adminclient.AddCarActivity
import hu.bme.aut.adminclient.CarDetailActivity
import hu.bme.aut.adminclient.NavigationActivity
import hu.bme.aut.adminclient.R
import hu.bme.aut.adminclient.adapter.CarAdapter
import hu.bme.aut.adminclient.model.Car
import hu.bme.aut.adminclient.retrofit.RetroListCars
import kotlinx.android.synthetic.main.activity_car_detail.*
import kotlinx.android.synthetic.main.activity_list_cars.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ListCarsFragment : Fragment(), CarAdapter.CarItemClickListener {


    private lateinit var carAdapter: CarAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    lateinit var username : String
    lateinit var password : String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        username = (activity as NavigationActivity).getUsername()//arguments!!.getString("username","")
        password = (activity as NavigationActivity).getPassword()//arguments!!.getString("password","")

        Log.d("retrofit",username)
        Log.d("retrofit",password)
        return inflater.inflate(R.layout.activity_list_cars, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater!!.inflate(R.menu.new_car,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
            /*val newCarDialog = AddCarDialog()
            newCarDialog.show(activity!!.supportFragmentManager,"AddCarDialog")
*/
        val intent = Intent()
        intent.setClass(activity!!.baseContext, AddCarActivity::class.java)
        intent.putExtra("username", username)
        intent.putExtra("password", password)
        startActivity(intent)

        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()

        carAdapter = CarAdapter()
        RecyclerViewCars.adapter = carAdapter

        val gson = GsonBuilder().setLenient().create()
        val builder : Retrofit.Builder = Retrofit.Builder()
            .baseUrl("http://ec2-3-14-28-216.us-east-2.compute.amazonaws.com")
            .addConverterFactory(GsonConverterFactory.create(gson))
        val retrofit : Retrofit = builder.build()
        val retroListCars = retrofit.create(RetroListCars::class.java)
       // username = // intent.getStringExtra("username")?:""
        //password = "admin" // intent.getStringExtra("password")?:""
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
        val intent = Intent(activity, CarDetailActivity::class.java)
        //intent.putExtra(CarDetailActivity.CAR_ID, car.carId)
        //intent.putExtra(CarDetailActivity.CAR_MODEL, car.model)
        Log.d("retrofit", username)
        Log.d("retrofit", password)
        intent.putExtra(CarDetailActivity.USERNAME, username)
        intent.putExtra(CarDetailActivity.PASSWORD, password)
        intent.putExtra(CarDetailActivity.DETAILED_CAR, car)
        startActivity(intent)
    }
}
