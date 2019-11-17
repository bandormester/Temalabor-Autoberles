package hu.bme.aut.adminclient.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.gson.GsonBuilder
import hu.bme.aut.adminclient.CostumerDetailActivity
import hu.bme.aut.adminclient.NavigationActivity
import hu.bme.aut.adminclient.R
import hu.bme.aut.adminclient.adapter.CostumerAdapter
import hu.bme.aut.adminclient.model.Costumer
import hu.bme.aut.adminclient.retrofit.RetroListUsers
import kotlinx.android.synthetic.main.activity_list_users.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ListUsersFragment : Fragment(), CostumerAdapter.CostumerItemClickListener {

    private lateinit var costumerAdapter: CostumerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_list_users)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        username = (activity as NavigationActivity).getUsername()
        password = (activity as NavigationActivity).getPassword()
        return inflater.inflate(R.layout.activity_list_users,container,false)
    }

    lateinit var username : String
    lateinit var password : String

    override fun onStart() {
        super.onStart()
        costumerAdapter = CostumerAdapter()
        RecyclerViewUsers.adapter = costumerAdapter

        val gson = GsonBuilder().setLenient().create()

        val builder : Retrofit.Builder = Retrofit.Builder()
            .baseUrl("http://ec2-3-14-28-216.us-east-2.compute.amazonaws.com")
            .addConverterFactory(GsonConverterFactory.create(gson))

        val retrofit : Retrofit = builder.build()

        val retroListUsers = retrofit.create(RetroListUsers::class.java)

        // username = intent.getStringExtra("username")?:""
         //password = intent.getStringExtra("password")?:""
        val loginDetails = "$username:$password"

        val header : String = "Basic " + Base64.encodeToString(loginDetails.toByteArray(), Base64.NO_WRAP)

        val call = retroListUsers.getUserList(header)

        call.enqueue(object : Callback<List<Costumer>> {
            override fun onFailure(call: Call<List<Costumer>>, t: Throwable) {
                Log.d("retrofit", "Listing failed")
            }

            override fun onResponse(
                call: Call<List<Costumer>>,
                response: Response<List<Costumer>>
            ) {
                Log.d("retrofit", "Listing succeeded")
                var Users = response.body()?: listOf<Costumer>()
                setupRecyclerView(Users)

                Log.d("retrofit", Users[1].firstName)
            }

        })
    }

    private fun setupRecyclerView(UserList : List<Costumer>) {

        costumerAdapter.itemClickListener = this
        costumerAdapter.clear()
        costumerAdapter.addAll(UserList)
        RecyclerViewUsers.adapter = costumerAdapter
    }

    override fun onCostumerSelected(costumer: Costumer) {
        Log.d("detview","user clicked")
        val intent = Intent(activity, CostumerDetailActivity::class.java)
        intent.putExtra(CostumerDetailActivity.DETAIL_ID,costumer.customerId)
        intent.putExtra(CostumerDetailActivity.USER_NAME,username)
        intent.putExtra(CostumerDetailActivity.USER_PASS,password)
        Log.d("retrofit",costumer.customerId.toString())
        intent.putExtra(CostumerDetailActivity.DETAIL_EMAIL,costumer.emailAddress)
        intent.putExtra(CostumerDetailActivity.DETAIL_PHONE,costumer.phone)
        intent.putExtra(CostumerDetailActivity.DETAIL_LICENCE,costumer.licenceCardNumber)
        intent.putExtra(CostumerDetailActivity.DETAIL_FIRST,costumer.firstName)
        intent.putExtra(CostumerDetailActivity.DETAIL_LAST,costumer.lastName)
        intent.putExtra(CostumerDetailActivity.DETAIL_EXPIRATION,costumer.expirationDate)
        intent.putExtra(CostumerDetailActivity.DETAIL_ENABLED,costumer.enabled)
        startActivity(intent)
    }
}
