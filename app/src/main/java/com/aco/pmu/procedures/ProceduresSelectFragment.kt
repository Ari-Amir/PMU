package com.aco.pmu.procedures

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
import com.aco.pmu.records.AddRecordActivity.Companion.EXTRA_PROCEDURES
import com.aco.pmu.R
import com.aco.pmu.appDatabase.ProceduresEntity
import kotlinx.android.synthetic.main.fragment_procedures.*


class ProceduresSelectFragment : DialogFragment() {


    private lateinit var proceduresViewModel: ProceduresViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_select_procedures, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recycler_view.layoutManager = LinearLayoutManager(activity)

        var adapter = ProceduresAdapter()

        recycler_view.adapter = adapter

        proceduresViewModel = ViewModelProviders.of(this).get(ProceduresViewModel::class.java)

        proceduresViewModel.getAllProcedures().observe(this, Observer<List<ProceduresEntity>> {
            adapter.submitList(it)
        })

        adapter.setOnItemClickListener(object : ProceduresAdapter.OnItemClickListener {
            override fun onItemClick(proceduresEntity: ProceduresEntity) {

                var intent = Intent(activity, AddRecordActivity::class.java)
                intent.putExtra(
                    EXTRA_PROCEDURES,
                    proceduresEntity.procedureName
                )

                (activity as AddRecordActivity).sendProceduresData(intent)

                activity?.supportFragmentManager?.popBackStack()

            }
        })
    }
}

