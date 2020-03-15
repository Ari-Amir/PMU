package com.aco.pmu.more


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.aco.pmu.R
import com.aco.pmu.googleDrive.GoogleDrive
import com.aco.pmu.pigments.PigmentsFragment
import com.aco.pmu.procedures.ProceduresFragment
import kotlinx.android.synthetic.main.fragment_more.*


class MoreFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_more, container, false)


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        toolbar.title = "Еще"

        proceduresButton.setOnClickListener {
            val fragment = ProceduresFragment()
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragment_more, fragment)
                ?.addToBackStack(null)
                ?.commit()
        }

        pigmentsButton.setOnClickListener {
            val fragment = PigmentsFragment()
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragment_more, fragment)
                ?.addToBackStack(null)
                ?.commit()
        }

        backupAndRestoreButton.setOnClickListener {
            startActivity(Intent(context, GoogleDrive::class.java))
        }
    }
}
