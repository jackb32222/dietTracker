package com.diet.tracker.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.diet.tracker.R
import com.diet.tracker.datasource.model.Weight
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class WeightAdapter(var weightList: List<Weight>, private val onWeightChangeListener: OnWeightChangeListener) : RecyclerView.Adapter<WeightAdapter.WeightViewHolder>() {

    interface OnWeightChangeListener {
        fun onWeightChanged(position: Int, weight: Double)
    }

    inner class WeightViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(weight: Weight) {
            val inputLayout = itemView.findViewById<TextInputLayout>(R.id.inputLayout)
            inputLayout.hint = "Day ${weight.day} weight"
            if (weight.weight > 0.0) {
                inputLayout.editText?.setText(weight.weight.toString())
            } else {
                inputLayout.editText?.setText("")
            }

            inputLayout.editText?.addTextChangedListener(
                afterTextChanged = {
                    val value = it?.toString()
                    if (!value.isNullOrEmpty()) {
                        val weightValue = value.toDouble()
                        onWeightChangeListener.onWeightChanged(adapterPosition, weightValue)
                    }
                }
            )

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