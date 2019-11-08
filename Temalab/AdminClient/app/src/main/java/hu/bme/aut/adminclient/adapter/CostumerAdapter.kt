package hu.bme.aut.adminclient.adapter

import android.content.Context
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.RequestOptions
import hu.bme.aut.adminclient.R
import hu.bme.aut.adminclient.model.Costumer
import kotlinx.android.synthetic.main.activity_costumer_detail.*
import kotlinx.android.synthetic.main.costumer_row.view.*
import kotlinx.android.synthetic.main.costumer_row.view.ivProfilePic
import java.io.File
import javax.xml.transform.Templates

class CostumerAdapter : RecyclerView.Adapter<CostumerAdapter.CostumerHolder>() {

    private val costumerList = mutableListOf<Costumer>()

    var itemClickListener : CostumerItemClickListener? = null

    lateinit var  context : Context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CostumerHolder {
        Log.d("recview","on create view holder")
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.costumer_row, parent, false)
        context=parent.context
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

        val loginDetails="admin:admin"
        val authHeader = "Basic " + Base64.encodeToString(loginDetails.toByteArray(), Base64.NO_WRAP)

        val pictureUrl ="https://upload.wikimedia.org/wikipedia/commons/3/3f/JPEG_example_flower.jpg"
            //"http://ec2-3-14-28-216.us-east-2.compute.amazonaws.com/customers/${costumer.customerId}/profile-image"
        val url = GlideUrl(pictureUrl, LazyHeaders.Builder().addHeader("Authorization",authHeader).build())
        val options = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE)

        Glide.with(context)
            .load(url)
            .apply(options)
            .into(holder.ivProfilePic)

        Log.d("recview",holder.tvFirstName.text.toString())
        Log.d("recview",holder.tvLastName.text.toString())
    }

    inner class CostumerHolder(costumerView : View) : RecyclerView.ViewHolder(costumerView) {
        val tvFirstName: TextView = costumerView.tvFirstName
        val tvLastName: TextView = costumerView.tvLastName
        val ivProfilePic = costumerView.ivProfilePic

        var costumer : Costumer? = null
        init{
            costumerView.setOnClickListener{
                Log.d("detview","item clicked")
                costumer?.let{itemClickListener?.onCostumerSelected(it)}
            }
        }
    }

    interface CostumerItemClickListener {
        fun onCostumerSelected(costumer: Costumer)
    }
}
