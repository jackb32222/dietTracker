package com.diet.tracker.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.diet.tracker.R
import com.diet.tracker.data.Weight

class WeightAdapter(private val weightList: MutableList<Weight>) : RecyclerView.Adapter<WeightAdapter.WeightViewHolder>() {

    inner class WeightViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(weight: Weight) {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeightViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_weight, parent, false)
        return WeightViewHolder(view)
    }

    override fun onBindViewHolder(holder: WeightViewHolder, position: Int) {
        holder.bind(weightList[position])
    }

    override fun getItemCount() = weightList.size
}