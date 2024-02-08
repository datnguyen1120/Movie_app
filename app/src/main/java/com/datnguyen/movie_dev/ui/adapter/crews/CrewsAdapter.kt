package com.datnguyen.movie_dev.ui.adapter.crews

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.datnguyen.movie_dev.R
import com.datnguyen.movie_dev.app_model.Crew
import com.datnguyen.movie_dev.extras.LiveEvent
import com.datnguyen.movie_dev.utils.Utils


class CrewsAdapter(private val crews: MutableList<Crew?>) : RecyclerView.Adapter<CrewsVH>() {
    var itemWidth: Int
    var itemClickEvent = LiveEvent<Crew?>()

    init {
        val screenWidth = Utils.getScreenWidth()
        val paddingSize: Int = Utils.convertDPtoPixel(16 * 2)    //padding
        itemWidth = (screenWidth - paddingSize) / 2  //item width
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrewsVH {
        val viewLayout =
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_crew,
                parent, false
            )
        return CrewsVH(viewLayout)
    }

    override fun onBindViewHolder(holder: CrewsVH, position: Int) {
        val crew = crews[position]
        holder.tvName.text = crew?.name
        holder.tvJob.text = crew?.job

        //set item width
        holder.parent.layoutParams.width = itemWidth

        //set event click
        holder.parent.setOnClickListener {
            itemClickEvent.postValue(crew)
        }
    }

    override fun getItemCount(): Int {
        return crews.size
    }
}