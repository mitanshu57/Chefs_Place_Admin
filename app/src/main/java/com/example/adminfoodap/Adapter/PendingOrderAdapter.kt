package com.example.adminfoodap.Adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adminfoodap.databinding.PendingOrderItemBinding

class PendingOrderAdapter(
    private val customerNames: MutableList<String>,
    private val totalPrices: MutableList<String>,
    private val foodImages: MutableList<String>,
    private val context: Context,
    private val itemClicked: OnItemClicked
) : RecyclerView.Adapter<PendingOrderAdapter.PendingViewHolder>() {

    interface OnItemClicked {
        fun onItemClickListener(position: Int)
        fun onItemAcceptClickListener(position: Int)
        fun onItemDispatchClickListener(position: Int)
    }

    inner class PendingViewHolder(private val binding: PendingOrderItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private var isAccepted = false

        fun bind(position: Int) {
            binding.apply {
                customerItemName.text = customerNames[position]
                customerItemQuantity.text = totalPrices[position]

                val uriString = foodImages[position]
                val uri = Uri.parse(uriString)
                Glide.with(context).load(uri).into(customerItemImage)

                orderAcceptButton.apply {
                    text = if (!isAccepted) "Accept" else "Dispatch"
                    setOnClickListener {
                        if (!isAccepted) {
                            text = "Dispatch"
                            isAccepted = true
                            Toast.makeText(context, "Order is Accepted!", Toast.LENGTH_SHORT).show()
                            itemClicked.onItemAcceptClickListener(position)
                        } else {
                            removeItem(position)
                            Toast.makeText(context, "Order is Dispatched!", Toast.LENGTH_SHORT).show()
                            itemClicked.onItemDispatchClickListener(position)
                        }
                    }
                }
                itemView.setOnClickListener { itemClicked.onItemClickListener(position) }
            }
        }

        private fun removeItem(position: Int) {
            customerNames.removeAt(position)
            totalPrices.removeAt(position)
            foodImages.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemCount)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingViewHolder {
        val binding = PendingOrderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PendingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PendingViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = customerNames.size
}
