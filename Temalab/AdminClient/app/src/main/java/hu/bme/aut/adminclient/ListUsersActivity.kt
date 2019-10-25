package hu.bme.aut.adminclient

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import hu.bme.aut.adminclient.adapter.CostumerAdapter
import hu.bme.aut.adminclient.model.Costumer
import hu.bme.aut.adminclient.retrofit.RetroListUsers
import kotlinx.android.synthetic.main.activity_after_login.*
import kotlinx.android.synthetic.main.activity_list_users.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ListUsersActivity : AppCompatActivity() {

    private lateinit var costumerAdapter: CostumerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_users)

        var gson = GsonBuilder().setLenient().create()

        var builder : Retrofit.Builder = Retrofit.Builder()
            .baseUrl("https://penzfeldobas.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))

        var retrofit : Retrofit = builder.build()

        val retroListUsers = retrofit.create(RetroListUsers::class.java)

        val loginDetails = "admin:admin"

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
                /*var Usernames = arrayOfNulls<String>(Users!!.size)

                for(i in 0 until Users.size){
                    Usernames[i] = Users[i].firstName+" "+Users[i].lastName
                }

                val adapter = ArrayAdapter(applicationContext, android.R.layout.simple_list_item_1, Usernames)
                lvUsers.adapter = adapter*/
            }

        })

    }

    private fun setupRecyclerView(UserList : List<Costumer>) {
        costumerAdapter = CostumerAdapter()
        costumerAdapter.addItem(UserList[1])
        costumerAdapter.addItem(UserList[2])
        costumerAdapter.addAll(UserList)
        RecyclerViewUsers.adapter = costumerAdapter
    }
}
