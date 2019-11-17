package hu.bme.aut.adminclient.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import hu.bme.aut.adminclient.R
import kotlinx.android.synthetic.main.fragment_new_car.*

class AddCarDialog : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_new_car,null)
    }

    override fun onStart() {
        super.onStart()

        btAccept.setOnClickListener {
            Toast.makeText(activity!!.baseContext,"Car created",Toast.LENGTH_LONG).show()
            dismiss()
        }

        btCancel.setOnClickListener {
            Toast.makeText(activity!!.baseContext,"Car created",Toast.LENGTH_LONG).show()
            dismiss()
        }
    }
}