package com.example.adminfoodap.Adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.adminfoodap.R
import com.example.adminfoodap.databinding.DeliveyItemBinding

class DeliveryAdapter(
    private val customerNames: MutableList<String>,
    private val moneyStatus: MutableList<Boolean>
) : RecyclerView.Adapter<DeliveryAdapter.DeliveryViewHolder>() {

    inner class DeliveryViewHolder(private val binding: DeliveyItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                customerName.text = customerNames[position]
                statusMoney.text = if (moneyStatus[position]) "Received" else "Not Received"

                val colorMap = mapOf(
                    true to R.color.GREEN,
                    false to R.color.RED
                )

                val context = binding.root.context
                val statusColor = colorMap[moneyStatus[position]] ?: Color.BLACK

                statusMoney.setTextColor(ContextCompat.getColor(context, statusColor))
                statusColour.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, statusColor))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveryViewHolder {
        val binding = DeliveyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DeliveryViewHolder(binding)
    }

    override fun getItemCount(): Int = customerNames.size

    override fun onBindViewHolder(holder: DeliveryViewHolder, position: Int) {
        holder.bind(position)
    }
}
