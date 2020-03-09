package com.aco.pmu.pigments

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
import com.aco.pmu.R
import com.aco.pmu.appDatabase.PigmentsEntity
import com.aco.pmu.records.AddRecordActivity.Companion.EXTRA_PIGMENT1
import com.aco.pmu.records.AddRecordActivity.Companion.EXTRA_PIGMENT2
import kotlinx.android.synthetic.main.fragment_select_pigments.*


class PigmentsSelectFragment2 : DialogFragment() {


    private lateinit var pigmentsViewModel: PigmentsViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_select_pigments, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recycler_view.layoutManager = LinearLayoutManager(activity)

        var adapter = PigmentsAdapter()

        recycler_view.adapter = adapter

        pigmentsViewModel = ViewModelProviders.of(this).get(PigmentsViewModel::class.java)

        pigmentsViewModel.getAlPigments().observe(this, Observer<List<PigmentsEntity>> {
            adapter.submitList(it)
        })

        adapter.setOnItemClickListener(object : PigmentsAdapter.OnItemClickListener {
            override fun onItemClick(pigmentsEntity: PigmentsEntity) {

                var intent = Intent(activity, AddRecordActivity::class.java)
                intent.putExtra(
                    EXTRA_PIGMENT2,
                    pigmentsEntity.pigmentName
                )

                (activity as AddRecordActivity).sendPigmentsData2(intent)

                activity?.supportFragmentManager?.popBackStack()

            }
        })
    }
}

