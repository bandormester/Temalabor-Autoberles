package hu.bme.aut.adminclient.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.adminclient.R
import hu.bme.aut.adminclient.model.Costumer
import kotlinx.android.synthetic.main.costumer_row.view.*
import java.io.File
import javax.xml.transform.Templates

class CostumerAdapter : RecyclerView.Adapter<CostumerAdapter.CostumerHolder>() {

    private val costumerList = mutableListOf<Costumer>()

    var itemClickListener : CostumerItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CostumerHolder {
        Log.d("recview","on create view holder")
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.costumer_row, parent, false)
        return CostumerHolder(view)
    }


     fun addItem(item : Costumer) {
         Log.d("recview","add-item")
         val size = costumerList.size
        costumerList.add(item)
        notifyItemInserted(size)
    }

    fun addAll(costumers : List<Costumer>){
        val size = costumerList.size
        Log.d("recview",costumers[2].firstName)
        costumerList += costumers
        Log.d("recview",costumerList[3].firstName)
        notifyItemRangeChanged(size, costumers.size)
    }

    override fun getItemCount(): Int {
        return costumerList.size
    }

    override fun onBindViewHolder(holder: CostumerHolder, position: Int) {
       val costumer = costumerList[position]

        holder.costumer = costumer

        holder.tvFirstName.text = costumer.firstName
        holder.tvLastName.text = costumer.lastName
        Log.d("recview",holder.tvFirstName.text.toString())
        Log.d("recview",holder.tvLastName.text.toString())
    }

    inner class CostumerHolder(costumerView : View) : RecyclerView.ViewHolder(costumerView) {
        val tvFirstName: TextView = costumerView.tvFirstName
        val tvLastName: TextView = costumerView.tvLastName

        var costumer : Costumer? = null
    }

    interface CostumerItemClickListener {

    }
}
