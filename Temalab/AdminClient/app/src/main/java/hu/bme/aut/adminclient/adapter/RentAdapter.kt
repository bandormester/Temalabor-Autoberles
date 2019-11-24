package hu.bme.aut.adminclient.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.adminclient.R
import hu.bme.aut.adminclient.model.Rent
import kotlinx.android.synthetic.main.rent_row.view.*

class RentAdapter : RecyclerView.Adapter<RentAdapter.RentHolder>(){

    lateinit var  context : Context

    private val rentList = mutableListOf<Rent>()

    var itemClickListener : RentItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RentHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rent_row, parent, false)
        context = parent.context
        return RentHolder(view)
    }

    override fun getItemCount(): Int {
        return rentList.size
    }

    override fun onBindViewHolder(holder: RentHolder, position: Int) {
        val rent = rentList[position]

        holder.rent = rent
        holder.tvRentActualStart.text = rent.actualStartTime
        holder.tvRentPlannedStart.text = rent.plannedStartTime
        holder.tvRentActualEnd.text = rent.actualEndTime
        holder.tvRentPlannedEnd.text = rent.plannedEndTime
        holder.tvStartStation.append(rent.startStationId.toString())
        holder.tvEndStation.append(rent.endStationId.toString())
    }

    fun addAll(rent : List<Rent>){
        val size = rentList.size
        rentList+=rent
        notifyItemRangeChanged(size, rent.size)
    }


    inner class RentHolder(rentView : View) : RecyclerView.ViewHolder(rentView){
        val tvRentActualStart: TextView = rentView.tvRentActualStart
        val tvRentPlannedStart: TextView = rentView.tvRentPlannedStart
        val tvRentActualEnd: TextView = rentView.tvRentActualEnd
        val tvRentPlannedEnd: TextView = rentView.tvRentPlannedEnd
        val tvStartStation: TextView = rentView.tvRentStartStation
        val tvEndStation: TextView = rentView.tvRendEndStation

        var rent : Rent? = null
        init{
            rentView.setOnClickListener{
                rent?.let { it1 -> itemClickListener?.onRentSelected(it1) }
            }
        }

    }

    interface RentItemClickListener{
        fun onRentSelected(rent : Rent)
    }
}