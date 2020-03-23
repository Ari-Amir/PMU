package com.aco.pmu.records

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aco.pmu.R
import com.aco.pmu.appDatabase.Converters
import com.aco.pmu.appDatabase.RecordsEntity
import kotlinx.android.synthetic.main.item_records.view.*

class RecordsAdapter : ListAdapter<RecordsEntity, RecordsAdapter.RecordsHolder>(
    DIFF_CALLBACK
) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RecordsEntity>() {
            override fun areItemsTheSame(oldItem: RecordsEntity, newItem: RecordsEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: RecordsEntity, newItem: RecordsEntity): Boolean {
                    return oldItem.date == newItem.date
                            && oldItem.time == newItem.time
                            && oldItem.firstAndlastNames == newItem.firstAndlastNames
                            && oldItem.phoneNumber1 == newItem.phoneNumber1
                            && oldItem.procedure == newItem.procedure
                            && oldItem.price == newItem.price
            }
        }
    }

    private var listener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordsHolder {
        var itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_records, parent, false)
        return RecordsHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecordsHolder, position: Int) {
        val currentRecordsEntity: RecordsEntity = getItem(position)

            if (currentRecordsEntity.price.isNotBlank()) {
                holder.textViewDate.setTextAppearance(R.style.Bold_15_Gray)
                holder.textViewTime.setTextAppearance(R.style.Bold_15_Gray)
                holder.textViewFirstAndLastName.setTextAppearance(R.style.Bold_12_Gray)
                holder.textViewPhoneNumber.setTextAppearance(R.style.Bold_12_Gray)
                holder.textViewProcedure.setTextAppearance(R.style.Italic_12_Gray)

                holder.textViewDate.text = Converters().timestampToDate(currentRecordsEntity.date)
                holder.textViewTime.text = currentRecordsEntity.time
                holder.textViewFirstAndLastName.text = currentRecordsEntity.firstAndlastNames
                holder.textViewPhoneNumber.text = currentRecordsEntity.phoneNumber1.trim(',')
                holder.textViewProcedure.text = currentRecordsEntity.procedure
                holder.textViewPrice.text = currentRecordsEntity.price + " тг."

            } else {
                holder.textViewDate.setTextAppearance(R.style.Bold_15_White)
                holder.textViewTime.setTextAppearance(R.style.Bold_15_White)
                holder.textViewFirstAndLastName.setTextAppearance(R.style.Bold_12_White)
                holder.textViewPhoneNumber.setTextAppearance(R.style.Bold_12_White)
                holder.textViewProcedure.setTextAppearance(R.style.Italic_12_White)

                holder.textViewDate.text = Converters().timestampToDate(currentRecordsEntity.date)
                holder.textViewTime.text = currentRecordsEntity.time
                holder.textViewFirstAndLastName.text = currentRecordsEntity.firstAndlastNames
                holder.textViewPhoneNumber.text = currentRecordsEntity.phoneNumber1.trim(',')
                holder.textViewProcedure.text = currentRecordsEntity.procedure
                holder.textViewPrice.text = currentRecordsEntity.price
        }
    }

    fun getRecordAt(position: Int): RecordsEntity {
        return getItem(position)
    }

    inner class RecordsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener?.onItemClick(getItem(position))
                }
            }
        }

        var textViewDate: TextView = itemView.dateTextView
        var textViewTime: TextView = itemView.timeTextView
        var textViewFirstAndLastName: TextView = itemView.firstAndLastNamesTextView
        var textViewPhoneNumber: TextView = itemView.phoneNumberTextView
        var textViewProcedure: TextView = itemView.procedureTextView
        var textViewPrice: TextView = itemView.priceTextView

    }

    interface OnItemClickListener {
        fun onItemClick(recordsEntity: RecordsEntity)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
}
