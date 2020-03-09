package com.aco.pmu.clients

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.aco.pmu.records.AddRecordActivity
import com.aco.pmu.records.AddRecordActivity.Companion.EXTRA_FIRSTANDLASTNAME
import com.aco.pmu.records.AddRecordActivity.Companion.EXTRA_PHONENUMBER1
import com.aco.pmu.R
import com.aco.pmu.appDatabase.ClientsEntity
import com.aco.pmu.records.AddRecordActivity.Companion.EXTRA_PHONENUMBER2
import kotlinx.android.synthetic.main.fragment_clients.*


class ClientsSelectFragment : DialogFragment() {

    private lateinit var clientsViewModel: ClientsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_select_clients, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recycler_view.layoutManager = LinearLayoutManager(activity)

        var adapter = ClientsAdapter()

        recycler_view.adapter = adapter

        clientsViewModel = ViewModelProviders.of(this).get(ClientsViewModel::class.java)

        clientsViewModel.getAllClients().observe(viewLifecycleOwner, Observer<List<ClientsEntity>> {
            adapter.submitList(it)
        })

        adapter.setOnItemClickListener(object : ClientsAdapter.OnItemClickListener {
            override fun onItemClick(clientsEntity: ClientsEntity) {

                val n1 = clientsEntity.phoneNumber1
                val n2 = clientsEntity.phoneNumber2

                var intent = Intent(activity, AddRecordActivity::class.java)
                intent.putExtra(
                    EXTRA_FIRSTANDLASTNAME,
                    clientsEntity.firstName + " " + clientsEntity.lastName
                )

                if (clientsEntity.phoneNumber2.isNotEmpty()) {
                    intent.putExtra(
                        EXTRA_PHONENUMBER1,
                        AddRecordActivity().phoneMask(n1)
                    )

                    intent.putExtra(
                        EXTRA_PHONENUMBER2,
                        AddRecordActivity().phoneMask(n2)
                    )
                } else {
                    intent.putExtra(
                        EXTRA_PHONENUMBER1,
                        AddRecordActivity().phoneMask(n1))
                }

                (activity as AddRecordActivity).sendClientsData(intent)

                activity?.supportFragmentManager?.popBackStack()

            }
        })
    }
}
