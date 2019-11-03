package hu.bme.aut.adminclient

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_costumer_detail.*

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

        val detailedCostumerID = intent.getIntExtra(DETAIL_ID,0)
        tvFirstName.text = intent.getStringExtra(DETAIL_FIRST)
        tvLastName.text = intent.getStringExtra(DETAIL_LAST)
    }
}
