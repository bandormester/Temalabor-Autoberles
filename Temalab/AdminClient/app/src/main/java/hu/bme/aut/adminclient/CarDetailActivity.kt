package hu.bme.aut.adminclient

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import hu.bme.aut.adminclient.model.Car
import hu.bme.aut.adminclient.model.State
import kotlinx.android.synthetic.main.activity_car_detail.*

class CarDetailActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {


    companion object{
        const val CAR_ID = "car.id"
        const val CAR_MODEL = "car.model"
        const val CAR_KM = "car.km"
        const val CAR_STATE = "car.state"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_detail)
        val car = intent.getSerializableExtra("carasd") as? Car
        Log.d("retrofit",car?.carId.toString())

        var list_of_items = arrayOf(State.RENTABLE, State.MAINTENANCE, State.SHIPPING, State.RENTED)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, list_of_items)
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)

        spCarState.onItemSelectedListener=this
        spCarState.adapter = adapter
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Log.d("retrofit", (spCarState.selectedItem as State).toString())
    }
}
