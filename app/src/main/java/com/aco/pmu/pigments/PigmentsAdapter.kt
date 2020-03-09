package com.aco.pmu.pigments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aco.pmu.R
import com.aco.pmu.appDatabase.PigmentsEntity
import kotlinx.android.synthetic.main.item_pigments.view.*

class PigmentsAdapter : ListAdapter<PigmentsEntity, PigmentsAdapter.PigmentsHolder>(
    DIFF_CALLBACK
) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PigmentsEntity>() {
            override fun areItemsTheSame(oldItem: PigmentsEntity, newItem: PigmentsEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: PigmentsEntity, newItem: PigmentsEntity): Boolean {
                return oldItem.pigmentName == newItem.pigmentName
            }
        }
    }

    private var listener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PigmentsHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.item_pigments, parent, false)
        return PigmentsHolder(itemView)
    }

    override fun onBindViewHolder(holder: PigmentsHolder, position: Int) {
        val currentPigmentEntity: PigmentsEntity = getItem(position)

        holder.pigmentsNameTextView.text = currentPigmentEntity.pigmentName
    }

    fun getPigmentAt(position: Int): PigmentsEntity {
        return getItem(position)
    }

    inner class PigmentsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener?.onItemClick(getItem(position))
                }
            }
        }

        var pigmentsNameTextView: TextView = itemView.pigmentNameTextView
    }

    interface OnItemClickListener {
        fun onItemClick(PigmentsEntity: PigmentsEntity)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
}
