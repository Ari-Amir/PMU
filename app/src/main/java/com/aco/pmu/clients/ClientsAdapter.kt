package com.aco.pmu.clients

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aco.pmu.R
import com.aco.pmu.appDatabase.ClientsEntity
import kotlinx.android.synthetic.main.item_clients.view.*

class ClientsAdapter : ListAdapter<ClientsEntity, ClientsAdapter.ClientsHolder>(
    DIFF_CALLBACK
) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ClientsEntity>() {
            override fun areItemsTheSame(oldItem: ClientsEntity, newItem: ClientsEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ClientsEntity, newItem: ClientsEntity): Boolean {
                return oldItem.firstName == newItem.firstName && oldItem.lastName == newItem.lastName
            }
        }
    }

    private var listener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientsHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.item_clients, parent, false)
        return ClientsHolder(itemView)
    }

    override fun onBindViewHolder(holder: ClientsHolder, position: Int) {
        val currentClientsEntity: ClientsEntity = getItem(position)

        holder.textViewFirstName.text = currentClientsEntity.firstName + " " + currentClientsEntity.lastName
    }

    fun getClientAt(position: Int): ClientsEntity {
        return getItem(position)
    }

    inner class ClientsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener?.onItemClick(getItem(position))
                }
            }
        }

        var textViewFirstName: TextView = itemView.text_view_firstAndLastName
    }

    interface OnItemClickListener {
        fun onItemClick(clientsEntity: ClientsEntity)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
}
