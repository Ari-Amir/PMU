package com.aco.pmu.procedures

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aco.pmu.R
import com.aco.pmu.appDatabase.ProceduresEntity
import kotlinx.android.synthetic.main.item_procedures.view.*

class ProceduresAdapter : ListAdapter<ProceduresEntity, ProceduresAdapter.ProceduresHolder>(
    DIFF_CALLBACK
) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ProceduresEntity>() {
            override fun areItemsTheSame(oldItem: ProceduresEntity, newItem: ProceduresEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ProceduresEntity, newItem: ProceduresEntity): Boolean {
                return oldItem.procedureName == newItem.procedureName
            }
        }
    }

    private var listener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProceduresHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.item_procedures, parent, false)
        return ProceduresHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProceduresHolder, position: Int) {
        val currentProceduresEntity: ProceduresEntity = getItem(position)

        holder.procedureNameTextView.text = currentProceduresEntity.procedureName
    }

    fun getProcedureAt(position: Int): ProceduresEntity {
        return getItem(position)
    }

    inner class ProceduresHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener?.onItemClick(getItem(position))
                }
            }
        }

        var procedureNameTextView: TextView = itemView.procedureNameTextView
    }

    interface OnItemClickListener {
        fun onItemClick(proceduresEntity: ProceduresEntity)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
}
