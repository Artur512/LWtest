package com.volcode.cards.ui.boardingcards

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.volcode.cards.databinding.StopItemViewBinding
import com.volcode.cards.domain.capabilities.Card


class CardAdapter : ListAdapter<Card, CardAdapter.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Card>() {
            override fun areItemsTheSame(oldItem: Card, newItem: Card): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Card, newItem: Card): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = StopItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    class ViewHolder(private val view: StopItemViewBinding) : RecyclerView.ViewHolder(view.root) {

        fun onBind(card: Card) {
            val title = "${card.departure.name} - ${card.arrival.name} "
            view.trace.text = title
            view.vehicle.text = card.vehicleType
        }
    }
}
