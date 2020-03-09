package com.aco.pmu.more


import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.aco.pmu.R
import com.aco.pmu.pigments.PigmentsFragment
import com.aco.pmu.procedures.ProceduresFragment
import kotlinx.android.synthetic.main.fragment_more.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


class MoreFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_more, container, false)


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        toolbar.title = "Еще"

        proceduresButton.setOnClickListener {
            var fragment = ProceduresFragment()
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragment_more, fragment)
                ?.addToBackStack(null)
                ?.commit()
        }

        pigmentsButton.setOnClickListener {
            var fragment = PigmentsFragment()
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragment_more, fragment)
                ?.addToBackStack(null)
                ?.commit()
        }

        backupButton.setOnClickListener {
            backup()
        }

        restoreButton.setOnClickListener {
            restore()
        }
    }

    private fun backup() {
        try
        {
            val databaseDirectory = Environment.getDataDirectory()
            val storageDirectory = Environment.getExternalStorageDirectory()
            if (storageDirectory.canWrite()) {
                val databaseFileName1 = "//data//com.aco.pmu//databases//PMUs_database"
                val storageFileName1 = "PMUs_database"
                val databaseFile1 = File(databaseDirectory, databaseFileName1)
                val storageFile1 = File(storageDirectory, storageFileName1)
                val input1 = FileInputStream(databaseFile1).getChannel()
                val output1 = FileOutputStream(storageFile1).getChannel()
                output1.transferFrom(input1, 0, input1.size())
                input1.close()
                output1.close()

                val databaseFileName2 = "//data//com.aco.pmu//databases//PMUs_database-shm"
                val storageFileName2 = "PMUs_database-shm"
                val databaseFile2 = File(databaseDirectory, databaseFileName2)
                val storageFile2 = File(storageDirectory, storageFileName2)
                val input2 = FileInputStream(databaseFile2).getChannel()
                val output2 = FileOutputStream(storageFile2).getChannel()
                output2.transferFrom(input2, 0, input2.size())
                input2.close()
                output2.close()

                val databaseFileName3 = "//data//com.aco.pmu//databases//PMUs_database-wal"
                val storageFileName3 = "PMUs_database-wal"
                val databaseFile3 = File(databaseDirectory, databaseFileName3)
                val storageFile3 = File(storageDirectory, storageFileName3)
                val input3 = FileInputStream(databaseFile3).getChannel()
                val output3 = FileOutputStream(storageFile3).getChannel()
                output3.transferFrom(input3, 0, input3.size())
                input3.close()
                output3.close()

                Toast.makeText(context, "Backup successfully done", Toast.LENGTH_SHORT).show()
            }
        }
        catch (e:Exception) {
            Log.e("Error", e.message)
        }
    }

    private fun restore() {
        try
        {
            val databaseDirectory = Environment.getDataDirectory()
            val storageDirectory = Environment.getExternalStorageDirectory()
            if (storageDirectory.canWrite()) {
                val databaseFileName1 = "//data//com.aco.pmu//databases//PMUs_database"
                val storageFileName1 = "PMUs_database"
                val databaseFile1 = File(databaseDirectory, databaseFileName1)
                val storageFile1 = File(storageDirectory, storageFileName1)
                val input1 = FileInputStream(storageFile1).getChannel()
                val output1 = FileOutputStream(databaseFile1).getChannel()
                output1.transferFrom(input1, 0, input1.size())
                input1.close()
                output1.close()

                val databaseFileName2 = "//data//com.aco.pmu//databases//PMUs_database-shm"
                val storageFileName2 = "PMUs_database-shm"
                val databaseFile2 = File(databaseDirectory, databaseFileName2)
                val storageFile2 = File(storageDirectory, storageFileName2)
                val input2 = FileInputStream(storageFile2).getChannel()
                val output2 = FileOutputStream(databaseFile2).getChannel()
                output2.transferFrom(input2, 0, input2.size())
                input2.close()
                output2.close()

                val databaseFileName3 = "//data//com.aco.pmu//databases//PMUs_database-wal"
                val storageFileName3 = "PMUs_database-wal"
                val databaseFile3 = File(databaseDirectory, databaseFileName3)
                val storageFile3 = File(storageDirectory, storageFileName3)
                val input3 = FileInputStream(storageFile3).getChannel()
                val output3 = FileOutputStream(databaseFile3).getChannel()
                output3.transferFrom(input3, 0, input3.size())
                input3.close()
                output3.close()

                Toast.makeText(context, "Restore successfully done", Toast.LENGTH_SHORT).show()
            }
        }
        catch (e:Exception) {
            Log.e("Error", e.message)
        }
    }
}
