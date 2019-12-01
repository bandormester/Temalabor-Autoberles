package hu.bme.aut.adminclient.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.google.gson.GsonBuilder
import hu.bme.aut.adminclient.*
import hu.bme.aut.adminclient.adapter.RentAdapter
import hu.bme.aut.adminclient.model.Car
import hu.bme.aut.adminclient.model.Rent
import hu.bme.aut.adminclient.retrofit.RetroListCars
import hu.bme.aut.adminclient.retrofit.RetroListRents
import kotlinx.android.synthetic.main.activity_list_cars.*
import kotlinx.android.synthetic.main.fragment_list_rents.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ListRentsFragment : Fragment(), RentAdapter.RentItemClickListener{


    private lateinit var retroListRents : RetroListRents
    private lateinit var rentAdapter: RentAdapter
    private lateinit var header: String
    private var activeList: Boolean = false
    private var menu : Menu? = null

    lateinit var username : String
    lateinit var password : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        this.menu = menu

        inflater!!.inflate(R.menu.rent_change,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        activeList = !activeList
        if(activeList){
            menu?.findItem(R.id.miSwitchRent)?.setTitle("Unclosed")
            val call = retroListRents.getActiveList(header)
            activity!!.setTitle("Active rents")
            call.enqueue(object : Callback<List<Rent>>{
                override fun onFailure(call: Call<List<Rent>>, t: Throwable) {
                    Log.d("retrofit", "Listing failed")
                    Log.d("retrofit", t.message)
                }

                override fun onResponse(call: Call<List<Rent>>, response: Response<List<Rent>>) {
                    Log.d("retrofit", "Listing succeeded")
                    val rents = response.body()?: mutableListOf<Rent>()
                    setupActiveRecyclerView(rents)
                }

            })
        }else{
            menu?.findItem(R.id.miSwitchRent)?.setTitle("Active")
            val call = retroListRents.getUserList(header)
            activity!!.setTitle("Unclosed rents")

            call.enqueue(object : Callback<List<Rent>>{
                override fun onFailure(call: Call<List<Rent>>, t: Throwable) {
                    Log.d("retrofit", "Listing failed")
                    Log.d("retrofit", t.message)
                }

                override fun onResponse(call: Call<List<Rent>>, response: Response<List<Rent>>) {
                    Log.d("retrofit", "Listing succeeded")
                    val rents = response.body()?: mutableListOf<Rent>()
                    setupRecyclerView(rents)
                }

            })
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        username = (activity as NavigationActivity).getUsername()
        password = (activity as NavigationActivity).getPassword()

        return inflater.inflate(R.layout.fragment_list_rents, container, false)
    }

    override fun onStart() {
        super.onStart()


        rentAdapter = RentAdapter()
        RecyclerViewRents.adapter = rentAdapter
        Log.d("retrofit",activeList.toString())

        activeList = false
        menu?.findItem(R.id.miSwitchRent)?.setTitle("Active")

        val gson = GsonBuilder().setLenient().create()
        val builder : Retrofit.Builder = Retrofit.Builder()
            .baseUrl("http://ec2-3-14-28-216.us-east-2.compute.amazonaws.com")
            .addConverterFactory(GsonConverterFactory.create(gson))
        val retrofit : Retrofit = builder.build()
        retroListRents = retrofit.create(RetroListRents::class.java)
        val loginDetails = "$username:$password"
        header = "Basic " + Base64.encodeToString(loginDetails.toByteArray(), Base64.NO_WRAP)
        val call = retroListRents.getUserList(header)

        call.enqueue(object : Callback<List<Rent>>{
            override fun onFailure(call: Call<List<Rent>>, t: Throwable) {
                Log.d("retrofit", "Listing failed")
            }

            override fun onResponse(call: Call<List<Rent>>, response: Response<List<Rent>>) {
                Log.d("retrofit", "Listing succeeded")
                val rents = response.body()?: mutableListOf<Rent>()
                setupRecyclerView(rents)
            }

        })
    }

    private fun setupRecyclerView(rentList : List<Rent>) {
        rentAdapter.itemClickListener = this
        rentAdapter.wipe()
        rentAdapter.addAll(rentList)
        RecyclerViewRents.adapter = rentAdapter
    }

    private fun setupActiveRecyclerView(rentList : List<Rent>) {
        rentAdapter.itemClickListener = object : RentAdapter.RentItemClickListener{
            override fun onRentSelected(rent: Rent) {
                val intent = Intent(activity, ActiveRentDetailActivity::class.java)
                intent.putExtra("username", username)
                intent.putExtra("password", password)
                intent.putExtra("rent", rent)
                startActivity(intent)
            }

        }
        rentAdapter.wipe()
        rentAdapter.addAll(rentList)
        RecyclerViewRents.adapter = rentAdapter
    }

    override fun onRentSelected(rent: Rent) {
       val intent = Intent(activity, RentDetailActivity::class.java)
        intent.putExtra("username", username)
        intent.putExtra("password", password)
        intent.putExtra("rent", rent)
        startActivity(intent)
    }

}